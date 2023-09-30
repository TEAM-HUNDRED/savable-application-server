package net.app.savable.domain.challenge.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;

@Getter
public class VerificationDetailDto {
    private String image;
    private final String title;
    private final Integer percentage;
    private final Long currentCount;
    private final Long goalCount;
    private final Long scheduledReward; // 지급 예정 보상
    private final Long reward; // 1회 인증 시 보상
    private final Long savings; // 절약 금액

    public VerificationDetailDto(String title, Integer percentage, Long currentCount, Long goalCount, Long scheduledReward, Long reward, Long savings) {
        this.title = title;
        this.percentage = percentage;
        this.currentCount = currentCount;
        this.goalCount = goalCount;
        this.scheduledReward = scheduledReward;
        this.reward = reward;
        this.savings = savings;
    }

    public void setImage(String image) {
        this.image = image;
    }
}
