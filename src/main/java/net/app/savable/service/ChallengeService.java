    package net.app.savable.service;

import lombok.RequiredArgsConstructor;
import net.app.savable.domain.challenge.Challenge;
import net.app.savable.domain.challenge.ChallengeRepository;
import net.app.savable.domain.challenge.ChallengeVerificationGuideRepository;
import net.app.savable.domain.challenge.dto.ChallengeDto;
import net.app.savable.domain.challenge.dto.ChallengeGuideDto;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ChallengeService {
    private final ChallengeVerificationGuideRepository guideRepository;
    private final ChallengeRepository challengeRepository;

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
