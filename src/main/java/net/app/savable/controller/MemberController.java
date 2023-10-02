package net.app.savable.controller;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import lombok.extern.java.Log;
import lombok.extern.slf4j.Slf4j;
import net.app.savable.domain.member.Member;
import net.app.savable.domain.member.dto.ChallengeSummaryResponseDto;
import net.app.savable.domain.member.dto.MemberProfileChangeRequestDto;
import net.app.savable.domain.member.dto.MemberSummaryResponseDto;
import net.app.savable.domain.member.dto.MyPageResponseDto;
import net.app.savable.global.config.auth.LoginMember;
import net.app.savable.global.config.auth.dto.SessionMember;
import net.app.savable.global.error.ApiResponse;
import net.app.savable.service.MemberService;
import net.app.savable.service.ParticipationChallengeService;
import net.app.savable.service.VerificationService;
import org.springframework.web.bind.annotation.*;

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

    @PatchMapping("/member/deletion")
    public ApiResponse<String> memberRemove(@LoginMember SessionMember sessionMember, HttpServletRequest request) {
        log.info("MemberController.deleteMember() 실행");
        memberService.deleteMember(sessionMember.getId());

        HttpSession session = request.getSession();
        if (session != null) {
            session.invalidate(); // 세션 무효화
        }

        return ApiResponse.success("회원 탈퇴가 완료되었습니다.");
    }

    @PatchMapping("/member/settings") // 유저 정보 수정
    public ApiResponse<String> memberProfileUpdate(@LoginMember SessionMember sessionMember, @RequestBody MemberProfileChangeRequestDto memberProfileChangeRequestDto) {
        log.info("MemberController.memberProfileUpdate() 실행");


        return ApiResponse.success("회원 정보 수정이 완료되었습니다.");
    }
}
