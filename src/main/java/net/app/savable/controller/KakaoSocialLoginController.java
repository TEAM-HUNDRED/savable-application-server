package net.app.savable.controller;

import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import net.app.savable.domain.member.Member;
import net.app.savable.domain.member.MemberRepository;
import net.app.savable.global.config.auth.CustomerOAuth2MemberService;
import net.app.savable.global.config.auth.dto.SessionMember;
import net.app.savable.global.error.ApiResponse;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;

@RestController
@RequiredArgsConstructor
@RequestMapping("/login")
public class KakaoSocialLoginController {

    private final CustomerOAuth2MemberService customerOAuth2MemberService;
    private final MemberRepository memberRepository;

    @PostMapping("/kakao")
    public ApiResponse<String> kakaoLogin(@RequestBody HashMap<String, Object> data,
                                          HttpSession httpSession) {
        String socialId = customerOAuth2MemberService.processKakaoLogin(data);

        Member member = memberRepository.findBySocialId(socialId).orElse(null);
        httpSession.setAttribute("member", new SessionMember(member)); // 세션에 사용자 정보를 저장하기 위한 Dto 클래스

        return ApiResponse.success("로그인이 완료되었습니다.");
    }
}
