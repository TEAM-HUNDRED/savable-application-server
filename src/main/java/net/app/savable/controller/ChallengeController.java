package net.app.savable.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import net.app.savable.domain.challenge.dto.ChallengeDetailDto;
import net.app.savable.domain.challenge.dto.ChallengeGuideDto;
import net.app.savable.service.ChallengeService;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RequiredArgsConstructor
@Slf4j
@RestController
@RequestMapping("/challenges")
public class ChallengeControler {
    private final ChallengeService challengeService;

    @GetMapping("/{challengeId}")
    public ChallengeDetailDto getChallengeDetail(@PathVariable Integer challengeId){
        return ChallengeDetailDto.builder().challenge(challengeService.findChallengeById(challengeId))
                .challengeGuideDto(challengeService.findChallengeGuide(challengeId))
                .build();
    }

}
