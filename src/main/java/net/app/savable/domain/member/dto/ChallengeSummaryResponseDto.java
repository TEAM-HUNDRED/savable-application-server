package net.app.savable.domain.member.dto;

import lombok.Builder;
import lombok.Getter;

@Getter
public class ChallengeSummaryResponseDto {

    private Long successChallengeCount; // 성공한 챌린지 수
    private Long completeChallengeCount; // 완료한 챌린지 수(성공+실패)
    private Long currentParticipationCount; // 현재 참여중인 챌린지 수

    @Builder
    public ChallengeSummaryResponseDto(Long successChallengeCount, Long completeChallengeCount, Long currentParticipationCount) {
        this.successChallengeCount = successChallengeCount;
        this.completeChallengeCount = completeChallengeCount;
        this.currentParticipationCount = currentParticipationCount;
    }
}
