package net.app.savable.domain.challenge.dto;

import lombok.Getter;
import net.app.savable.domain.challenge.VerificationState;

@Getter
public class VerificationAdminUpdateDto {
    private Long verificationId;
    private VerificationState verificationState;
}
