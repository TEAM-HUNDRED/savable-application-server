package net.app.savable.domain.challenge;

import net.app.savable.domain.challenge.custom.ChallengeRepositoryCustom;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ChallengeRepository extends JpaRepository<Challenge, Long>, ChallengeRepositoryCustom {
    Challenge findById(Integer challengeId);
}