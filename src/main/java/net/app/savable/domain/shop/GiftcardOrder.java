package net.app.savable.domain.shop;

import jakarta.persistence.*;
import lombok.Getter;
import net.app.savable.domain.member.BaseTimeEntity;
import net.app.savable.domain.member.Member;
import org.hibernate.annotations.ColumnDefault;

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
