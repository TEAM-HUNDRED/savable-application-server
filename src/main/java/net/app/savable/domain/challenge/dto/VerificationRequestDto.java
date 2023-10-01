package net.app.savable.domain.challenge.dto;

import lombok.Builder;
import lombok.Getter;
import net.app.savable.domain.challenge.ParticipationChallenge;
import net.app.savable.domain.challenge.Verification;
import net.app.savable.domain.challenge.VerificationState;
import net.app.savable.domain.member.Member;

@Getter
public class VerificationRequestDto {
    private String image;
    private VerificationState state;
    private ParticipationChallenge participationChallenge;
    private Member member;

    @Builder
    public VerificationRequestDto(String image, VerificationState state, ParticipationChallenge participationChallenge, Member member) {
        this.image = image;
        this.state = state;
        this.participationChallenge = participationChallenge;
        this.member = member;
    }

    public Verification toEntity() {
        return Verification.builder()
            .image(image)
            .state(state)
            .participationChallenge(participationChallenge)
            .member(member)
            .build();
    }
}
