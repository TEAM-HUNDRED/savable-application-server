package net.app.savable.domain.shop;

import net.app.savable.domain.member.Member;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface GiftcardOrderRepository extends JpaRepository<GiftcardOrder, Long> {
    List<GiftcardOrder> findGiftcardByMemberOrderByCreatedAtDesc(Member member);
}
