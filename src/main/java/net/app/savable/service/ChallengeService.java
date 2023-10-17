package net.app.savable.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import net.app.savable.domain.challenge.*;
import net.app.savable.domain.challenge.dto.ChallengeResponseDto;
import net.app.savable.domain.challenge.dto.ChallengeGuideDto;
import net.app.savable.domain.challenge.dto.HomeChallengeDto;

import net.app.savable.domain.challenge.dto.ParticipationRequestDto;
import net.app.savable.domain.member.Member;
import net.app.savable.domain.member.MemberRepository;
import net.app.savable.global.error.exception.ErrorCode;
import net.app.savable.global.error.exception.GeneralException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;

import static net.app.savable.domain.challenge.ParticipationState.*;

@Service
@RequiredArgsConstructor
@Slf4j
@Transactional(readOnly = true)
public class ChallengeService {
    private final ChallengeRepository challengeRepository;
    private final ChallengeVerificationGuideRepository guideRepository;
    private final MemberRepository memberRepository;
    private final ParticipationChallengeRepository participationChallengeRepository;

    public List<HomeChallengeDto> findChallengeByDate(){
        return challengeRepository.findChallengeByDate().stream()
                .map(HomeChallengeDto::new)
                .toList();
    }
    public ChallengeResponseDto findChallengeDetailById(Long challengeId){
        Challenge challengeDetail = challengeRepository.findChallengeById(challengeId)
                .orElseThrow(() -> new GeneralException(ErrorCode.NOT_FOUND, "INVALID_CHALLENGE : " + challengeId)
        );
        return new ChallengeResponseDto(challengeDetail);
    }

    public List<ChallengeGuideDto> findChallengeGuide(Long challengeId){
        return guideRepository.findByChallengeIdOrderByIsPassDesc(challengeId).stream()
                .map(ChallengeGuideDto::new)
                .toList();
    }

    @Transactional(readOnly = false)
    public void addParticipation(ParticipationRequestDto participationRequestDto, Long memberId){
        Challenge requestedChallenge = challengeRepository.findChallengeById(participationRequestDto.getChallengeId())
                .orElseThrow(() -> new GeneralException(ErrorCode.NOT_FOUND, "INVALID_CHALLENGE : " + participationRequestDto.getChallengeId()));
        Member requestedMember = memberRepository.findMemberById(memberId)
                .orElseThrow(() -> new GeneralException(ErrorCode.NOT_FOUND, "INVALID_MEMBER : " + memberId));
        LocalDate today = LocalDate.now();

        // 이미 참여중인 챌린지인지 확인
        List<ParticipationChallenge> duplicateChallenge = participationChallengeRepository.findParticipationChallengeByMember_idAndChallenge_idAndParticipationState(memberId, participationRequestDto.getChallengeId(),ParticipationState.IN_PROGRESS);
        if (duplicateChallenge.size()>0){
            throw new GeneralException(ErrorCode.BAD_REQUEST, "DUPLICATE_CHALLENGE_PARTICIPATION");
        }

        ParticipationChallenge participationChallenge = ParticipationChallenge.builder()
                .startDate(today)
                .endDate(today.plusDays(participationRequestDto.getDuration()))
                .verificationGoal(participationRequestDto.getVerificationGoal())
                .participationState(IN_PROGRESS)
                .savings(requestedChallenge.getEstimatedSavings())
                .challenge(requestedChallenge)
                .member(requestedMember)
                .verificationList(List.of()) // 빈 리스트
                .build();

        participationChallengeRepository.save(participationChallenge);
    }

    public Boolean checkParticipatable(Long challengeId, Long memberId){
        Boolean isParticipatable= true;

        List<ParticipationChallenge> duplicateChallenge = participationChallengeRepository.findParticipationChallengeByMember_idAndChallenge_idAndParticipationState(memberId, challengeId,ParticipationState.IN_PROGRESS);
        if (duplicateChallenge.size()>0){
            isParticipatable=false;
        }
        return isParticipatable;
    }
}
