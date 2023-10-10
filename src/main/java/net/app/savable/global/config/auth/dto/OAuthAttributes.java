package net.app.savable.global.config.auth.dto;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.Builder;
import lombok.Getter;
import net.app.savable.domain.member.AccountState;
import net.app.savable.domain.member.Member;
import net.app.savable.domain.member.Role;

import java.util.HashMap;
import java.util.Map;

@Getter
public class OAuthAttributes {
    private HashMap<String, Object> attributes; // OAuth2User의 getAttributes() 메소드의 반환값을 담을 클래스
    private String socialId;
    private String nameAttributeKey;
    private String name;
    private String picture;

    @Builder
    public OAuthAttributes(HashMap<String, Object> attributes, String nameAttributeKey,
                           String name, String picture, String socialId) {
        this.socialId = socialId;
        this.attributes = attributes;
        this.nameAttributeKey = nameAttributeKey; // OAuth2 로그인 진행 시 키가 되는 필드값. PK와 같은 의미
        this.name = name;
        this.picture = picture;
    }

    public static OAuthAttributes of(String registrationId, String userNameAttributeName,
                                     HashMap<String, Object> attributes) {
        if ("naver".equals(registrationId)) {
            return ofNaver(userNameAttributeName, attributes);
        }
        if ("kakao".equals(registrationId)) {
            return ofKakao(userNameAttributeName, attributes);
        }
        return ofGoogle(userNameAttributeName, attributes);
    }

    private static OAuthAttributes ofGoogle(String userNameAttributeName, HashMap<String, Object> attributes) {
        return OAuthAttributes.builder()
                .name((String) attributes.get("name"))
                .picture((String) attributes.get("picture"))
                .attributes(attributes) // OAuth2User의 getAttributes() 메소드의 반환값을 담을 클래스
                .nameAttributeKey(userNameAttributeName) // OAuth2 로그인 진행 시 키가 되는 필드값. PK와 같은 의미
                .build();
    }

    @SuppressWarnings("unchecked")
    private static OAuthAttributes ofNaver(String userNameAttributeName, HashMap<String, Object> attributes) {
        Map<String, Object> response = (Map<String, Object>) attributes.get("response");

        return OAuthAttributes.builder()
                .name((String) response.get("name"))
                .picture((String) response.get("profile_image"))
                .attributes(attributes)
                .nameAttributeKey(userNameAttributeName)
                .build();
    }

    private static OAuthAttributes ofKakao(String userNameAttributeName, HashMap<String, Object> attributes) {

        return OAuthAttributes.builder()
                .socialId((String) attributes.get(userNameAttributeName))
                .name((String) attributes.get("nickname"))
                .picture((String) attributes.get("profile_image_url"))
                .attributes(attributes)
                .nameAttributeKey(userNameAttributeName)
                .build();
    }

    public Member toEntity() {
        ObjectMapper objectMapper = new ObjectMapper();
        String defaultImage = "https://chatbot-budket.s3.ap-northeast-2.amazonaws.com/profile/default-profile.png";
        String jsonAttributes;
        try {
            jsonAttributes = objectMapper.writeValueAsString(attributes);
        } catch (Exception e) {
            jsonAttributes = null;
        }
        return Member.builder()
                .socialId(socialId)
                .profileImage(defaultImage)
                .role(Role.USER)
                .accountState(AccountState.ACTIVE)
                .socialData(jsonAttributes) // JSON 형태로 저장
                .savings(0L)
                .reward(0L)
                .build();
    }
}
