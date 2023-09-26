package net.app.savable.domain.shop;

import net.app.savable.domain.shop.dto.GiftcardResponseDto;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface GiftcardProductRepository extends JpaRepository<GiftcardProduct, Long> {
    List<GiftcardResponseDto> findGiftcardByInOnSaleAndPriceBetweenOrderByBrandNameAsc(Boolean inOnSale,Long minPrice, Long maxPrice);

    Optional<GiftcardProduct> findGiftcardProductById(Long id);
}
