package net.app.savable.domain.challenge.dto;


import lombok.Getter;
import net.app.savable.domain.challenge.Challenge;

import java.time.LocalDate;

@Getter
public class ChallengeResponseDto {
    private Long id;
    private String title;
    private String image;
    private String explanation;
    private String verificationDescription;
    private Long reward;
    private Boolean hasDeadline;
    private LocalDate startDate;
    private LocalDate endDate;

    public ChallengeResponseDto(Challenge challenge) {
        this.id = challenge.getId();
        this.title = challenge.getTitle();
        this.image = challenge.getImage();
        this.explanation = challenge.getExplanation();
        this.verificationDescription = challenge.getVerificationDescription();
        this.reward = challenge.getReward();
        this.hasDeadline = challenge.getHasDeadline();
        this.startDate = challenge.getStartDate();
        this.endDate = challenge.getEndDate();
    }
}
