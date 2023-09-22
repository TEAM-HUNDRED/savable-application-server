package net.app.savable.domain.challenge.dto.request;

import lombok.Getter;

@Getter
public class ParticipationRequestDto {
    private Long memberId;
    private Long challengeId;
    private Long duration;
    private Long verificationGoal;

}
