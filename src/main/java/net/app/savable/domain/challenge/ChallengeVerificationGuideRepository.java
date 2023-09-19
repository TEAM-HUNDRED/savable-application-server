package net.app.savable.domain.challenge;

import net.app.savable.domain.challenge.dto.ChallengeGuideDto;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ChallengeVerificationGuideRepository extends JpaRepository<ChallengeVerificationGuide, Long> {
    List<ChallengeVerificationGuide> findByChallengeIdOrderByIsPassDesc(Integer challengeId);
}
