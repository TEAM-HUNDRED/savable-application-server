package net.app.savable.domain.history;

import net.app.savable.domain.history.dto.RewardHistoryResponseDto;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface RewardHistoryRepository extends JpaRepository<RewardHistory, Long>{
    RewardHistoryResponseDto findTopByMemberIdOrderByCreatedAtDesc(Long memberId);
}
