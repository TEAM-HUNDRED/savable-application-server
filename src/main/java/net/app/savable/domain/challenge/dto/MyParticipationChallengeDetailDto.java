package net.app.savable.domain.challenge.dto;

import lombok.Getter;
import lombok.Setter;
import org.springframework.stereotype.Service;

@Getter
public class MyParticipationChallengeDetailDto {
    private ParticipationChallengeInfoDto participationChallengeInfoDto;
    private VerificationInfoDto verificationInfoDto;

    public MyParticipationChallengeDetailDto(ParticipationChallengeInfoDto participationChallengeInfoDto, VerificationInfoDto verificationInfoDto) {
        this.participationChallengeInfoDto = participationChallengeInfoDto;
        this.verificationInfoDto = verificationInfoDto;
    }
}
