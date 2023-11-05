package net.app.savable.domain.challenge.custom;

import jakarta.persistence.EntityManager;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import net.app.savable.domain.challenge.dto.MyParticipationChallengeDetailDto;
import net.app.savable.domain.challenge.dto.MyParticipationChallengeDto;
import net.app.savable.domain.challenge.dto.VerificationResponseDto;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;
import org.springframework.stereotype.Repository;

import java.util.List;

@Slf4j
@Repository
@RequiredArgsConstructor
@PropertySource("classpath:sql/ParticipationChallengeSql.xml") // "classpath:sql/ParticipationChallengeSql.xml
public class ParticipationChallengeRepositoryImpl implements ParticipationChallengeRepositoryCustom {

    private final EntityManager em;

    @Value("${participationListSql}")
    String participationListSql;

    @Value("${participationDetailsSql}")
    String participationDetailSql;

    @Value("${participationDetailsSubSql}")
    String participationDetailsSubSql;

    @Value("${scheduledRewardSql}")
    String scheduledRewardSql;

    @Override
    public List<MyParticipationChallengeDto> findMyParticipationChallengeByMemberId(Long memberId){
        return em.createQuery(participationListSql, MyParticipationChallengeDto.class)
                .setParameter("memberId", memberId)
                .getResultList();
    }

    @Override
    public MyParticipationChallengeDetailDto findMyParticipationChallengeDetailByParticipationChallengeId(Long participationChallengeId, Long memberId) {
        log.info("ParticipationChallengeRepositoryImpl.findMyParticipationChallengeDetailByParticipationChallengeId() 실행");
        MyParticipationChallengeDetailDto myParticipationChallengeDetailDtos = em.createQuery(participationDetailSql, MyParticipationChallengeDetailDto.class)
                .setParameter("participationChallengeId", participationChallengeId)
                .setParameter("memberId", memberId)
                .getSingleResult();

        List<VerificationResponseDto> verificationDtoList = em.createQuery(participationDetailsSubSql, VerificationResponseDto.class)
                .setParameter("participationChallengeId", participationChallengeId)
                .setParameter("memberId", memberId)
                .getResultList();

        if (!verificationDtoList.isEmpty()) {
            myParticipationChallengeDetailDtos.getVerificationInfoDto().setVerificationResponseDtos(verificationDtoList);
        }

        return myParticipationChallengeDetailDtos;
    }

    @Override
    public Long findScheduledReward(Long memberId){
        return em.createQuery(scheduledRewardSql, Long.class)
                .setParameter("memberId", memberId)
                .getSingleResult();
    }

}
