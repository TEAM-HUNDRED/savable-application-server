package net.app.savable.domain.challenge.dto;

import lombok.Getter;

import java.time.LocalDate;

@Getter
public class ParticipationChallengeInfoDto {
    private Long challengeId;
    private String image;
    private String title;
    private LocalDate startDate;
    private LocalDate endDate;
    private Long verificationGoal;
    private Long scheduledReward;
    private Long savings;

    public ParticipationChallengeInfoDto(Long challengeId, String image, String title, LocalDate startDate, LocalDate endDate, Long verificationGoal, Long scheduledReward, Long savings) {
        this.challengeId = challengeId;
        this.image = image;
        this.title = title;
        this.startDate = startDate;
        this.endDate = endDate;
        this.verificationGoal = verificationGoal;
        this.scheduledReward = scheduledReward;
        this.savings = savings;
    }
}
