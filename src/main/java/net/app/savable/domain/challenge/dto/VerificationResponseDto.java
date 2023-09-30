package net.app.savable.domain.challenge.dto;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import net.app.savable.domain.challenge.VerificationState;

import java.sql.Timestamp;
import java.time.LocalDateTime;

@Getter
@RequiredArgsConstructor
public class VerificationResponseDto {
    private final Long id;
    private final String image;
    private final VerificationState state;
    private final LocalDateTime createAt;
}
