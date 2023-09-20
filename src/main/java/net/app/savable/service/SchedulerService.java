package net.app.savable.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import net.app.savable.domain.challenge.*;
import net.app.savable.domain.member.Member;
import net.app.savable.domain.member.MemberRepository;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

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

        if (waitingVerifications.isEmpty()) {
            log.warn("No verifications found with WAITING state.");
            return;
        }

        for (Verification verification : waitingVerifications) {
            processVerification(verification);
        }
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

    private boolean isParticipationSuccessful(ParticipationChallenge participation, Long successCount) {
        return successCount >= participation.getVerificationGoal();
    }

    private void rewardSuccessfulParticipation(ParticipationChallenge participation, Long successCount) {
        log.info("챌린지 성공!");
        participation.updateState(ParticipationState.SUCCESS);

        Member member = memberRepository.findById(participation.getMemberId())
                .orElseThrow(() -> {
                    log.error("Invalid member ID: {}", participation.getMemberId());
                    return new IllegalArgumentException("Invalid member ID: " + participation.getMemberId());
                });

        member.updateSavings(participation.getSavings() * successCount);

        Challenge challenge = challengeRepository.findById(participation.getChallengeId())
                .orElseThrow(() -> {
                    log.error("Invalid challenge ID: {}", participation.getChallengeId());
                    return new IllegalArgumentException("Invalid challenge ID: " + participation.getChallengeId());
                });

        member.updateReward(challenge.getReward() * successCount);
    }
}
