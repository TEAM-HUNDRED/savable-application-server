package net.app.savable.domain.history.dto;

import lombok.Builder;
import net.app.savable.domain.history.RewardHistory;
import net.app.savable.domain.history.RewardType;
import net.app.savable.domain.member.Member;

public class RewardHistorySaveDto {
    private Long id;
    private Long reward;
    private Long totalReward; // 누적 리워드
    private RewardType rewardType; // 리워드 타입(챌린지, 기프티콘)
    private String description;
    private Member member;

    @Builder
    public RewardHistorySaveDto(Long id, Long reward, Long totalReward, RewardType rewardType, String description, Member member) {
        this.id = id;
        this.reward = reward;
        this.totalReward = totalReward;
        this.rewardType = rewardType;
        this.description = description;
        this.member = member;
    }

    public RewardHistory toEntity() {
        return RewardHistory.builder()
                .id(id)
                .reward(reward)
                .totalReward(totalReward)
                .rewardType(rewardType)
                .description(description)
                .member(member)
                .build();
    }
}
