package net.app.savable.domain.challenge.custom;

import jakarta.persistence.EntityManager;
import lombok.RequiredArgsConstructor;
import net.app.savable.domain.challenge.dto.MyParticipationChallengeDetailDto;
import net.app.savable.domain.challenge.dto.MyParticipationChallengeDto;
import net.app.savable.domain.challenge.dto.VerificationDto;

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
                "cast((p.savings * sum(case when v.state = 'SUCCESS' then 1 else 0 end)) as Long), " +
                "case when sum(case when v.state = 'SUCCESS' and DATE(v.dateTime) = CURRENT_DATE then 1 else 0 end) > 0 then true else false end) " +
                "from ParticipationChallenge p " +
                "join p.challenge c " +
                "left join p.verificationList v " +
                "where p.member.id = :memberId and p.participationState = 'IN_PROGRESS' " +
                "group by p.id, c.image, c.title, p.verificationGoal, c.reward, c.estimatedSavings";

        return em.createQuery(query, MyParticipationChallengeDto.class)
                .setParameter("memberId", memberId)
                .getResultList();
    }

    @Override
    public MyParticipationChallengeDetailDto findMyParticipationChallengeDetailByParticipationChallengeId(Long participationChallengeId) {
        String query = "select new net.app.savable.domain.challenge.dto.MyParticipationChallengeDetailDto( " +
                "new net.app.savable.domain.challenge.dto.ParticipationChallengeInfoDto( " +
                "c.id, c.image, c.title, p.startDate, p.endDate, p.verificationGoal, " +
                "cast((c.reward * sum(case when v.state = 'SUCCESS' then 1 else 0 end)) as Long), " +
                "cast((p.savings * sum(case when v.state = 'SUCCESS' then 1 else 0 end)) as Long)), " +
                "new net.app.savable.domain.challenge.dto.VerificationInfoDto( " +
                "sum(case when v.state = 'SUCCESS' then 1 else 0 end), " +
                "sum(case when v.state = 'FAIL' then 1 else 0 end), " +
                "cast((p.verificationGoal - sum(case when v.state = 'SUCCESS' then 1 else 0 end)) as Long)) " +
                ") " +
                "from ParticipationChallenge p " +
                "join p.challenge c " +
                "left join p.verificationList v " +
                "where p.id = :participationChallengeId " +
                "group by p.id, c.id, c.image, c.title, p.startDate, p.endDate, p.verificationGoal, c.reward, p.savings";

        String subQuery = "select new net.app.savable.domain.challenge.dto.VerificationDto(v.id, v.image, v.state, v.dateTime) " +
                "from Verification v " +
                "where v.participationChallenge.id = :participationChallengeId";

        MyParticipationChallengeDetailDto myParticipationChallengeDetailDtos = em.createQuery(query, MyParticipationChallengeDetailDto.class)
                .setParameter("participationChallengeId", participationChallengeId)
                .getSingleResult();

        List<VerificationDto> verificationDtoList = em.createQuery(subQuery, VerificationDto.class)
                .setParameter("participationChallengeId", participationChallengeId)
                .getResultList();

        if (!verificationDtoList.isEmpty()) {
            myParticipationChallengeDetailDtos.getVerificationInfoDto().setVerificationDtoList(verificationDtoList);
        }

        return myParticipationChallengeDetailDtos;
    }
}
