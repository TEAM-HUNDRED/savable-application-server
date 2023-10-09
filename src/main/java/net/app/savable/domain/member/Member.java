package net.app.savable.domain.member;

import jakarta.persistence.*;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import net.app.savable.domain.challenge.ParticipationChallenge;
import net.app.savable.domain.history.RewardHistory;
import net.app.savable.domain.history.SavingsHistory;
import net.app.savable.domain.shop.GiftcardOrder;
import org.hibernate.annotations.ColumnDefault;
import org.hibernate.annotations.Type;

import java.time.LocalDateTime;
import java.util.List;

@Entity
@Getter
@NoArgsConstructor
public class Member extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true)
    private String socialId;

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

    private String profileImage;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private Role role;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    @ColumnDefault("'ACTIVE'")
    private AccountState accountState;

    private LocalDateTime deletedAt;

    @Column(columnDefinition = "TEXT")
    private String socialData;


    @OneToMany(mappedBy = "member", cascade = CascadeType.ALL) // Member 1 : N ParticipationChallenge
    private List<ParticipationChallenge> participationChallengeList;

    @OneToMany(mappedBy = "member", cascade = CascadeType.ALL) // Member 1 : N GiftcardOrder
    private List<GiftcardOrder> giftcardOrderList;

    @OneToMany(mappedBy = "member", cascade = CascadeType.ALL) // Member 1 : N RewardHistory
    private List<RewardHistory> rewardHistoryList;

    @OneToMany(mappedBy = "member", cascade = CascadeType.ALL) // Member 1 : N SavingHistory
    private List<SavingsHistory> savingHistoryList;

    @Builder
    public Member(String socialId, String username, String email, Long reward, Long savings, String phoneNumber, String profileImage, Role role, AccountState accountState, String socialData) {
        this.socialId = socialId;
        this.username = username;
        this.email = email;
        this.reward = reward;
        this.savings = savings;
        this.phoneNumber = phoneNumber;
        this.profileImage = profileImage;
        this.role = role;
        this.accountState = accountState;
        this.socialData = socialData;
    }

    public void updateSavings(Long savings) { // 회원의 절약 금액을 증가시킴
        this.savings += savings;
    }

    public void updateReward(Long reward) { // 회원의 리워드를 증가시킴
        this.reward += reward;
    }

    public Member update(String username, String profileImage, String phoneNumber){
        this.username = username;
        this.profileImage = profileImage;
        this.phoneNumber = phoneNumber;

        return this;
    }

    public Member delete(){
        this.email = null;
        this.accountState = AccountState.DELETED;
        this.deletedAt = LocalDateTime.now();

        return this;
    }

    public String getRoleKey(){
        return this.role.getKey();
    }
}
