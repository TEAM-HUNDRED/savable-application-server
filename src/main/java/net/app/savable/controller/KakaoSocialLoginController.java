package net.app.savable.controller;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import net.app.savable.domain.member.Member;
import net.app.savable.global.config.auth.CustomerOAuth2MemberService;
import net.app.savable.global.config.auth.dto.SessionMember;
import net.app.savable.global.error.ApiResponse;
import net.app.savable.global.wrapper.CustomResponseWrapper;
import net.app.savable.service.MemberService;
import org.joda.time.DateTime;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;
import java.util.*;

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

        System.out.printf("\n\n////////로그인 API 호출////////\n");
        System.out.printf("호출 시간: %s\n", new DateTime().toString("yyyy-MM-dd HH:mm:ss"));
        System.out.printf("*** request headers ***\n");
        printRequestInfo(request);

        System.out.printf("*** 로그인 전 기존 세션 ***\n");
        printSession(httpSession);

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

        System.out.printf("\n*** 로그인 후 새롭게 발급된 세션 ***\n");
        printSession(newSession);
        System.out.printf("\n");

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


    public void printSession(HttpSession httpSession) {
        System.out.printf("[세션 인코딩 전] session id: %s\n", httpSession.getId());

        String decodedSession = httpSession.getId();
        byte[] encodedBytes = decodedSession.getBytes();
        String encodedSession = Base64.getEncoder().encodeToString(encodedBytes);
        System.out.printf("[세션 인코딩 후] session id: %s\n", encodedSession);
        System.out.printf("*********************\n");
    }

    public void printResponseInfo(CustomResponseWrapper wrappedResponse) {
        // 응답 헤더 출력
        Collection<String> headerNames = wrappedResponse.getHeaderNames();
        StringBuilder responseHeaders = new StringBuilder();
        for (String headerName : headerNames) {
            String headerValue = wrappedResponse.getHeader(headerName);
            responseHeaders.append(headerName).append(": ").append(headerValue).append("\n");
        }

        // 캡쳐된 응답 본문 출력
        String responseBody = wrappedResponse.getCaptureAsString();

        System.out.printf("[응답 정보 출력]\n헤더:\n%s\n본문:\n%s\n", responseHeaders.toString(), responseBody);
    }

}
