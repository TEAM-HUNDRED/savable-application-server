package net.app.savable.domain.challenge;

import jakarta.persistence.*;
import lombok.Builder;
import lombok.Getter;
import net.app.savable.domain.member.BaseTimeEntity;
import lombok.NoArgsConstructor;
import net.app.savable.domain.member.Member;
import org.hibernate.annotations.ColumnDefault;

@Entity
@Getter
@NoArgsConstructor //TODO : 왜 넣어야 되는지 모르겠음
public class Verification extends BaseTimeEntity{
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

    @ManyToOne(fetch = FetchType.LAZY) // Verification N : 1 Member (지연로딩)
    @JoinColumn(name = "member_id")
    private Member member;

    private String aiResult;

    @Builder
    public Verification(String image, VerificationState state, ParticipationChallenge participationChallenge, Member member, String aiResult) {
        this.image = image;
        this.state = state;
        this.participationChallenge = participationChallenge;
        this.member = member;
        this.aiResult=aiResult;
    }

    public void updateState(VerificationState state) { // 인증 상태 변경
        this.state = state;
    }

    public Long getParticipationChallengeId() { // 참여 챌린지 id 반환
        return this.participationChallenge.getId();
    }
}
