package net.app.savable.domain.shop;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface GiftcardOrderRepository extends JpaRepository<GiftcardOrder, Long> {
}
