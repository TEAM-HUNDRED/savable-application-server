package net.app.savable.domain.shop.dto;

import lombok.Getter;
import net.app.savable.domain.member.Member;
import net.app.savable.domain.shop.GiftcardOrder;
import net.app.savable.domain.shop.GiftcardProduct;
import net.app.savable.domain.shop.SendState;

@Getter
public class GiftcardOrderRequestDto {
    private Long giftcardId;
    private Long quantity;
    private String positivePoint;
    private String negativePoint;
}
