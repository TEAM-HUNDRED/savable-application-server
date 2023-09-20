package net.app.savable.domain.challenge.custom;

import jakarta.persistence.EntityManager;
import lombok.RequiredArgsConstructor;
import net.app.savable.domain.challenge.dto.VerificationDetailDto;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;
import org.springframework.stereotype.Repository;

@Repository
@RequiredArgsConstructor
@PropertySource("classpath:sql/VerificationSql.xml")
public class VerificationRepositoryImpl implements VerificationRepositoryCustom{

    private final EntityManager em;

    @Value("${verificationDetailSql}")
    String verificationDetailSql;

    @Override
    public VerificationDetailDto findVerificationDetailByParticipationChallengeId(Long participationChallengeId) {
        return em.createQuery(verificationDetailSql, VerificationDetailDto.class)
                .setParameter("participationChallengeId", participationChallengeId)
                .getSingleResult();
    }
}
