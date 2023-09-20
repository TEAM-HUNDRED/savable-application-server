package net.app.savable.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import net.app.savable.domain.challenge.VerificationRepository;
import net.app.savable.domain.challenge.dto.VerificationDetailDto;
import net.app.savable.domain.challenge.dto.VerificationRequestDto;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class VerificationService {

    private final VerificationRepository verificationRepository;

    @Transactional(readOnly = false)
    public void addVerification(VerificationRequestDto verificationRequestDto) {
        log.info("addVerification: {}", verificationRequestDto);
        verificationRepository.save(verificationRequestDto.toEntity());
    }

    public VerificationDetailDto findVerificationDetail(Long participationId){
        return verificationRepository.findVerificationDetailByParticipationId(participationId);
    }
}
