package net.app.savable.global.config.auth;

import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import net.app.savable.domain.member.Member;
import net.app.savable.domain.member.MemberRepository;
import net.app.savable.global.config.auth.dto.OAuthAttributes;
import net.app.savable.global.config.auth.dto.SessionMember;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserService;
import org.springframework.security.oauth2.core.user.DefaultOAuth2User;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;

import java.util.Collections;

@Slf4j
@RequiredArgsConstructor
@Service
public class CustomerOAuth2MemberService implements OAuth2UserService<OAuth2UserRequest, OAuth2User>{
    private final MemberRepository memberRepository;
    private final HttpSession httpSession;

    @Override
    public OAuth2User loadUser(OAuth2UserRequest userRequest) {
        log.info("userRequest : {}", userRequest);
        OAuth2UserService<OAuth2UserRequest, OAuth2User> delegate = new DefaultOAuth2UserService();
        OAuth2User oAuth2User = delegate.loadUser(userRequest);

        String registrationId = userRequest.getClientRegistration().getRegistrationId();
        String userNameAttributeName = userRequest.getClientRegistration().
                getProviderDetails().getUserInfoEndpoint().
                getUserNameAttributeName();

        OAuthAttributes attributes = OAuthAttributes.of(registrationId, userNameAttributeName, oAuth2User.getAttributes());

        Member member = saveMember(attributes);
        httpSession.setAttribute("member", new SessionMember(member)); // 세션에 사용자 정보를 저장하기 위한 Dto 클래스

        return new DefaultOAuth2User(
                Collections.singleton(new
                        SimpleGrantedAuthority(member.getRoleKey())),
                attributes.getAttributes(),
                attributes.getNameAttributeKey());
    }

    private Member saveMember(OAuthAttributes attributes) {
        return memberRepository.findByEmail(attributes.getEmail())
                .orElseGet(() -> memberRepository.save(attributes.toEntity())); // 없는 사용자라면 insert
    }
}
