package net.app.savable.domain.member;

import jakarta.persistence.*;
import lombok.Getter;
import net.app.savable.domain.challenge.ParticipationChallenge;
import net.app.savable.domain.history.RewardHistory;
import net.app.savable.domain.history.SavingsHistory;
import net.app.savable.domain.shop.GiftcardOrder;
import org.hibernate.annotations.ColumnDefault;

import java.util.List;

@Entity
@Getter
public class Member extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(unique = true)
    private String username;

    @Column(unique = true)
    private String email;

    @Column(nullable = false)
    @ColumnDefault("0")
    private Long reward;

    @Column(nullable = false)
    @ColumnDefault("0")
    private Long savings;
    private String phoneNumber;

    @Column(nullable = false)
    private String profileImage;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private Role role;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    @ColumnDefault("'ACTIVE'")
    private AccountState accountState;

    @OneToMany(mappedBy = "member", cascade = CascadeType.ALL) // Member 1 : N ParticipationChallenge
    private List<ParticipationChallenge> participationChallengeList;

    @OneToMany(mappedBy = "member", cascade = CascadeType.ALL) // Member 1 : N GiftcardOrder
    private List<GiftcardOrder> giftcardOrderList;

    @OneToMany(mappedBy = "member", cascade = CascadeType.ALL) // Member 1 : N RewardHistory
    private List<RewardHistory> rewardHistoryList;

    @OneToMany(mappedBy = "member", cascade = CascadeType.ALL) // Member 1 : N SavingHistory
    private List<SavingsHistory> savingHistoryList;

    public void updateSavings(Long savings) { // 회원의 절약 금액을 증가시킴
        this.savings += savings;
    }

    public void updateReward(Long reward) { // 회원의 리워드를 증가시킴
        this.reward += reward;
    }
}
