package net.app.savable.domain.challenge;

import net.app.savable.domain.challenge.custom.ParticipationChallengeRepositoryCustom;
import net.app.savable.domain.member.Member;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Repository
public interface ParticipationChallengeRepository extends JpaRepository<ParticipationChallenge, Long>, ParticipationChallengeRepositoryCustom {

    List<ParticipationChallenge> findByParticipationStateAndEndDateBefore(ParticipationState participationState, LocalDate endDate);
    Long countByMember_IdAndParticipationState(Long memberId, ParticipationState participationState);
//    ParticipationState findMyParticipationChallengeByMemberIdAndChallengeId(Long memberId, Long challengeId);
    Optional<ParticipationChallenge> findParticipationStateByMemberAndChallenge(Member member, Challenge challenge);
}
