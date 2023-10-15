package net.app.savable.domain.member;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface MemberRepository extends JpaRepository<Member, Long>{
    Optional<Member> findMemberById(Long memberId);
    Optional<Member> findBySocialId(String socialId);
    Optional<Member> findByUsernameAndAccountStateNot(String username, AccountState accountState);
    Optional<Member> findByPhoneNumberAndAccountStateNot(String phoneNumber, AccountState accountState);
}
