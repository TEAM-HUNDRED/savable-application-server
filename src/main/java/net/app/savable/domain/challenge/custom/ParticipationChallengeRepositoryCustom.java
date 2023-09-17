package net.app.savable.domain.challenge.custom;


import net.app.savable.domain.challenge.dto.MyParticipationChallengeDto;
import org.springframework.stereotype.Repository;

import java.util.List;

public interface ParticipationChallengeRepositoryCustom {
    List<MyParticipationChallengeDto> findMyParticipationChallengeByMemberId(Long memberId);
}

