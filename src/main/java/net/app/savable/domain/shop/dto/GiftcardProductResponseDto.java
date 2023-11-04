package net.app.savable.domain.shop.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import net.app.savable.domain.shop.GiftcardProduct;

import java.io.Serializable;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class GiftcardProductResponseDto implements Serializable {
    private Long id;
    private String image;
    private String brandName;
    private String productName;
    private Long price;

    public static GiftcardProductResponseDto valueOf(GiftcardProduct giftcardProduct) {
        return GiftcardProductResponseDto.builder()
                .id(giftcardProduct.getId())
                .image(giftcardProduct.getImage())
                .brandName(giftcardProduct.getBrandName())
                .productName(giftcardProduct.getProductName())
                .price(giftcardProduct.getPrice())
                .build();
    }
}
