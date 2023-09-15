package net.app.savable.domain.challenge.custom;

import jakarta.persistence.EntityManager;
import lombok.RequiredArgsConstructor;
import net.app.savable.domain.challenge.dto.MyParticipationChallengeDto;

import java.util.List;

@RequiredArgsConstructor
public class ParticipationChallengeRepositoryImpl implements ParticipationChallengeRepositoryCustom {

    private final EntityManager em;

    @Override
    public List<MyParticipationChallengeDto> findMyParticipationChallengeByMemberId(Long memberId){
        String query = "select new net.app.savable.domain.challenge.dto.MyParticipationChallengeDto(" +
                "p.id, " +
                "c.image, " +
                "c.title, " +
                "cast(p.verificationGoal - sum(case when v.state = 'SUCCESS' then 1 else 0 end) as Long), " +
                "cast((sum(case when v.state = 'SUCCESS' then 1 else 0 end) * 100 / p.verificationGoal) as Long), " +
                "cast((c.reward * sum(case when v.state = 'SUCCESS' then 1 else 0 end)) as Long), " +
                "cast((p.savings * sum(case when v.state = 'SUCCESS' then 1 else 0 end)) as Long)) " +
                "from ParticipationChallenge p " +
                "join p.challenge c " +
                "left join p.verificationList v " +
                "where p.member.id = :memberId " +
                "group by p.id, c.image, c.title, p.verificationGoal, c.reward, c.estimatedSavings";


        return em.createQuery(query, MyParticipationChallengeDto.class)
                .setParameter("memberId", memberId)
                .getResultList();
    }
}
