package net.app.savable.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import net.app.savable.domain.member.Member;
import net.app.savable.domain.member.dto.ChallengeSummaryResponseDto;
import net.app.savable.domain.member.dto.MemberSummaryResponseDto;
import net.app.savable.domain.member.dto.MyPageResponseDto;
import net.app.savable.global.config.auth.LoginMember;
import net.app.savable.global.config.auth.dto.SessionMember;
import net.app.savable.global.error.ApiResponse;
import net.app.savable.service.MemberService;
import net.app.savable.service.ParticipationChallengeService;
import net.app.savable.service.VerificationService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RequiredArgsConstructor
@RestController
@RequestMapping
public class MemberController {

    private final MemberService memberService;
    private final VerificationService verificationService;
    private final ParticipationChallengeService participationChallengeService;

    @GetMapping("/my-page")
    public ApiResponse<MyPageResponseDto> myPageDetails(@LoginMember SessionMember sessionMember) {

        log.info("MemberController.myPageDetails() 실행");
        Long memberId = sessionMember.getId();
        Member member = memberService.findById(memberId);
        Long verificationCount = verificationService.findTotalVerificationCount(memberId); // 지금까지 총 인증 횟수
        Long scheduledReward = participationChallengeService.findScheduledReward(memberId); // 예정된 리워드
        MemberSummaryResponseDto memberSummary = MemberSummaryResponseDto.builder()
                .username(member.getUsername())
                .totalReward(member.getReward())
                .totalSavings(member.getSavings())
                .scheduledReward(scheduledReward)
                .verificationCount(verificationCount)
                .build();

        ChallengeSummaryResponseDto challengeSummary = participationChallengeService.findChallengeSummary(member.getId());

        MyPageResponseDto myPageResponseDto = MyPageResponseDto.builder()
                .memberSummaryResponseDto(memberSummary)
                .challengeSummaryResponseDto(challengeSummary)
                .build();

        return ApiResponse.success(myPageResponseDto);
    }
}
