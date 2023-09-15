package net.app.savable.service;

import lombok.RequiredArgsConstructor;
import net.app.savable.domain.challenge.ParticipationChallenge;
import net.app.savable.domain.challenge.ParticipationChallengeRepository;
import net.app.savable.domain.challenge.dto.MyParticipationChallengeDto;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ParticipationChallengeService {

    private final ParticipationChallengeRepository participationChallengeRepository;

    public List<MyParticipationChallengeDto> findParticipationChallengeByMemberId(Long memberId) {
        return participationChallengeRepository.findMyParticipationChallengeByMemberId(memberId);
    }
}
