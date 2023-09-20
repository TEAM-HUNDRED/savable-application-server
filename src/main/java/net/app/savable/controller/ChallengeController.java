package net.app.savable.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import net.app.savable.domain.challenge.dto.HomeChallengeDto;
import net.app.savable.service.ChallengeService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import net.app.savable.domain.challenge.dto.ChallengeDetailDto;
import net.app.savable.global.common.ApiResponse;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RequiredArgsConstructor
@Slf4j
@RestController
@RequestMapping("/challenges")
public class ChallengeController {
    private final ChallengeService challengeService;

    @GetMapping()
    public ApiResponse<List<HomeChallengeDto>> participatableChallengeList() {
        List<HomeChallengeDto> challengeList = challengeService.findChallengeByDate();
        return ApiResponse.success(challengeList);
    }

    @GetMapping("/{challengeId}")
    public ApiResponse<ChallengeDetailDto> getChallengeDetail(@PathVariable Integer challengeId){
        ChallengeDetailDto challengeDetailDto = ChallengeDetailDto.builder().challenge(challengeService.findChallengeById(challengeId))
                .verificationGuide(challengeService.findChallengeGuide(challengeId))
                .build();
        return ApiResponse.success(challengeDetailDto);
    }
}
