package net.app.savable.controller;

import lombok.RequiredArgsConstructor;
import net.app.savable.domain.shop.dto.GiftcardOrderRequestDto;
import net.app.savable.domain.shop.dto.GiftcardOrderResponseDto;
import net.app.savable.domain.shop.dto.GiftcardProductResponseDto;
import net.app.savable.global.config.auth.LoginMember;
import net.app.savable.global.config.auth.dto.SessionMember;
import net.app.savable.global.error.ApiResponse;
import net.app.savable.global.error.exception.GeneralException;
import net.app.savable.service.ShopService;
import org.springframework.web.bind.annotation.*;

import java.util.List;

import static net.app.savable.global.error.exception.ErrorCode.INVALID_INPUT_VALUE;
import static net.app.savable.global.error.exception.ErrorCode.NOT_FOUND;

@RestController
@RequestMapping("/shop")
@RequiredArgsConstructor
public class ShopController {
    private final ShopService shopService;

    @GetMapping("/giftcards/{price}")
    public ApiResponse<List<GiftcardProductResponseDto>> giftcardByPrice(@PathVariable Long price, @LoginMember SessionMember sessionMember){
        if (price>5000)
            return ApiResponse.fail(INVALID_INPUT_VALUE, "최대 금액을 넘는 가격대 입니다.");

        List<GiftcardProductResponseDto> giftcardList = shopService.findGiftcardByInOnSale(true,price);

        return ApiResponse.success(giftcardList);

    }

    @PostMapping("/order")
    public ApiResponse<String> giftcardOrderAdd(@RequestBody GiftcardOrderRequestDto giftcardOrderRequest, @LoginMember SessionMember sessionMember){
        if (giftcardOrderRequest.getPositivePoint().isEmpty() || giftcardOrderRequest.getNegativePoint().isEmpty()){
            throw new GeneralException(INVALID_INPUT_VALUE, "EMPTY_ORDER_ANSWER");
        }
        shopService.addGiftcardOrder(giftcardOrderRequest, sessionMember.getId());
        return ApiResponse.success("기프티콘 구매가 완료되었습니다.");
    }

    @GetMapping("/histories")
    public ApiResponse<List<GiftcardOrderResponseDto>> getGiftcardHistoryList(@LoginMember SessionMember sessionMember){
        List<GiftcardOrderResponseDto> giftcardHistoryList = shopService.findGiftcardByMember(sessionMember.getId());
        return ApiResponse.success(giftcardHistoryList);
    }
}
