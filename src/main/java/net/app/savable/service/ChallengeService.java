package net.app.savable.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import net.app.savable.domain.challenge.ChallengeRepository;
import net.app.savable.domain.challenge.Challenge;
import net.app.savable.domain.challenge.ChallengeVerificationGuideRepository;
import net.app.savable.domain.challenge.dto.ChallengeDto;
import net.app.savable.domain.challenge.dto.ChallengeGuideDto;
import net.app.savable.domain.challenge.dto.HomeChallengeDto;

import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class ChallengeService {
    private final ChallengeRepository challengeRepository;
    private final ChallengeVerificationGuideRepository guideRepository;

    public List<HomeChallengeDto> findChallengeByDate(){
        List<HomeChallengeDto> challengeList = challengeRepository.findChallengeByDate().stream()
                .map(HomeChallengeDto::new)
                .toList();
        return challengeList;
    }
    public ChallengeDto findChallengeById(Integer challengeId){
        Challenge challengeDetail = challengeRepository.findById(challengeId);
        ChallengeDto challengeDto = new ChallengeDto(challengeDetail);
        return challengeDto;
    }

    public List<ChallengeGuideDto> findChallengeGuide(Integer challengeId){
        List<ChallengeGuideDto> challengeGuideDtoList = guideRepository.findByChallengeIdOrderByIsPassDesc(challengeId).stream()
                .map(ChallengeGuideDto::new)
                .toList();
        return challengeGuideDtoList;
    }

}
