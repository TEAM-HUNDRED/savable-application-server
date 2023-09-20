package net.app.savable.domain.history;

import jakarta.persistence.*;
import lombok.Getter;
import net.app.savable.domain.member.BaseTimeEntity;
import net.app.savable.domain.member.Member;

import java.sql.Timestamp;

@Entity
@Getter
public class RewardHistory extends BaseTimeEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private Long reward;

    @Column(nullable = false)
    private Long totalReward; // 누적 리워드

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private RewardType rewardType; // 리워드 타입(챌린지, 기프티콘)

    @Column(nullable = false)
    private String description;

    @ManyToOne(fetch = FetchType.LAZY) // RewardHistory N : 1 Member (지연로딩)
    @JoinColumn(name = "member_id")
    private Member member;
}
