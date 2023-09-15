package net.app.savable.domain.challenge.dto;

import lombok.Getter;

@Getter
public class MyParticipationChallengeDto {
    private Long participationChallengeId;
    private String image;
    private String title;
    private Long remainingVerification;
    private Long percentage;
    private Long scheduledReward;
    private Long savings;

    public MyParticipationChallengeDto(Long participationChallengeId, String image, String title, Long remainingVerification, Long percentage, Long scheduledReward, Long savings) {
        this.participationChallengeId = participationChallengeId;
        this.image = image;
        this.title = title;
        this.remainingVerification = remainingVerification;
        this.percentage = percentage;
        this.scheduledReward = scheduledReward;
        this.savings = savings;
    }
}
