package net.app.savable.domain.challenge.dto;

import lombok.Getter;
import net.app.savable.domain.challenge.VerificationState;
import org.springframework.cglib.core.Local;

import java.sql.Timestamp;
import java.time.LocalDateTime;

@Getter
public class VerificationDto {
    private Long id;
    private String image;
    private VerificationState state;
    private LocalDateTime createdAt;

    public VerificationDto(Long id, String image, VerificationState state, LocalDateTime createdAt) {
        this.id = id;
        this.image = image;
        this.state = state;
        this.createdAt = createdAt;
    }
}
