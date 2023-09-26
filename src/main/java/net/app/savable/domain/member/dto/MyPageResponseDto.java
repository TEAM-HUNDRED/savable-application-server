package net.app.savable.domain.member.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;

@Getter
@NoArgsConstructor
public class MyPageResponseDto {

    private MemberSummaryResponseDto memberSummaryResponseDto;
    private ChallengeSummaryResponseDto challengeSummaryResponseDto;

    @Builder
    public MyPageResponseDto(MemberSummaryResponseDto memberSummaryResponseDto, ChallengeSummaryResponseDto challengeSummaryResponseDto) {
        this.memberSummaryResponseDto = memberSummaryResponseDto;
        this.challengeSummaryResponseDto = challengeSummaryResponseDto;
    }
}
