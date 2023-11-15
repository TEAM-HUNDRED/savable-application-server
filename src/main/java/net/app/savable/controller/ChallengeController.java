package net.app.savable.controller;

import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import net.app.savable.domain.challenge.dto.ChallengeDetailDto;
import net.app.savable.domain.challenge.dto.ChallengeResponseDto;
import net.app.savable.domain.challenge.dto.ChallengeGuideDto;
import net.app.savable.domain.challenge.dto.HomeChallengeDto;
import net.app.savable.domain.challenge.dto.ParticipationRequestDto;
import net.app.savable.global.config.auth.LoginMember;
import net.app.savable.global.config.auth.dto.SessionMember;
import net.app.savable.global.error.ApiResponse;
import net.app.savable.global.error.exception.ErrorCode;
import net.app.savable.service.ChallengeService;
import org.joda.time.DateTime;
import org.springframework.web.bind.annotation.*;

import java.util.Enumeration;
import java.util.List;

@RequiredArgsConstructor
@Slf4j
@RestController
@RequestMapping("/challenges")
public class ChallengeController {
    private final ChallengeService challengeService;

    @GetMapping()
    public ApiResponse<List<HomeChallengeDto>> participatableChallengeList(@LoginMember SessionMember sessionMember,
                                                                           HttpServletRequest request) {

        System.out.printf("\n\n////////챌린지 API 호출////////\n");
        System.out.printf("호출 시간: %s\n", new DateTime().toString("yyyy-MM-dd HH:mm:ss"));
        System.out.printf("*** request headers ***\n");
        printRequestInfo(request);

        List<HomeChallengeDto> challengeList = challengeService.findChallengeByDate();  // 챌린지가 많아지면 오늘 날짜 기준 참여하고 있는 챌린지는 제외하고 보여줘야함.
        return ApiResponse.success(challengeList);
    }

    @GetMapping("/{challengeId}")
    public ApiResponse<ChallengeDetailDto> getChallengeDetail(@PathVariable Long challengeId, @LoginMember SessionMember sessionMember){
        ChallengeResponseDto challengeResponseDto = challengeService.findChallengeDetailById(challengeId);
        Boolean isParticipatable = challengeService.checkParticipatable(challengeId,sessionMember.getId());
        List<ChallengeGuideDto> challengeGuideDtoList= challengeService.findChallengeGuide(challengeId);

        ChallengeDetailDto challengeDetailDto = ChallengeDetailDto.builder()
                .challenge(challengeResponseDto)
                .isParticipatable(isParticipatable)
                .verificationGuide(challengeGuideDtoList)
                .build();
        return ApiResponse.success(challengeDetailDto);
    }

    @PostMapping("/participations")
    public ApiResponse<String> participationAdd(@RequestBody ParticipationRequestDto participationRequestDto, @LoginMember SessionMember sessionMember){
        log.info("participationRequestDto={}",participationRequestDto);

        ApiResponse<String> INVALID_INPUT_VALUE = validateVerificationGoal(participationRequestDto);
        if (INVALID_INPUT_VALUE != null) return INVALID_INPUT_VALUE;

        challengeService.addParticipation(participationRequestDto,sessionMember.getId());
        return ApiResponse.success("챌린지 신청이 완료되었습니다.");
    }

    private static ApiResponse<String> validateVerificationGoal(ParticipationRequestDto participationRequestDto) {
        Long duration= participationRequestDto.getDuration();
        Long verificationGoal = participationRequestDto.getVerificationGoal();
        Long week = duration/7;
        if (verificationGoal < week)
            return ApiResponse.fail(ErrorCode.INVALID_INPUT_VALUE, "목표 인증 횟수가 너무 낮습니다");
        if (verificationGoal > duration)
            return ApiResponse.fail(ErrorCode.INVALID_INPUT_VALUE, "목표 인증 횟수가 너무 높습니다");
        return null;
    }

    public void printRequestInfo(HttpServletRequest request) {
        // 요청 헤더 출력
        Enumeration<String> headerNames = request.getHeaderNames();
        StringBuilder headers = new StringBuilder();
        while (headerNames.hasMoreElements()) {
            String headerName = headerNames.nextElement();
            String headerValue = request.getHeader(headerName);
            headers.append(headerName).append(": ").append(headerValue).append("\n");
        }

        // 요청 URL 출력
        String requestURL = request.getRequestURL().toString();

        System.out.printf("[URL]\nURL: %s\n\n[Request Headers]\n%s", requestURL, headers.toString());
        System.out.printf("*********************\n\n");
    }
}
