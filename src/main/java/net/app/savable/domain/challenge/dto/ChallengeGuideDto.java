package net.app.savable.domain.challenge.dto;

import lombok.Getter;
import net.app.savable.domain.challenge.ChallengeVerificationGuide;

@Getter
public class ChallengeGuideDto {
    private Boolean isPass;
    private String image;
    private String explanation;

    public ChallengeGuideDto(ChallengeVerificationGuide challengeVerificationGuide) {
        this.isPass = challengeVerificationGuide.getIsPass();
        this.image = challengeVerificationGuide.getImage();
        this.explanation = challengeVerificationGuide.getExplanation();
    }
}
