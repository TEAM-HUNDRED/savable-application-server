package net.app.savable.domain.history.dto;

import lombok.Builder;
import lombok.Getter;

@Getter
public class SavingsHistoryResponseDto {
    private Long savings;
    private Long totalSavings; // 누적 적립금
    private String description;

    @Builder
    public SavingsHistoryResponseDto(Long savings, Long totalSavings, String description) {
        this.savings = savings;
        this.totalSavings = totalSavings;
        this.description = description;
    }
}
