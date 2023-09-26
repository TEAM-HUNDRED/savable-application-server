package net.app.savable.domain.shop.dto;

import lombok.Builder;
import lombok.Getter;
import net.app.savable.domain.shop.GiftcardOrder;
import net.app.savable.domain.shop.SendState;

import java.time.LocalDateTime;

@Getter
public class GiftcardHistoryDto {
    private LocalDateTime date;
    private String image;
    private String productName;
    private Long productPrice;
    private Long totalPrice;
    private Long quantity;
    private String brandName;
    private SendState sendState;

    public GiftcardHistoryDto(GiftcardOrder giftcardOrder){
        // WAITING과 READY를 사용자에게는 WAITING으로 보여주어 2개의 상태만 존재하게 함.
        SendState sendState = giftcardOrder.getSendState();
        if (sendState != SendState.COMPLETE){
            sendState=SendState.WAITING;
        }

        this.date = giftcardOrder.getCreatedAt();
        this.image= giftcardOrder.getGiftcardProduct().getImage();
        this.productName=giftcardOrder.getGiftcardProduct().getProductName();
        this.productPrice = giftcardOrder.getGiftcardProduct().getPrice();
        this.quantity = giftcardOrder.getQuantity();
        this.totalPrice= this.quantity * this.productPrice;
        this.brandName=giftcardOrder.getGiftcardProduct().getBrandName();
        this.sendState = sendState;
    }
}
