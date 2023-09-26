package net.app.savable.service;

import lombok.RequiredArgsConstructor;
import net.app.savable.domain.member.Member;
import net.app.savable.domain.member.MemberRepository;
import net.app.savable.domain.shop.*;
import net.app.savable.domain.shop.dto.GiftcardResponseDto;
import net.app.savable.domain.shop.dto.request.GiftcardOrderRequestDto;
import net.app.savable.global.common.GeneralException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;import java.util.List;

import static net.app.savable.global.common.ErrorCode.*;

@Service
@RequiredArgsConstructor
public class ShopService {
    private final GiftcardProductRepository giftcardProductRepository;
    private final GiftcardOrderRepository giftcardOrderRepository;
    private final MemberRepository memberRepository;

    public List<GiftcardResponseDto> findGiftcardByInOnSale(Boolean inOnSale, Long price){
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

        List<GiftcardResponseDto> giftcardList = giftcardProductRepository.findGiftcardByInOnSaleAndPriceBetweenOrderByBrandNameAsc(inOnSale,minPrice,maxPrice);
        return giftcardList;
    }

    @Transactional(readOnly = false)
    public void addGiftcardOrder(GiftcardOrderRequestDto giftcardOrderRequest){
        Member orderedMember = memberRepository.findMemberById(giftcardOrderRequest.getMemberId())
                .orElseThrow(() -> new GeneralException(NOT_FOUND,"INVALID_MEMBER : "+giftcardOrderRequest.getMemberId()));
        GiftcardProduct orderProduct = giftcardProductRepository.findGiftcardProductById(giftcardOrderRequest.getGiftcardId())
                .orElseThrow(() -> new GeneralException(NOT_FOUND,"INVALID_GIFTCARD_PRODUCT : "+giftcardOrderRequest.getGiftcardId()));

        if (!orderProduct.getInOnSale()){
            throw new GeneralException(INVALID_INPUT_VALUE, "판매 중인 기프티콘이 아닙니다.");
        }
        Long totalPrice = giftcardOrderRequest.getQuantity()* orderProduct.getPrice();
        if ( orderedMember.getReward() < totalPrice){
            throw new GeneralException(INSUFFICIENT_BALANCE,"리워드가 부족합니다");
        }

        giftcardOrderRepository.save(giftcardOrderRequest.toEntity(orderedMember,orderProduct, SendState.WAITING));

        // 리워드 차감
        orderedMember.updateReward(-totalPrice);
    }
}
