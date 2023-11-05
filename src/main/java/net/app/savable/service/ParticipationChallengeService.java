package net.app.savable.service;

import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import net.app.savable.domain.challenge.ParticipationChallenge;
import net.app.savable.domain.challenge.ParticipationChallengeRepository;
import net.app.savable.domain.challenge.ParticipationState;
import net.app.savable.domain.challenge.dto.MyParticipationChallengeDetailDto;
import net.app.savable.domain.challenge.dto.MyParticipationChallengeDto;
import net.app.savable.domain.member.dto.ChallengeInfoResponseDto;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class ParticipationChallengeService {

    private final ParticipationChallengeRepository participationChallengeRepository;

    public List<MyParticipationChallengeDto> findParticipationChallengeByMemberId(Long memberId) {
        return participationChallengeRepository.findMyParticipationChallengeByMemberId(memberId);
    }

    public MyParticipationChallengeDetailDto findParticipationChallengeDetailByParticipationChallengeId(Long participationChallengeId, Long memberId) {
        findParticipationChallengeById(participationChallengeId); // 참여 챌린지가 존재하는지 확인
        return participationChallengeRepository.findMyParticipationChallengeDetailByParticipationChallengeId(participationChallengeId, memberId);
    }

    public ParticipationChallenge findParticipationChallengeById(Long participationChallengeId) {
        return participationChallengeRepository.findById(participationChallengeId).orElseThrow(() -> {
            log.error("Invalid participationChallenge ID: {}", participationChallengeId);
            return new EntityNotFoundException("Invalid participationChallenge ID: " + participationChallengeId);
        });
    }

    public ChallengeInfoResponseDto findChallengeSummary(Long memberId){
        log.info("ParticipationChallengeService.findChallengeSummary() 실행");
        Long currentParticipationCount = participationChallengeRepository.countByMember_IdAndParticipationState(memberId, ParticipationState.IN_PROGRESS);
        Long failedParticipationCount = participationChallengeRepository.countByMember_IdAndParticipationState(memberId, ParticipationState.FAIL);
        Long successParticipationCount = participationChallengeRepository.countByMember_IdAndParticipationState(memberId, ParticipationState.SUCCESS);

        return ChallengeInfoResponseDto.builder()
                .successChallengeCount(successParticipationCount)
                .completeChallengeCount(successParticipationCount + failedParticipationCount)
                .currentParticipationCount(currentParticipationCount)
                .build();
    }

    public Long findScheduledReward(Long memberId){
        log.info("ParticipationChallengeService.findScheduledReward() 실행");
        return participationChallengeRepository.findScheduledReward(memberId);
    }
}
