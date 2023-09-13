package net.app.savable.domain.history;

import jakarta.persistence.*;
import lombok.Getter;

import java.sql.Timestamp;

@Entity
@Getter
public class RewardHistory {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private Long reward;

    @Column(nullable = false)
    private Long totalReward; // 누적 리워드

    @Column(nullable = false)
    private Timestamp date;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private RewardType rewardType; // 리워드 타입(챌린지, 기프티콘)

    @Column(nullable = false)
    private String description;
}
