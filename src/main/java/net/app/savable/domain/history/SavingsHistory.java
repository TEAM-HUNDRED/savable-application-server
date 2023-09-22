package net.app.savable.domain.history;

import jakarta.persistence.*;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import net.app.savable.domain.member.BaseTimeEntity;
import net.app.savable.domain.member.Member;

@Entity
@Getter
@NoArgsConstructor
public class SavingsHistory extends BaseTimeEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private Long savings;

    @Column(nullable = false)
    private Long totalSavings; // 누적 적립금

    @Column(nullable = false)
    private String description;

    @ManyToOne(fetch = FetchType.LAZY) // SavingsHistory N : 1 Member (지연로딩)
    @JoinColumn(name = "member_id")
    private Member member;

    @Builder
    public SavingsHistory(Long savings, Long totalSavings, String description, Member member) {
        this.savings = savings;
        this.totalSavings = totalSavings;
        this.description = description;
        this.member = member;
    }
}
