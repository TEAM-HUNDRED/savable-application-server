package net.app.savable.domain.shop.dto;

import lombok.Builder;
import net.app.savable.domain.member.Member;
import net.app.savable.domain.shop.GiftcardOrder;
import net.app.savable.domain.shop.GiftcardProduct;
import net.app.savable.domain.shop.SendState;

public class GiftcardOrderSaveDto {

    private String positivePoint;
    private String negativePoint;
    private Long quantity;
    private SendState sendState;
    private Member member;
    private GiftcardProduct giftcardProduct;

    @Builder
    public GiftcardOrderSaveDto(String positivePoint, String negativePoint, Long quantity, SendState sendState, Member member, GiftcardProduct giftcardProduct) {
        this.positivePoint = positivePoint;
        this.negativePoint = negativePoint;
        this.quantity = quantity;
        this.sendState = sendState;
        this.member = member;
        this.giftcardProduct = giftcardProduct;
    }

    public GiftcardOrder toEntity(){
        return GiftcardOrder.builder()
                .positivePoint(positivePoint)
                .negativePoint(negativePoint)
                .quantity(quantity)
                .sendState(sendState)
                .member(member)
                .giftcardProduct(giftcardProduct)
                .build();

    }
}
