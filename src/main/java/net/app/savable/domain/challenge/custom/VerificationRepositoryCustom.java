package net.app.savable.domain.challenge.custom;

import net.app.savable.domain.challenge.dto.VerificationDetailDto;

public interface VerificationRepositoryCustom {
    VerificationDetailDto findVerificationDetailByParticipationId(Long participationId);
}
