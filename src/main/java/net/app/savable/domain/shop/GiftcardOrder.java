package net.app.savable.domain.shop;

import jakarta.persistence.*;
import lombok.Getter;
import net.app.savable.domain.member.BaseTimeEntity;
import net.app.savable.domain.member.Member;
import org.hibernate.annotations.ColumnDefault;

import java.sql.Timestamp;

@Entity
@Getter
public class GiftcardOrder extends BaseTimeEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String positivePoint;

    @Column(nullable = false)
    private String negativePoint;

    @Column(nullable = false)
    private Long quantity;

<<<<<<< HEAD
=======
    private Timestamp date;

>>>>>>> fe7082b (feat: 챌린지 실패 시 인증 비율에 따라 지급하는 로직 구현)
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    @ColumnDefault("'WAITING'")
    private SendState sendState;

    @ManyToOne(fetch = FetchType.LAZY) // GiftcardOrder N : 1 GiftcardProduct (지연로딩)
    @JoinColumn(name = "giftcard_product_id")
    private GiftcardProduct giftcardProduct;

    @ManyToOne(fetch = FetchType.LAZY) // GiftcardOrder N : 1 Member (지연로딩)
    @JoinColumn(name = "member_id")
    private Member member;
}
