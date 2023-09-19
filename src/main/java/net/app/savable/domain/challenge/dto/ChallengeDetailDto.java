package net.app.savable.domain.challenge.dto;

import lombok.Builder;
import lombok.Getter;
import net.app.savable.domain.challenge.Challenge;

import java.util.List;

@Builder
@Getter
public class ChallengeDetailDto {
    private ChallengeDto challenge;
    private List<ChallengeGuideDto> verificationGuide;

    public ChallengeDetailDto(ChallengeDto challenge, List<ChallengeGuideDto> challengeGuideDto) {
        this.challenge = challenge;
        this.verificationGuide = challengeGuideDto;
    }
}
