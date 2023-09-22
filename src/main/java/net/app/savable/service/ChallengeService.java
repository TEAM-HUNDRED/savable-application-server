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
import org.springframework.cglib.core.Local;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class ChallengeService {
    private final ChallengeRepository challengeRepository;
    private final ChallengeVerificationGuideRepository guideRepository;
    private final MemberRepository memberRepository;
    private final ParticipationChallengeRepository participationChallengeRepository;
    private final VerificationRepository verificationRepository;

    public List<HomeChallengeDto> findChallengeByDate(){
        List<HomeChallengeDto> challengeList = challengeRepository.findChallengeByDate().stream()
                .map(HomeChallengeDto::new)
                .toList();
        return challengeList;
    }
    public ChallengeDto findChallengeDtoById(Long challengeId){
        Challenge challengeDetail = challengeRepository.findChallengeById(challengeId);
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
        return challengeRepository.findChallengeById(challengeId);
    }

    public void addParticipation(ParticipationRequestDto participationRequestDto){

        participationChallengeRepository.save(ParticipationChallenge.builder()
            .startDate(LocalDate.now())
            .endDate(LocalDate.now().plusDays(participationRequestDto.getDuration()))
            .verificationGoal(participationRequestDto.getVerificationGoal())
            .participationState(ParticipationState.IN_PROGRESS)
            .savings(challengeRepository.findChallengeById(participationRequestDto.getChallengeId()).getEstimatedSavings())
            .createdAt(LocalDateTime.now())
            .lastModifiedAt(LocalDateTime.now())
            .challenge(challengeRepository.findChallengeById(participationRequestDto.getChallengeId()))
            .member(memberRepository.findMemberById(participationRequestDto.getMemberId()))
            .verificationList(List.of()) // 빈 리스트
            .build());
    }
}
