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
    private Boolean isVerifiedToday;

    public MyParticipationChallengeDto(Long participationChallengeId, String image, String title, Long remainingVerification, Long percentage, Long scheduledReward, Long savings, Boolean isVerifiedToday) {
        this.participationChallengeId = participationChallengeId;
        this.image = image;
        this.title = title;
        this.remainingVerification = remainingVerification;
        this.percentage = percentage;
        this.scheduledReward = scheduledReward;
        this.savings = savings;
        this.isVerifiedToday = isVerifiedToday;
    }
}
