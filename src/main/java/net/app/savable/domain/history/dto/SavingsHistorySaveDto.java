package net.app.savable.domain.history.dto;

import lombok.Builder;
import net.app.savable.domain.history.SavingsHistory;
import net.app.savable.domain.member.Member;

public class SavingsHistorySaveDto {
    private Long savings;
    private Long totalSavings; // 누적 적립금
    private String description;
    private Member member;

    @Builder
    public SavingsHistorySaveDto(Long savings, Long totalSavings, String description, Member member) {
        this.savings = savings;
        this.totalSavings = totalSavings;
        this.description = description;
        this.member = member;
    }

    public SavingsHistory toEntity() {
        return SavingsHistory.builder()
                .savings(savings)
                .totalSavings(totalSavings)
                .description(description)
                .member(member)
                .build();
    }
}
