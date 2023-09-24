package net.app.savable.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import net.app.savable.domain.challenge.Challenge;
import net.app.savable.domain.challenge.dto.ChallengeDto;
import net.app.savable.domain.challenge.dto.ChallengeGuideDto;
import net.app.savable.domain.challenge.dto.HomeChallengeDto;
import net.app.savable.domain.challenge.dto.request.ParticipationRequestDto;
import net.app.savable.domain.member.Member;
import net.app.savable.global.common.ErrorCode;
import net.app.savable.service.ChallengeService;
import net.app.savable.service.MemberService;
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
    public ApiResponse<ChallengeDetailDto> getChallengeDetail(@PathVariable Long challengeId){
        ChallengeDto ChallengeResponseDto = challengeService.findChallengeDetailById(challengeId);
        List<ChallengeGuideDto> challengeGuide = challengeService.findChallengeGuide(challengeId);

        ChallengeDetailDto challengeDetailDto = ChallengeDetailDto.builder()
                .challenge(ChallengeResponseDto)
                .verificationGuide(challengeGuide)
                .build();
        return ApiResponse.success(challengeDetailDto);
    }

    @PostMapping("/participations")
    public ApiResponse<String> participationAdd(@RequestBody ParticipationRequestDto participationRequestDto){
        log.info("participationRequestDto={}",participationRequestDto);

        ApiResponse<String> INVALID_INPUT_VALUE = validateVerificationGoal(participationRequestDto);
        if (INVALID_INPUT_VALUE != null) return INVALID_INPUT_VALUE;


        challengeService.addParticipation(participationRequestDto);
        return ApiResponse.success("챌린지 신청이 완료되었습니다.");
    }

    private static ApiResponse<String> validateVerificationGoal(ParticipationRequestDto participationRequestDto) {
        Long duration= participationRequestDto.getDuration();
        Long verificationGoal = participationRequestDto.getVerificationGoal();
        Long week = duration/7;
        if (verificationGoal > week)
            return ApiResponse.fail(ErrorCode.INVALID_INPUT_VALUE, "목표 인증 횟수가 너무 낮습니다");
        if (verificationGoal > duration)
            return ApiResponse.fail(ErrorCode.INVALID_INPUT_VALUE, "목표 인증 횟수가 너무 높습니다");
        return null;
    }
}
