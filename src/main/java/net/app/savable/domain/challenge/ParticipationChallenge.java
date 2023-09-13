package net.app.savable.domain.challenge;

import jakarta.persistence.*;
import lombok.Getter;
import net.app.savable.domain.member.Member;

import java.time.LocalDate;

@Entity
@Getter
public class ParticipationChallenge {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private LocalDate startDate; // 챌린지 참여 시작일

    @Column(nullable = false)
    private LocalDate endDate; // 챌린지 참여 마감일

    @Column(nullable = false)
    private Long verificationGoal; // 인증 목표 횟수

    @Column(nullable = false)
    private Boolean isSuccess; // 챌린지 성공 여부

    @Column(nullable = false)
    private Long savings; // 챌린지 인증당 절약 금액

    @ManyToOne(fetch = FetchType.LAZY) // ParticipationChallenge N : 1 Challenge (지연로딩)
    @JoinColumn(name = "challenge_id")
    private Challenge challenge;

    @ManyToOne(fetch = FetchType.LAZY) // ParticipationChallenge N : 1 Member (지연로딩)
    @JoinColumn(name = "member_id")
    private Member member;
}
