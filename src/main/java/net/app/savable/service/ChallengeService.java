package net.app.savable.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import net.app.savable.domain.challenge.ChallengeRepository;
import net.app.savable.domain.challenge.dto.ParticipatableChallengeDto;
import org.springframework.stereotype.Service;

import java.util.List;


@Service
@RequiredArgsConstructor
@Slf4j
public class ChallengeService {
    private final ChallengeRepository challengeRepository;

    public List<ParticipatableChallengeDto> findByHasDeadlineFalse(){
        //                ParticipatableChallengeDto participatableChallengeDto = new ParticipatableChallengeDto(challenge);
        List<ParticipatableChallengeDto> challengeWithoutDeadline= challengeRepository.findByHasDeadlineFalse().stream()
                .map(ParticipatableChallengeDto::new)
                .toList();
        return challengeWithoutDeadline;
    }

    public List<ParticipatableChallengeDto> findChallengeByDate(){
        List<ParticipatableChallengeDto> challengeWithDeadline = challengeRepository.findChallengeByDate().stream()
                .map(ParticipatableChallengeDto::new)
                .toList();
        return challengeWithDeadline;
    }
}
