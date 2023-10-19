package net.app.savable.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import net.app.savable.domain.challenge.VerificationRepository;
import net.app.savable.domain.challenge.VerificationState;
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
    private final ParticipationChallengeService participationChallengeService;

    @Transactional(readOnly = false)
    public void addVerification(VerificationRequestDto verificationRequestDto) {
        log.info("addVerification: {}", verificationRequestDto);
        verificationRepository.save(verificationRequestDto.toEntity());
    }

    public VerificationDetailDto findVerificationDetail(Long participationId){
        log.info("VerificationService.findVerificationDetail() 실행");
        participationChallengeService.findParticipationChallengeById(participationId); // 참여 챌린지가 존재하는지 확인
        return verificationRepository.findVerificationDetailByParticipationId(participationId);
    }

    public Long findTotalVerificationCount(Long memberId){
        log.info("VerificationService.findTotalVerificationCount() 실행");
        return verificationRepository.countByMember_IdAndState(memberId, VerificationState.SUCCESS);
    }
}
