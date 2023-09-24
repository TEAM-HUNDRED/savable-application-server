package net.app.savable.domain.challenge;

import jakarta.persistence.*;
import lombok.Builder;
import lombok.Getter;
import net.app.savable.domain.member.BaseTimeEntity;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.ColumnDefault;

@Entity
@Getter
@NoArgsConstructor //TODO : 왜 넣어야 되는지 모르겠음
public class Verification extends BaseTimeEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String image;

    @Builder
    public Verification(Long id, String image, VerificationState state, ParticipationChallenge participationChallenge) {
        this.id = id;
        this.image = image;
        this.state = state;
        this.participationChallenge = participationChallenge;
    }

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
