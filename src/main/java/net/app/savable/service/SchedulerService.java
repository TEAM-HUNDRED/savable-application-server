package net.app.savable.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import net.app.savable.domain.challenge.Verification;
import net.app.savable.domain.challenge.VerificationRepository;
import net.app.savable.domain.challenge.VerificationState;
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

    @Transactional(readOnly = false)
    @Scheduled(cron = "0 0 7 * * *") // 매일 7시에 실행
    public void checkVerification() { // 일정 시간에 인증 상태를 SUCCESS로 변경하는 함수.
        log.info("SchedulerService.checkVerification() 실행");
        List<Verification> verifications = verificationRepository.findByStateIs(VerificationState.WAITING); // 인증 상태가 WAITING인 인증

        if (verifications.isEmpty()) { // 인증이 없으면 종료
            log.warn("No verifications found with WAITING state.");
            return;
        }

        for (Verification verification : verifications) {
            verification.updateState(VerificationState.SUCCESS); // 인증 상태를 SUCCESS로 변경
        }
    }
}
