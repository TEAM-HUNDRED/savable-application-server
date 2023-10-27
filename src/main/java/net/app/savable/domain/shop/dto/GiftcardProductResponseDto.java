package net.app.savable.domain.shop.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class GiftcardProductResponseDto {
    private Long id;
    private String image;
    private String brandName;
    private String productName;
    private Long price;

    public GiftcardProductResponseDto(Long id, String image, String brandName, String productName, Long price) {
        this.id = id;
        this.image = image;
        this.brandName = brandName;
        this.productName = productName;
        this.price = price;
    }
}
