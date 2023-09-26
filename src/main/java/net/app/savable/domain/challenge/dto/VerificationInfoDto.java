package net.app.savable.domain.challenge.dto;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class VerificationInfoDto {
    private Long successCount;
    private Long failCount;
    private Long remainingCount;
    List<VerificationResponseDto> verificationResponseDtos;

    public VerificationInfoDto(Long successCount, Long failCount, Long remainingCount) {
        this.successCount = successCount;
        this.failCount = failCount;
        this.remainingCount = remainingCount;
    }
}
