package net.app.savable.domain.history.dto;

import lombok.Builder;
import lombok.Getter;

@Getter
public class RewardHistoryResponseDto {
    private Long reward;
    private Long totalReward; // 누적 리워드
    private String description;

    @Builder
    public RewardHistoryResponseDto(Long reward, Long totalReward, String description) {
        this.reward = reward;
        this.totalReward = totalReward;
        this.description = description;
    }
}
