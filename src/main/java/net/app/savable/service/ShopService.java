package net.app.savable.service;

import lombok.RequiredArgsConstructor;
import net.app.savable.domain.history.RewardHistoryRepository;
import net.app.savable.domain.history.RewardType;
import net.app.savable.domain.history.dto.RewardHistoryResponseDto;
import net.app.savable.domain.history.dto.RewardHistorySaveDto;
import net.app.savable.domain.member.Member;
import net.app.savable.domain.member.MemberRepository;
import net.app.savable.domain.shop.*;
import net.app.savable.domain.shop.dto.GiftcardOrderResponseDto;
import net.app.savable.domain.shop.dto.GiftcardOrderSaveDto;
import net.app.savable.domain.shop.dto.GiftcardProductResponseDto;
import net.app.savable.domain.shop.dto.GiftcardOrderRequestDto;
import net.app.savable.global.error.exception.GeneralException;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;import java.util.List;
import java.util.stream.Collectors;

import static net.app.savable.global.error.exception.ErrorCode.*;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class ShopService {
    private final GiftcardProductRepository giftcardProductRepository;
    private final GiftcardOrderRepository giftcardOrderRepository;
    private final MemberRepository memberRepository;
    private final RewardHistoryRepository rewardHistoryRepository;

    @Cacheable(value="GiftcardList", key="#price", cacheManager="contentCacheManager")
    public List<GiftcardProductResponseDto> findGiftcardByInOnSale(Boolean inOnSale, Long price){
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

        List<GiftcardProductResponseDto> giftcardList
                = giftcardProductRepository.findGiftcardByInOnSaleAndPriceBetweenOrderByPriceAscBrandNameAsc(inOnSale,minPrice,maxPrice)
                .stream()
                .map(GiftcardProductResponseDto::valueOf)
//                .toList();
                .collect(Collectors.toList());
        return giftcardList;
    }

    @Transactional(readOnly = false)
    public void addGiftcardOrder(GiftcardOrderRequestDto giftcardOrderRequest, Long memberId){
        // 기프티콘 구매
        Member orderedMember = memberRepository.findMemberById(memberId)
                .orElseThrow(() -> new GeneralException(NOT_FOUND,"INVALID_MEMBER : "+memberId));
        GiftcardProduct orderProduct = giftcardProductRepository.findGiftcardProductById(giftcardOrderRequest.getGiftcardId())
                .orElseThrow(() -> new GeneralException(NOT_FOUND,"INVALID_GIFTCARD_PRODUCT : "+giftcardOrderRequest.getGiftcardId()));

        if (!orderProduct.getInOnSale()){
            throw new GeneralException(INVALID_INPUT_VALUE, "판매 중인 기프티콘이 아닙니다.");
        }
        Long totalPrice = giftcardOrderRequest.getQuantity()* orderProduct.getPrice();
        if ( orderedMember.getReward() < totalPrice){
            throw new GeneralException(INSUFFICIENT_BALANCE,"리워드가 부족합니다");
        }

        GiftcardOrderSaveDto giftcardOrderSaveDto = GiftcardOrderSaveDto.builder()
                .positivePoint(giftcardOrderRequest.getPositivePoint())
                .negativePoint(giftcardOrderRequest.getNegativePoint())
                .wishChallenge(giftcardOrderRequest.getWishChallenge())
                .quantity(giftcardOrderRequest.getQuantity())
                .sendState(SendState.WAITING)
                .member(orderedMember)
                .giftcardProduct(orderProduct)
                .build();

        giftcardOrderRepository.save(giftcardOrderSaveDto.toEntity());

        // 리워드 차감
        orderedMember.updateReward(-totalPrice);

        // 기프티콘 구매 후 히스토리 기록
        RewardHistoryResponseDto recentRewardHistory = rewardHistoryRepository.findTopByMemberIdOrderByCreatedAtDesc(orderedMember.getId()); // 가장 최근 리워드 내역 조회
        if (recentRewardHistory == null) {
            recentRewardHistory = RewardHistoryResponseDto.builder()
                    .totalReward(0L)
                    .build();
        }

        RewardHistorySaveDto rewardHistorySave = RewardHistorySaveDto.builder()
                .reward(-totalPrice)
                .totalReward(recentRewardHistory.getTotalReward() - totalPrice)
                .rewardType(RewardType.GIFTCARD)
                .description('['+orderProduct.getBrandName()+"] "+orderProduct.getProductName())
                .member(orderedMember)
                .build();

        rewardHistoryRepository.save(rewardHistorySave.toEntity());
    }

    public List<GiftcardOrderResponseDto> findGiftcardByMember(Long memberId){
        Member orderedMember = memberRepository.findMemberById(memberId)
                .orElseThrow(() -> new GeneralException(NOT_FOUND,"INVALID_MEMBER : "+memberId));

        List<GiftcardOrderResponseDto> giftcardHistoryList = giftcardOrderRepository.findGiftcardByMemberOrderByCreatedAtDesc(orderedMember)
                .stream()
                .map(GiftcardOrderResponseDto::new)
                .toList();
        return giftcardHistoryList;
    }
}
