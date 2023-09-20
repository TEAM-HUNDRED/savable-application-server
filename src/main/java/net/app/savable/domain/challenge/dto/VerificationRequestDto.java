package net.app.savable.domain.challenge.dto;

import lombok.Builder;
import lombok.Getter;
import net.app.savable.domain.challenge.ParticipationChallenge;
import net.app.savable.domain.challenge.Verification;
import net.app.savable.domain.challenge.VerificationState;

import java.sql.Timestamp;

@Getter
public class VerificationRequestDto {
    private Timestamp dateTime;
    private String image;
    private VerificationState state;
    private ParticipationChallenge participationChallenge;

    @Builder
    public VerificationRequestDto(Timestamp dateTime, String image, VerificationState state, ParticipationChallenge participationChallenge) {
        this.dateTime = dateTime;
        this.image = image;
        this.state = state;
        this.participationChallenge = participationChallenge;
    }

    public Verification toEntity() {
        return Verification.builder()
            .dateTime(dateTime)
            .image(image)
            .state(state)
            .participationChallenge(participationChallenge)
            .build();
    }
}
