package net.app.savable.service;

import lombok.RequiredArgsConstructor;
import net.app.savable.domain.shop.GiftcardProduct;
import net.app.savable.domain.shop.GiftcardProductRepository;
import net.app.savable.domain.shop.dto.GiftcardDto;
import net.app.savable.global.common.ErrorCode;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ShopService {
    private final GiftcardProductRepository giftcardProductRepository;

    public List<GiftcardDto> findGiftcardByInOnSale(Boolean inOnSale, Long price){
        Long minPrice;
        Long maxPrice;
        if (price == 0) {
            minPrice = price;
            maxPrice = 5000L;
        }
        else {
            minPrice = price;
            maxPrice = price + 999;
        }

        List<GiftcardDto> giftcardList = giftcardProductRepository.findGiftcardByInOnSaleAndPriceBetweenOrderByBrandNameAsc(inOnSale,minPrice,maxPrice);
        return giftcardList;
    }

}
