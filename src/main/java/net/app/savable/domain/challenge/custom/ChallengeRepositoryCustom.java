package net.app.savable.domain.challenge.custom;

import net.app.savable.domain.challenge.Challenge;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface ChallengeRepositoryCustom {
    List<Challenge> findChallengeByDate();
}
