package net.app.savable.domain.challenge.dto;

import lombok.Builder;
import lombok.Getter;

@Getter
public class VerificationDetailDto {
    private final String image;
    private final String title;
    private final Integer percentage;
    private final Long currentCount;
    private final Long goalsCount;
    private final Long scheduledReward; // 지급 예정 보상
    private final Long reward; // 1회 인증 시 보상
    private final Long savings; // 절약 금액

    @Builder
    public VerificationDetailDto(String image, String title, Integer percentage, Long currentCount, Long goalsCount, Long scheduledReward, Long reward, Long savings) {
        this.image = image;
        this.title = title;
        this.percentage = percentage;
        this.currentCount = currentCount;
        this.goalsCount = goalsCount;
        this.scheduledReward = scheduledReward;
        this.reward = reward;
        this.savings = savings;
    }
}
