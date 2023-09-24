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
        List<HomeChallengeDto> challengeList = challengeRepository.findChallengeByDate().stream()
                .map(HomeChallengeDto::new)
                .toList();
        return challengeList;
    }
    public ChallengeDto findChallengeDetailById(Long challengeId){
        Challenge challengeDetail = challengeRepository.findChallengeById(challengeId).orElseThrow(
                () -> new IllegalArgumentException("Invalid Challenge Id: {}"+challengeId)
        );
        ChallengeDto challengeDto = new ChallengeDto(challengeDetail);
        return challengeDto;
    }

    public List<ChallengeGuideDto> findChallengeGuide(Long challengeId){
        List<ChallengeGuideDto> challengeGuideDtoList = guideRepository.findByChallengeIdOrderByIsPassDesc(challengeId).stream()
                .map(ChallengeGuideDto::new)
                .toList();
        return challengeGuideDtoList;
    }

    public Challenge findChallengeById(Long challengeId){
        return challengeRepository.findChallengeById(challengeId).orElseThrow(() ->
            new IllegalArgumentException("Invalid challengeId"+challengeId)
        );
    }

    public void addParticipation(ParticipationRequestDto participationRequestDto){
        Challenge requestedChallenge = challengeRepository.findChallengeById(participationRequestDto.getChallengeId())
                .orElseThrow(() -> new IllegalArgumentException("INVALID_CHALLENGE" + participationRequestDto.getChallengeId()));
        Member requestedMember = memberRepository.findMemberById(participationRequestDto.getMemberId())
                .orElseThrow(()-> new IllegalArgumentException("INVALID_MEMBER"+participationRequestDto.getMemberId()));
        LocalDate today = LocalDate.now();

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
