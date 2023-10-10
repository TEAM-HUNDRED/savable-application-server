package net.app.savable.domain.member.dto;

import lombok.Getter;

@Getter
public class MemberSignUpRequestDto {
    String imageUrl;
    String username;
    String phoneNumber;
}
