package net.app.savable.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import net.app.savable.domain.challenge.*;
import net.app.savable.domain.challenge.dto.ChallengeDto;
import net.app.savable.domain.challenge.dto.ChallengeGuideDto;
import net.app.savable.domain.challenge.dto.HomeChallengeDto;

import net.app.savable.domain.challenge.dto.request.ParticipationRequestDto;
import net.app.savable.domain.member.Member;
import net.app.savable.domain.member.MemberRepository;
import net.app.savable.global.config.auth.dto.SessionMember;
import net.app.savable.global.error.exception.ErrorCode;
import net.app.savable.global.error.exception.GeneralException;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;

import static net.app.savable.domain.challenge.ParticipationState.*;

@Service
@RequiredArgsConstructor
@Slf4j
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
    public ChallengeDto findChallengeDetailById(Long challengeId){
        Challenge challengeDetail = challengeRepository.findChallengeById(challengeId)
                .orElseThrow(() -> new GeneralException(ErrorCode.NOT_FOUND, "INVALID_CHALLENGE : " + challengeId)
        );
        return new ChallengeDto(challengeDetail);
    }

    public List<ChallengeGuideDto> findChallengeGuide(Long challengeId){
        return guideRepository.findByChallengeIdOrderByIsPassDesc(challengeId).stream()
                .map(ChallengeGuideDto::new)
                .toList();
    }

    public Challenge findChallengeById(Long challengeId){
        return challengeRepository.findChallengeById(challengeId).orElseThrow(() ->
            new GeneralException(ErrorCode.NOT_FOUND, "INVALID_CHALLENGE : " + challengeId)
        );
    }

    public void addParticipation(ParticipationRequestDto participationRequestDto, SessionMember sessionMember){
        Challenge requestedChallenge = challengeRepository.findChallengeById(participationRequestDto.getChallengeId())
                .orElseThrow(() -> new GeneralException(ErrorCode.NOT_FOUND, "INVALID_CHALLENGE : " + participationRequestDto.getChallengeId()));
        Member requestedMember = memberRepository.findMemberById(sessionMember.getId())
                .orElseThrow(() -> new GeneralException(ErrorCode.NOT_FOUND, "INVALID_MEMBER : " + sessionMember.getId()));
        LocalDate today = LocalDate.now();

        // 이미 참여중인 챌린지인지 확인
        participationChallengeRepository.findParticipationStateByMemberAndChallenge(requestedMember, requestedChallenge)
                .ifPresent((challenge) -> {
                    throw new GeneralException(ErrorCode.BAD_REQUEST, "DUPLICATE_CHALLENGE_PARTICIPATION");
                });

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
}
