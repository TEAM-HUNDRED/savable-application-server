package net.app.savable.domain.shop;

import net.app.savable.domain.shop.dto.GiftcardDto;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface GiftcardOrderRepository extends JpaRepository<GiftcardOrder, Long> {
}
