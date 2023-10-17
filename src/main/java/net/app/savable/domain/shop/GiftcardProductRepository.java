package net.app.savable.domain.shop;

import net.app.savable.domain.shop.dto.GiftcardProductResponseDto;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface GiftcardProductRepository extends JpaRepository<GiftcardProduct, Long> {
    List<GiftcardProductResponseDto> findGiftcardByInOnSaleAndPriceBetweenOrderByPriceAscBrandNameAsc(Boolean inOnSale, Long minPrice, Long maxPrice);

    Optional<GiftcardProduct> findGiftcardProductById(Long id);
}
