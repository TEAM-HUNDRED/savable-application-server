package net.app.savable.controller;

import jakarta.servlet.http.HttpServletRequest;
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

import java.util.Enumeration;
import java.util.HashMap;
import java.util.Map;

@RestController
@RequiredArgsConstructor
@RequestMapping("/login")
public class KakaoSocialLoginController {

    private final CustomerOAuth2MemberService customerOAuth2MemberService;
    private final MemberService memberService;

    @PostMapping("/kakao")
    public ApiResponse<Map<String, Object>> kakaoLogin(@RequestBody HashMap<String, Object> data,
                                                       HttpSession httpSession,
                                                       HttpServletRequest request) {

        httpSession.invalidate(); // 기존 세션을 무효화
        HttpSession newSession = request.getSession(true); // 새 세션 강제 생성

        String socialId = customerOAuth2MemberService.processKakaoLogin(data);

        Member member = memberService.findBySocialId(socialId);
        newSession.setAttribute("member", new SessionMember(member)); // 세션에 사용자 정보를 저장하기 위한 Dto 클래스

        Map<String, Object> result = new HashMap<>();
        if (member.getUsername() == null) { // 회원가입이 안되어있는 경우
            result.put("isRegistered", false);
        } else { // 회원가입이 되어있는 경우
            result.put("isRegistered", true);
        }

        return ApiResponse.success(result);
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
