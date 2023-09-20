package net.app.savable.domain.challenge.dto;

import lombok.Getter;
import net.app.savable.domain.challenge.VerificationState;

import java.sql.Timestamp;

@Getter
public class VerificationDto {
    private Long id;
    private String image;
    private VerificationState state;
    private Timestamp createdAt;

    public VerificationDto(Long id, String image, VerificationState state, Timestamp createdAt) {
        this.id = id;
        this.image = image;
        this.state = state;
        this.createdAt = createdAt;
    }
}
