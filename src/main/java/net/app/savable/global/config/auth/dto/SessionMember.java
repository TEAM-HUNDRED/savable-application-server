package net.app.savable.global.config.auth.dto;

import lombok.Getter;
import net.app.savable.domain.member.Member;

import java.io.Serializable;

@Getter
public class SessionMember implements Serializable {
    private Long id;
    private String socialId;
    private String username;
    private String picture;

    public SessionMember(Member member) {
        this.id = member.getId();
        this.socialId = member.getSocialId();
        this.username = member.getUsername();
        this.picture = member.getProfileImage();
    }
}
