package net.app.savable.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import net.app.savable.domain.challenge.dto.ChallengeDetailDto;
import net.app.savable.global.common.ApiResponse;
import net.app.savable.service.ChallengeService;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RequiredArgsConstructor
@Slf4j
@RestController
@RequestMapping("/challenges")
public class ChallengeController {
    private final ChallengeService challengeService;

    @GetMapping("/{challengeId}")
    public ApiResponse<ChallengeDetailDto> getChallengeDetail(@PathVariable Integer challengeId){
        ChallengeDetailDto challengeDetailDto = ChallengeDetailDto.builder().challenge(challengeService.findChallengeById(challengeId))
                .verificationGuide(challengeService.findChallengeGuide(challengeId))
                .build();
        return ApiResponse.success(challengeDetailDto);
    }
}
