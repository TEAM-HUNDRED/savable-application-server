package net.app.savable.domain.shop;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface GiftcardProductRepository extends JpaRepository<GiftcardProduct, Long> {
}
