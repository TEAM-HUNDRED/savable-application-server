package net.app.savable.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import net.app.savable.domain.challenge.Challenge;
import net.app.savable.domain.challenge.dto.ParticipatableChallengeDto;
import net.app.savable.service.ChallengeService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;

@RequiredArgsConstructor
@Slf4j
@RestController
@RequestMapping("/challenges")
public class ChallengeController {

    private final ChallengeService challengeService;

    @GetMapping()
    public List<ParticipatableChallengeDto> participatableChallengeList(){
        List<ParticipatableChallengeDto> challengeWithoutDeadline=challengeService.findByHasDeadlineFalse();
        List<ParticipatableChallengeDto> challengeWithDeadline=challengeService.findChallengeByDate();
        List<ParticipatableChallengeDto> participatableChallengeList= new ArrayList<>(challengeWithoutDeadline);
        participatableChallengeList.addAll(new ArrayList<>(challengeWithDeadline));
        return participatableChallengeList;
    }
}
