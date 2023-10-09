package net.app.savable.controller;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import net.app.savable.domain.member.AccountState;
import net.app.savable.domain.member.Member;
import net.app.savable.domain.member.MemberRepository;
import net.app.savable.domain.member.Role;
import net.app.savable.global.config.auth.CustomerOAuth2MemberService;
import net.app.savable.global.config.auth.dto.KakaoTokenDto;
import net.app.savable.global.config.auth.dto.SessionMember;
import net.app.savable.global.error.ApiResponse;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.util.MultiValueMap;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

import java.util.HashMap;

@RestController
@RequiredArgsConstructor
@RequestMapping("/login")
public class KakaoSocialLoginController {

    private final CustomerOAuth2MemberService customerOAuth2MemberService;
    private final MemberRepository memberRepository;

//    @PostMapping("/kakao")
//    public ResponseEntity<String> dummyLogin(@RequestBody KakaoTokenDto kakaoTokenDto, HttpSession session) {
//
//
//        System.out.printf("kakao login token: %s\n", kakaoTokenDto.getAccessToken());
//        Member member = kakaoLogin(kakaoTokenDto.getAccessToken());
//
//        session.setAttribute("member", new SessionMember(member));
//
//        return ResponseEntity.ok("Logged in as dummy user");
//    }

    @PostMapping("/kakao")
    public ApiResponse<Member> kakaoLogin(@RequestBody HashMap<String, Object> data, HttpSession httpSession) {
        String socialId = customerOAuth2MemberService.processKakaoLogin(data);
        System.out.printf("socialId: %s\n", socialId);

        Member member = memberRepository.findBySocialId(socialId).orElse(null);
        httpSession.setAttribute("member", new SessionMember(member)); // 세션에 사용자 정보를 저장하기 위한 Dto 클래스

        return ApiResponse.success(member);
    }

//    public Member kakaoLogin(String kakaoAccessToken) {
//        Member account = getKakaoInfo(kakaoAccessToken);
//
//        return account;
//    }

//    public Member getKakaoInfo(String kakaoAccessToken) {
//        RestTemplate rt = new RestTemplate();
//
//        HttpHeaders headers = new HttpHeaders();
//        headers.add("Authorization", "Bearer " + kakaoAccessToken);
//        headers.add("Content-type", "application/x-www-form-urlencoded;charset=utf-8");
//
//        HttpEntity<MultiValueMap<String, String>> accountInfoRequest = new HttpEntity<>(headers);
//
//        // POST 방식으로 API 서버에 요청 후 response 받아옴
//        ResponseEntity<String> accountInfoResponse = rt.exchange(
//                "https://kapi.kakao.com/v2/user/me", // "https://kapi.kakao.com/v2/user/me"
//                HttpMethod.POST,
//                accountInfoRequest,
//                String.class
//        );
//
//        // JSON Parsing (-> kakaoAccountDto)
//        ObjectMapper objectMapper = new ObjectMapper();
//        objectMapper.registerModule(new JavaTimeModule());
//        objectMapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
//
//        System.out.printf("accountInfoResponse.getBody(): %s\n", accountInfoResponse.getBody());
//        KakaoAccountResponse kakaoAccountResponse = null;
//        try {
//            kakaoAccountResponse = objectMapper.readValue(accountInfoResponse.getBody(), KakaoAccountResponse.class);
//        }
//        catch (Exception e) {
//            System.out.printf("Exception: %s\n", e.getMessage());
//        }
//
//        String defaultImage = "https://chatbot-budket.s3.ap-northeast-2.amazonaws.com/profile/default-profile.png";
//        Member member = Member.builder()
//                .profileImage(defaultImage)
//                .role(Role.USER)
//                .accountState(AccountState.ACTIVE)
//                .savings(0L)
//                .reward(0L)
//                .email(kakaoAccountResponse.getKakao_account().getEmail())
//                .username(null)
//                .build();
//
//        Member findMember = memberRepository.findByEmail(member.getEmail()).orElse(null);
//        if (findMember != null) {
//            System.out.printf("findMember: %s\n", findMember);
//            System.out.printf("email: %s\n", findMember.getEmail());
//            System.out.printf("name: %s\n", findMember.getUsername());
//            return findMember;
//        }
//        memberRepository.save(member);
//        return member;
//    }
}
