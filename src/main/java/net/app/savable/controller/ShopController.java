package net.app.savable.controller;

import lombok.RequiredArgsConstructor;
import net.app.savable.domain.member.MemberRepository;
import net.app.savable.domain.shop.GiftcardProductRepository;
import net.app.savable.domain.shop.dto.GiftcardHistoryDto;
import net.app.savable.domain.shop.dto.GiftcardResponseDto;
import net.app.savable.domain.shop.dto.request.GiftcardHistoryRequestDto;
import net.app.savable.domain.shop.dto.request.GiftcardOrderRequestDto;
import net.app.savable.global.common.ApiResponse;
import net.app.savable.global.common.ErrorCode;
import net.app.savable.service.ShopService;
import org.springframework.web.bind.annotation.*;

import java.util.List;

import static net.app.savable.global.common.ErrorCode.INVALID_INPUT_VALUE;

@RestController
@RequestMapping("/shop")
@RequiredArgsConstructor
public class ShopController {
    private final ShopService shopService;
    private final MemberRepository memberRepository;
    private final GiftcardProductRepository giftcardProductRepository;

    @GetMapping("/giftcards/{price}")
    public ApiResponse<List<GiftcardResponseDto>> giftcardByPrice(@PathVariable Long price){
        if (price>5000)
            return ApiResponse.fail(INVALID_INPUT_VALUE, "최대 금액을 넘는 가격대 입니다.");

        List<GiftcardResponseDto> giftcardList = shopService.findGiftcardByInOnSale(true,price);
        if (giftcardList.size() == 0)
            return ApiResponse.fail(ErrorCode.NOT_FOUND, "해당 가격대에 맞는 상품이 없습니다. : "+price+"원 대");
        return ApiResponse.success(giftcardList);

    }

    @PostMapping("/order")
    public ApiResponse<String> giftcardOrderAdd(@RequestBody GiftcardOrderRequestDto giftcardOrderRequest){
        shopService.addGiftcardOrder(giftcardOrderRequest);
        return ApiResponse.success("기프티콘 구매가 완료되었습니다.");
    }

    @GetMapping("/histories")
    public ApiResponse<List<GiftcardHistoryDto>> getGiftcardHistoryList(@RequestBody GiftcardHistoryRequestDto giftcardHistoryRequestDto){
        List<GiftcardHistoryDto> giftcardHistoryList = shopService.findGiftcardByMember(giftcardHistoryRequestDto);
        return ApiResponse.success(giftcardHistoryList);
    }
}
