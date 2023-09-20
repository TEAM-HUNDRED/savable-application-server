package net.app.savable.domain.challenge;

import net.app.savable.domain.challenge.custom.ParticipationChallengeRepositoryCustom;
import net.app.savable.domain.challenge.dto.ParticipationChallengeInfoDto;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ParticipationChallengeRepository extends JpaRepository<ParticipationChallenge, Long>, ParticipationChallengeRepositoryCustom {
}
