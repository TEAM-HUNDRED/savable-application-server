package net.app.savable.domain.challenge;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ChallengeVerificationGuideRepository extends JpaRepository<ChallengeVerificationGuide, Long> {
}
