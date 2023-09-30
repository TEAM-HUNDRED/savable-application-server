package net.app.savable.domain.challenge.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public class MyParticipationChallengeDetailDto {
    private final ParticipationChallengeInfoDto participationChallengeInfoDto;
    private final VerificationInfoDto verificationInfoDto;
}
