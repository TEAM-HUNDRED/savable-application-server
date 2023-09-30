package net.app.savable.service;

import lombok.RequiredArgsConstructor;
import net.app.savable.domain.history.RewardHistoryRepository;
import net.app.savable.domain.history.RewardType;
import net.app.savable.domain.history.dto.RewardHistoryResponseDto;
import net.app.savable.domain.history.dto.RewardHistorySaveDto;
import net.app.savable.domain.member.Member;
import net.app.savable.domain.member.MemberRepository;
import net.app.savable.domain.shop.*;
import net.app.savable.domain.shop.dto.GiftcardHistoryResponseDto;
import net.app.savable.domain.shop.dto.GiftcardResponseDto;
import net.app.savable.domain.shop.dto.request.GiftcardOrderRequestDto;
import net.app.savable.global.config.auth.dto.SessionMember;
import net.app.savable.global.error.exception.GeneralException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;import java.util.List;

import static net.app.savable.global.error.exception.ErrorCode.*;

@Service
@RequiredArgsConstructor
public class ShopService {
    private final GiftcardProductRepository giftcardProductRepository;
    private final GiftcardOrderRepository giftcardOrderRepository;
    private final MemberRepository memberRepository;
    private final RewardHistoryRepository rewardHistoryRepository;

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
        Member orderedMember = memberRepository.findMemberById(giftcardOrderRequest.getMember().getId())
                .orElseThrow(() -> new GeneralException(NOT_FOUND,"INVALID_MEMBER : "+giftcardOrderRequest.getMember().getId()));
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

        //reward_history에 기록
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
                .description(orderProduct.getBrandName()+orderProduct.getProductName())
                .member(giftcardOrderRequest.getMember())
                .build();

        rewardHistoryRepository.save(rewardHistorySave.toEntity());
    }

    public List<GiftcardHistoryResponseDto> findGiftcardByMember(SessionMember member){
        Member orderedMember = memberRepository.findMemberById(member.getId()) // TODO
                .orElseThrow(() -> new GeneralException(NOT_FOUND,"INVALID_MEMBER : "+member.getId()));

        List<GiftcardHistoryResponseDto> giftcardHistoryList = giftcardOrderRepository.findGiftcardByMemberOrderByCreatedAtDesc(orderedMember)
                .stream()
                .map(GiftcardHistoryResponseDto::new)
                .toList();
        return giftcardHistoryList;
    }
}
