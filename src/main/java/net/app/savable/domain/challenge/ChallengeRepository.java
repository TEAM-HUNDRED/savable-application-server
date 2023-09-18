package net.app.savable.domain.challenge;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
@Repository
public interface ChallengeRepository extends JpaRepository<Challenge, Long> {
    List<Challenge> findByHasDeadlineFalse();

    @Query("select c from Challenge c where c.hasDeadline=true and c.startDate<=current_date and current_date<=c.endDate")
    List<Challenge> findChallengeByDate();
}
