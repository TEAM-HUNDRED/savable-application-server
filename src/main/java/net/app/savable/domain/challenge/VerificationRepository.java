package net.app.savable.domain.challenge;

import net.app.savable.domain.challenge.custom.VerificationRepositoryCustom;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface VerificationRepository extends JpaRepository<Verification, Long>, VerificationRepositoryCustom {
    List<Verification> findByStateIs(VerificationState state); // 인증 상태가 state인 인증을 찾는 함수
    Long countByParticipationChallenge_IdAndState(Long participationChallengeId, VerificationState state); // 동일한 참여 챌린지의 인증 상태가 state인 인증 개수를 찾는 함수
    Long countByMember_IdAndState(Long memberId, VerificationState state); // 동일한 회원의 인증 상태가 state인 인증 개수를 찾는 함수

    @Query("SELECT v FROM Verification v WHERE v.participationChallenge.challenge.id = :challengeId AND v.state = :state")
    List<Verification> findByChallengeIdAndState(@Param("challengeId") Long challengeId, @Param("state") VerificationState state); // 동일한 챌린지의 인증 상태가 state인 인증을 찾는 함수
}
