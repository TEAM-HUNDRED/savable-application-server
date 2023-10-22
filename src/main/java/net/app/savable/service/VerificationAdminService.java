package net.app.savable.service;

import com.amazonaws.services.ec2.model.Status;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import net.app.savable.domain.challenge.Verification;
import net.app.savable.domain.challenge.VerificationRepository;
import net.app.savable.domain.challenge.VerificationState;
import net.app.savable.domain.challenge.dto.VerificationAdminResponseDto;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
@Slf4j
@Transactional(readOnly = true)
public class VerificationAdminService {
    private final VerificationRepository verificationRepository;
    private final SchedulerService schedulerService;

    public List<VerificationAdminResponseDto> getVerifications(Long challengeId) {
        return verificationRepository.findByChallengeIdAndState(challengeId, VerificationState.WAITING).stream()
                .map(VerificationAdminResponseDto::new)
                .toList();
    }

    @Transactional(readOnly = false)
    public void updateVerification(Map<String, String> data) {
        for (Map.Entry<String, String> entry : data.entrySet()) {
            String statusValue = entry.getValue();
            if (statusValue.equals("WAITING")) continue; // 인증 상태가 WAITING이면 넘어감

            Long verificationId = Long.parseLong(entry.getKey());
            Verification verification = verificationRepository.findById(verificationId)
                    .orElseThrow(() -> new IllegalArgumentException("Invalid verification Id:" + verificationId));

            verification.updateState(VerificationState.valueOf(statusValue));
            if (statusValue.equals(VerificationState.SUCCESS.getKey())) {
                schedulerService.processVerification(verification);
            }
        }
    }
}
