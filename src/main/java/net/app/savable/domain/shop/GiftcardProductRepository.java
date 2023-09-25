package net.app.savable.domain.shop;

import net.app.savable.domain.shop.dto.GiftcardDto;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface GiftcardProductRepository extends JpaRepository<GiftcardProduct, Long> {
    List<GiftcardDto> findGiftcardByInOnSaleAndPriceBetweenOrderByBrandNameAsc(Boolean inOnSale,Long minPrice, Long maxPrice);
}
