package net.app.savable.domain.challenge;

import jakarta.persistence.*;
import lombok.Getter;
import net.app.savable.domain.member.BaseTimeEntity;

@Entity
@Getter
public class ChallengeVerificationGuide extends BaseTimeEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private Boolean isPass; // 패스 여부

    @Column(nullable = false)
    private String image; // 인증 사진 예시

    @Column(nullable = false)
    private String explanation; // 인증 방식 설명

    @ManyToOne(fetch = FetchType.LAZY) // ChallengeVerificationGuide N : 1 Challenge (지연로딩)
    @JoinColumn(name = "challenge_id")
    private Challenge challenge;
}
