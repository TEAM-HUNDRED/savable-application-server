package net.app.savable.domain.challenge;

import jakarta.persistence.*;
import lombok.Getter;
import net.app.savable.domain.member.BaseTimeEntity;
import net.app.savable.domain.member.Member;
import org.hibernate.annotations.ColumnDefault;

import java.time.LocalDate;
import java.util.List;

@Entity
@Getter
public class ParticipationChallenge extends BaseTimeEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private LocalDate startDate; // 챌린지 참여 시작일

    @Column(nullable = false)
    private LocalDate endDate; // 챌린지 참여 마감일

    @Column(nullable = false)
    private Long verificationGoal; // 인증 목표 횟수

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    @ColumnDefault("'IN_PROGRESS'")
    private ParticipationState participationState; // 챌린지 성공 여부

    @Column(nullable = false)
    private Long savings; // 챌린지 인증당 절약 금액

    @ManyToOne(fetch = FetchType.LAZY) // ParticipationChallenge N : 1 Challenge (지연로딩)
    @JoinColumn(name = "challenge_id")
    private Challenge challenge;

    @ManyToOne(fetch = FetchType.LAZY) // ParticipationChallenge N : 1 Member (지연로딩)
    @JoinColumn(name = "member_id")
    private Member member;

    @OneToMany(mappedBy = "participationChallenge", cascade = CascadeType.ALL) // ParticipationChallenge 1 : N Verification
    private List<Verification> verificationList;

    public void updateState(ParticipationState state) { // 챌린지 성공 여부 변경
        this.participationState = ParticipationState.SUCCESS;
    }

    public Long getMemberId() { // 회원 id 반환
        return this.member.getId();
    }

    public Long getChallengeId() { // 챌린지 id 반환
        return this.challenge.getId();
    }
}
