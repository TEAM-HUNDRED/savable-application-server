package net.app.savable.global.config.auth;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import net.app.savable.domain.member.Member;
import net.app.savable.domain.member.MemberRepository;
import net.app.savable.global.config.auth.dto.OAuthAttributes;
import org.springframework.stereotype.Service;

import java.util.HashMap;

@Slf4j
@RequiredArgsConstructor
@Service
public class CustomerOAuth2MemberService {
    private final MemberRepository memberRepository;

    public String processKakaoLogin(HashMap<String, Object> data) {
        OAuthAttributes attributes = OAuthAttributes.of("kakao", "id", data);

        Member member = saveMember(attributes);
        return member.getSocialId();
    }

    private Member saveMember(OAuthAttributes attributes) {
        return memberRepository.findByEmail(attributes.getEmail())
                .orElseGet(() -> memberRepository.save(attributes.toEntity())); // 없는 사용자라면 insert
    }
}
