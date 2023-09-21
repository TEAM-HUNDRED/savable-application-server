package net.app.savable.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import net.app.savable.domain.challenge.*;
import net.app.savable.domain.member.Member;
import net.app.savable.domain.member.MemberRepository;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class SchedulerService {

    private final VerificationRepository verificationRepository;
    private final ParticipationChallengeRepository participationRepository;
    private final MemberRepository memberRepository;
    private final ChallengeRepository challengeRepository;

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
        Long successCount = verificationRepository.countByParticipationChallenge_IdAndState(participationId, VerificationState.SUCCESS);

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

        Member member = memberRepository.findById(participation.getMemberId())
                .orElseThrow(() -> {
                    log.error("Invalid member ID: {}", participation.getMemberId());
                    return new IllegalArgumentException("Invalid member ID: " + participation.getMemberId());
                });

        member.updateSavings(participation.getSavings() * successCount); // 절약 금액 증가

        Challenge challenge = challengeRepository.findById(participation.getChallengeId())
                .orElseThrow(() -> {
                    log.error("Invalid challenge ID: {}", participation.getChallengeId());
                    return new IllegalArgumentException("Invalid challenge ID: " + participation.getChallengeId());
                });

        member.updateReward(challenge.getReward() * successCount); // 보상 지급
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

            Long successCount = verificationRepository.countByParticipationChallenge_IdAndState(participation.getId(), VerificationState.SUCCESS);
            updateRewardAndSavings(participation, successCount);
        }
    }

    private void updateRewardAndSavings(ParticipationChallenge participation, Long count) {
        Member member = memberRepository.findById(participation.getMemberId())
                .orElseThrow(() -> {
                    log.error("Invalid member ID: {}", participation.getMemberId());
                    return new IllegalArgumentException("Invalid member ID: " + participation.getMemberId());
                });

        member.updateSavings(participation.getSavings() * count); // 절약 금액 증가

        Challenge challenge = challengeRepository.findById(participation.getChallengeId())
                .orElseThrow(() -> {
                    log.error("Invalid challenge ID: {}", participation.getChallengeId());
                    return new IllegalArgumentException("Invalid challenge ID: " + participation.getChallengeId());
                });

        double percentage = (double) count/participation.getVerificationGoal();
        Long reward = Math.round(challenge.getReward() * count * percentage);
        member.updateReward(reward); // 보상 지급
    }
}
