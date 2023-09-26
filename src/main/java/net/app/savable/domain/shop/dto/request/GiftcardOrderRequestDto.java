package net.app.savable.domain.shop.dto.request;

import lombok.Getter;
import net.app.savable.domain.member.Member;
import net.app.savable.domain.shop.GiftcardOrder;
import net.app.savable.domain.shop.GiftcardProduct;
import net.app.savable.domain.shop.SendState;

@Getter
public class GiftcardOrderRequestDto {
    private Long memberId;
    private Long giftcardId;
    private Long quantity;
    private String positivePoint;
    private String negativePoint;

    public GiftcardOrder toEntity(Member member, GiftcardProduct giftcardProduct, SendState sendState){
        return GiftcardOrder.builder()
                .member(member)
                .giftcardProduct(giftcardProduct)
                .quantity(quantity)
                .positivePoint(positivePoint)
                .negativePoint(negativePoint)
                .sendState(sendState)
                .build();
    }
}
