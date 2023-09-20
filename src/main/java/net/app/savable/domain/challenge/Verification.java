package net.app.savable.domain.challenge;

import jakarta.persistence.*;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.ColumnDefault;

@Entity
@Getter
@NoArgsConstructor
public class Verification {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String image;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    @ColumnDefault("'WAITING'")
    private VerificationState state;

    @ManyToOne(fetch = FetchType.LAZY) // Verification N : 1 ParticipationChallenge (지연로딩)
    @JoinColumn(name = "participation_challenge_id")
    private ParticipationChallenge participationChallenge;

    @Builder
    public Verification(String image, VerificationState state, ParticipationChallenge participationChallenge) {
        this.image = image;
        this.state = state;
        this.participationChallenge = participationChallenge;
    }

    public void updateState(VerificationState state) { // 인증 상태 변경
        this.state = state;
    }

    public Long getParticipationChallengeId() { // 참여 챌린지 id 반환
        return this.participationChallenge.getId();
    }
}
