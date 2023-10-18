package net.app.savable.controller;

import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import net.app.savable.domain.member.Member;
import net.app.savable.global.config.auth.CustomerOAuth2MemberService;
import net.app.savable.global.config.auth.dto.SessionMember;
import net.app.savable.global.error.ApiResponse;
import net.app.savable.service.MemberService;
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
    private final MemberService memberService;

    @PostMapping("/kakao")
    public ApiResponse<HashMap<String, Object>> kakaoLogin(@RequestBody HashMap<String, Object> data,
                                          HttpSession httpSession) {

        Member member = customerOAuth2MemberService.processKakaoLogin(data);

        HashMap<String, Object> result = new HashMap<>();
        httpSession.setAttribute("member", new SessionMember(member)); // 세션에 사용자 정보를 저장하기 위한 Dto 클래스
        if (member.getUsername() == null && member.getPhoneNumber() == null) {
            result.put("isRegistered", false);
            return ApiResponse.success(result);
        } else {
            result.put("isRegistered", true);
            return ApiResponse.success(result);
        }
    }
}
