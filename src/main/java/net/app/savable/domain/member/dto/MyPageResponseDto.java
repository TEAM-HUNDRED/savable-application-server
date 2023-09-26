package net.app.savable.domain.member.dto;

import lombok.Builder;
import lombok.RequiredArgsConstructor;

public class MyPageResponseDto {

    private final MemberSummaryResponseDto memberSummaryResponseDto;
    private final ChallengeSummaryResponseDto challengeSummaryResponseDto;

    @Builder
    public MyPageResponseDto(MemberSummaryResponseDto memberSummaryResponseDto, ChallengeSummaryResponseDto challengeSummaryResponseDto) {
        this.memberSummaryResponseDto = memberSummaryResponseDto;
        this.challengeSummaryResponseDto = challengeSummaryResponseDto;
    }
}
