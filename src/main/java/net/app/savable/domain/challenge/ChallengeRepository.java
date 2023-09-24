package net.app.savable.domain.challenge;

import net.app.savable.domain.challenge.custom.ChallengeRepositoryCustom;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;


@Repository
public interface ChallengeRepository extends JpaRepository<Challenge, Long>, ChallengeRepositoryCustom {
    Optional<Challenge> findChallengeById(Long challengeId);
}
