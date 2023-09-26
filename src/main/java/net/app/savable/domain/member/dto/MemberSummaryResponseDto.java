package net.app.savable.domain.member.dto;

import lombok.Builder;
import lombok.Getter;

@Getter
public class MemberSummaryResponseDto {

    private String username;
    private Long totalSavings;
    private Long totalReward;
    private Long scheduledReward;
    private Long verificationCount;

    @Builder
    public MemberSummaryResponseDto(String username, Long totalSavings, Long totalReward, Long scheduledReward, Long verificationCount) {
        this.username = username;
        this.totalSavings = totalSavings;
        this.totalReward = totalReward;
        this.scheduledReward = scheduledReward;
        this.verificationCount = verificationCount;
    }
}
