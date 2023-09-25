package net.app.savable.controller;

import lombok.RequiredArgsConstructor;
import net.app.savable.domain.shop.dto.GiftcardDto;
import net.app.savable.global.common.ApiResponse;
import net.app.savable.global.common.ErrorCode;
import net.app.savable.service.ShopService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/shop")
@RequiredArgsConstructor
public class ShopController {
    private final ShopService shopService;

    @GetMapping("/giftcards/{price}")
    public ApiResponse<List<GiftcardDto>> giftcardByPrice(@PathVariable Long price){
        if (price>5000)
            return ApiResponse.fail(ErrorCode.INVALID_INPUT_VALUE, "최대 금액을 넘는 가격대 입니다.");

        List<GiftcardDto> giftcardList = shopService.findGiftcardByInOnSale(true,price);
        if (giftcardList.size() == 0)
            return ApiResponse.fail(ErrorCode.NOT_FOUND, "해당 가격대에 맞는 상품이 없습니다. : "+price+"원 대");
        return ApiResponse.success(giftcardList);

    }

}
