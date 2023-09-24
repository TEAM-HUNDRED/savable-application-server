package net.app.savable.domain.history;

import net.app.savable.domain.history.dto.SavingsHistoryResponseDto;
import net.app.savable.domain.history.dto.SavingsHistorySaveDto;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface SavingsHistoryRepository extends JpaRepository<SavingsHistory, Long> {
    SavingsHistoryResponseDto findTopByMemberIdOrderByCreatedAtDesc(Long memberId);
}
