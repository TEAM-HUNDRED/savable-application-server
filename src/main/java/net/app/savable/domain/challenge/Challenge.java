package net.app.savable.domain.challenge;

import jakarta.persistence.*;
import lombok.Getter;

import java.time.LocalDate;
import java.util.List;

@Entity
@Getter
public class Challenge {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String title;

    @Column(nullable = false)
    private String image; // 챌린지 썸네일 이미지

    @Column(nullable = false)
    private String explanation; // 챌린지 설명

    @Column(nullable = false)
    private String verificationDescription; // 인증방법 설명

    @Column(nullable = false)
    private Long estimatedSavings; // 1회 인증당 절약추정금액

    @Column(nullable = false)
    private Long reward; // 1회 인증당 리워드

    @Column(nullable = false)
    private Boolean hasDeadline; // 마감 여부 (false: 상시모집, true: 기간모집)

    private LocalDate startDate; // 챌린지 모집 시작일
    private LocalDate endDate; // 챌린지 모잡 마감일

    @OneToMany(mappedBy = "challenge", cascade = CascadeType.ALL) // Challenge 1 : N ChallengeVerificationGuide
    private List<ChallengeVerificationGuide> challengeVerificationGuideList;

    @OneToMany(mappedBy = "challenge") // Challenge 1 : N ParticipationChallenge
    private List<ParticipationChallenge> participationChallengeList;
}
