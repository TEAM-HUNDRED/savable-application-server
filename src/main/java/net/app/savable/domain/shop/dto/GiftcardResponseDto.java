package net.app.savable.domain.shop.dto;

import lombok.Getter;

@Getter
public class GiftcardResponseDto {
    private Long id;
    private String image;
    private String brandName;
    private String productName;
    private Long price;

    public GiftcardResponseDto(Long id, String image, String brandName, String productName, Long price) {
        this.id = id;
        this.image = image;
        this.brandName = brandName;
        this.productName = productName;
        this.price = price;
    }
}
