package net.app.savable.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import net.app.savable.domain.challenge.*;
import net.app.savable.domain.history.RewardHistoryRepository;
import net.app.savable.domain.history.RewardType;
import net.app.savable.domain.history.SavingsHistory;
import net.app.savable.domain.history.SavingsHistoryRepository;
import net.app.savable.domain.history.dto.RewardHistoryResponseDto;
import net.app.savable.domain.history.dto.RewardHistorySaveDto;
import net.app.savable.domain.history.dto.SavingsHistoryResponseDto;
import net.app.savable.domain.history.dto.SavingsHistorySaveDto;
import net.app.savable.domain.member.Member;
import net.app.savable.domain.member.MemberRepository;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Slf4j
@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class SchedulerService {

    private final VerificationRepository verificationRepository;
    private final ParticipationChallengeRepository participationRepository;
    private final MemberRepository memberRepository;
    private final ChallengeRepository challengeRepository;
    private final RewardHistoryRepository rewardHistoryRepository;
    private final SavingsHistoryRepository savingsHistoryRepository;

    @Transactional(readOnly = false)
    @Scheduled(cron = "1 0 0 * * *") // 매일 0시 0분 1초에 실행
    public void checkVerification() {
        log.info("SchedulerService.checkVerification() 실행");
        List<Verification> waitingVerifications = verificationRepository.findByStateIs(VerificationState.WAITING);

        if (!waitingVerifications.isEmpty()) {
            for (Verification verification : waitingVerifications) {
                processVerification(verification);
            }
        }

        rewardUnsuccessfulParticipation();
    }

    private void processVerification(Verification verification) {
        verification.updateState(VerificationState.SUCCESS);

        Long participationId = verification.getParticipationChallengeId();
        Long successCount = verificationRepository.countByParticipationChallengeIdAndState(participationId, VerificationState.SUCCESS);

        ParticipationChallenge participation = participationRepository.findById(participationId)
                .orElseThrow(() -> {
                    log.error("Invalid participation ID: {}", participationId);
                    return new IllegalArgumentException("Invalid participation ID: " + participationId);
                });

        if (isParticipationSuccessful(participation, successCount)) {
            rewardSuccessfulParticipation(participation, successCount);
        }
    }

    private boolean isParticipationSuccessful(ParticipationChallenge participation, Long successCount) { // 챌린지 성공 여부 확인
        return successCount >= participation.getVerificationGoal();
    }

    private void rewardSuccessfulParticipation(ParticipationChallenge participation, Long successCount) { // 챌린지 성공 시 보상 지급
        log.info("챌린지 성공!");
        participation.updateState(ParticipationState.SUCCESS);

        updateRewardAndSavings(participation, successCount, ParticipationState.SUCCESS);
    }

    private void rewardUnsuccessfulParticipation(){
        log.info("실패한 챌린지 보상 지급");
        List<ParticipationChallenge> participationChallenges = participationRepository.findByParticipationStateAndEndDateBefore(ParticipationState.IN_PROGRESS, LocalDate.now());
        for (ParticipationChallenge participation :participationChallenges){
            participation.updateState(ParticipationState.FAIL);

            Member member = memberRepository.findById(participation.getMemberId())
                    .orElseThrow(() -> {
                        log.error("Invalid member ID: {}", participation.getMemberId());
                        return new IllegalArgumentException("Invalid member ID: " + participation.getMemberId());
                    });

            Long successCount = verificationRepository.countByParticipationChallengeIdAndState(participation.getId(), VerificationState.SUCCESS);
            updateRewardAndSavings(participation, successCount, ParticipationState.FAIL);
        }
    }

    private void updateRewardAndSavings(ParticipationChallenge participation, Long count, ParticipationState participationState) { // 보상 및 절약 금액 증가
        Member member = memberRepository.findById(participation.getMemberId())
                .orElseThrow(() -> {
                    log.error("Invalid member ID: {}", participation.getMemberId());
                    return new IllegalArgumentException("Invalid member ID: " + participation.getMemberId());
                });

        Challenge challenge = challengeRepository.findById(participation.getChallengeId())
                .orElseThrow(() -> {
                    log.error("Invalid challenge ID: {}", participation.getChallengeId());
                    return new IllegalArgumentException("Invalid challenge ID: " + participation.getChallengeId());
                });

        // 절약 금액 지급
        Long additionalSavings = participation.getSavings() * count;
        member.updateSavings(additionalSavings); // 절약 금액 증가
        SavingsHistoryResponseDto recentSavingsHistory = savingsHistoryRepository.findTopByMemberIdOrderByCreatedAtDesc(member.getId()); // 가장 최근 절약 내역 조회
        if (recentSavingsHistory == null) {
            recentSavingsHistory = SavingsHistoryResponseDto.builder()
                    .totalSavings(0L)
                    .build();
        }
        SavingsHistorySaveDto savingsHistorySaveDto = SavingsHistorySaveDto.builder()
                .savings(additionalSavings)
                .totalSavings(recentSavingsHistory.getTotalSavings() + additionalSavings)
                .description(challenge.getTitle())
                .member(member)
                .build();

        savingsHistoryRepository.save(savingsHistorySaveDto.toEntity()); // 절약 내역 저장

        // 리워드 지급
        double percentage = (double) count/participation.getVerificationGoal();
        Long additionalReward;
        if (participationState == ParticipationState.SUCCESS) {
            additionalReward = challenge.getReward() * count;
        } else {
            additionalReward = Math.round(challenge.getReward() * count * percentage);
        }
        member.updateReward(additionalReward); // 보상 지급

        RewardHistoryResponseDto recentRewardHistory = rewardHistoryRepository.findTopByMemberIdOrderByCreatedAtDesc(member.getId()); // 가장 최근 리워드 내역 조회
        if (recentRewardHistory == null) {
            recentRewardHistory = RewardHistoryResponseDto.builder()
                    .totalReward(0L)
                    .build();
        }

        RewardHistorySaveDto rewardHistorySaveDto = RewardHistorySaveDto.builder()
                .reward(additionalReward)
                .totalReward(recentRewardHistory.getTotalReward() + additionalReward)
                .rewardType(RewardType.CHALLENGE)
                .description(challenge.getTitle())
                .member(member)
                .build();

        rewardHistoryRepository.save(rewardHistorySaveDto.toEntity()); // 리워드 내역 저장
    }
}
