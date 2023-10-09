package net.app.savable.domain.challenge.dto;

import lombok.Builder;
import lombok.Getter;

import java.util.List;

@Builder
@Getter
public class ChallengeDetailDto {
    private ChallengeResponseDto challenge;
    private Boolean isParticipatable;
    private List<ChallengeGuideDto> verificationGuide;

    public ChallengeDetailDto(ChallengeResponseDto challenge, Boolean isParticipatable, List<ChallengeGuideDto> verificationGuide) {
        this.challenge = challenge;
        this.isParticipatable = isParticipatable;
        this.verificationGuide = verificationGuide;
    }
}
