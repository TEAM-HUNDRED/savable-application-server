package net.app.savable.domain.challenge.custom;

import jakarta.persistence.EntityManager;
import lombok.RequiredArgsConstructor;
import net.app.savable.domain.challenge.Challenge;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
@RequiredArgsConstructor
@PropertySource("classpath:sql/ParticipationChallengeSql.xml") // "classpath:sql/ParticipationChallengeSql.xml
public class ChallengeRepositoryImpl implements ChallengeRepositoryCustom{
    private final EntityManager em;

    @Value("${challengeListSql}")
    String challengeListSql;
    @Override
    public List<Challenge> findChallengeByDate(){
        List<Challenge> challengelist = em.createQuery(challengeListSql).getResultList();
        return challengelist;
    }

}
