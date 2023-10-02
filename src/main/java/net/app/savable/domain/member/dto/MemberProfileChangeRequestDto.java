package net.app.savable.domain.member.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class MemberProfileChangeRequestDto {
    private String username;
    private String profileImage;
    private String phoneNumber;

    @Builder
    public MemberProfileChangeRequestDto(String username, String profileImage, String phoneNumber) {
        this.username = username;
        this.profileImage = profileImage;
        this.phoneNumber = phoneNumber;
    }
}
