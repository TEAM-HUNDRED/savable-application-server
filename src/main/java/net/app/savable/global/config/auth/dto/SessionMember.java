package net.app.savable.global.config.auth.dto;

import lombok.Getter;
import net.app.savable.domain.member.Member;

import java.io.Serializable;

@Getter
public class SessionMember implements Serializable {
    private Long id;
    private String name;
    private String email;
    private String picture;

    public SessionMember(Member member) {
        this.id = member.getId();
        this.name = member.getUsername();
        this.email = member.getEmail();
        this.picture = member.getProfileImage();
    }
}
