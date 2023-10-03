package net.app.savable.domain.challenge.dto.request;

import lombok.Getter;

@Getter
public class ParticipationRequestDto {
    private Long challengeId;
    private Long duration; // 1주 : 7, 2주: 14
    private Long verificationGoal;

}
