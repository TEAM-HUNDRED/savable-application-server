package net.app.savable.domain.member.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;

@Getter
@NoArgsConstructor
public class MemberInfoResponseDto {

    private String username;
    private String profileImage;
    private String phoneNumber;
    private Long totalSavings;
    private Long totalReward;
    private Long scheduledReward;
    private Long verificationCount;
    private ChallengeInfoResponseDto challengeInfoResponseDto;

    @Builder
    public MemberInfoResponseDto(String username, String profileImage, String phoneNumber ,Long totalSavings, Long totalReward, Long scheduledReward, Long verificationCount, ChallengeInfoResponseDto challengeInfoResponseDto) {
        this.username = username;
        this.profileImage = profileImage;
        this.phoneNumber = phoneNumber;
        this.totalSavings = totalSavings;
        this.totalReward = totalReward;
        this.scheduledReward = scheduledReward;
        this.verificationCount = verificationCount;
        this.challengeInfoResponseDto = challengeInfoResponseDto;
    }
}
