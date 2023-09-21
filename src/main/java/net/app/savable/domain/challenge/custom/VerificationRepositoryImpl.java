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

    @Value("${verificationDetailSubSql}")
    String verificationDetailSubSql;

    @Override
    public VerificationDetailDto findVerificationDetailByParticipationId(Long participationId) {
        VerificationDetailDto verificationDetailDto = em.createQuery(verificationDetailSql, VerificationDetailDto.class)
                .setParameter("participationId", participationId)
                .getSingleResult();

        verificationDetailDto.setImage(em.createQuery(verificationDetailSubSql, String.class)
                .setParameter("participationId", participationId)
                .setMaxResults(1)
                .getSingleResult());

        return verificationDetailDto;
    }
}
