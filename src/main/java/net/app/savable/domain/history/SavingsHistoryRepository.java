package net.app.savable.domain.history;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface SavingsHistoryRepository extends JpaRepository<SavingsHistory, Long> {
}
