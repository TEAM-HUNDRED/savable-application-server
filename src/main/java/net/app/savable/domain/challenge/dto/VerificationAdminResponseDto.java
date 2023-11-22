package net.app.savable.domain.challenge.dto;

import lombok.Builder;
import lombok.Getter;
import net.app.savable.domain.challenge.Verification;
import java.time.format.DateTimeFormatter;

@Getter
public class VerificationAdminResponseDto {
    private Long verificationId;
    private Long memberId;
    private String username;
    private String verificationDateTime;
    private String image;
    private String aiState;

    @Builder
    public VerificationAdminResponseDto(Verification verfication) {
        this.verificationId = verfication.getId();
        this.memberId = verfication.getMember().getId();
        this.username = verfication.getMember().getUsername();

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy년 MM월 dd일 H시 m분");
        this.verificationDateTime = verfication.getCreatedAt().format(formatter);
        this.image = verfication.getImage();
        this.aiState = verfication.getAiState().toString();
    }
}
