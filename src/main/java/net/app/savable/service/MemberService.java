package net.app.savable.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import net.app.savable.domain.member.AccountState;
import net.app.savable.domain.member.Member;
import net.app.savable.domain.member.MemberRepository;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class MemberService {
    private final MemberRepository memberRepository;

    public Member findById(Long memberId){
        return memberRepository.findById(memberId)
                .orElseThrow(()-> new IllegalArgumentException("INVALID_MEMBER :"+memberId));
    }

    public Member findByUsernameAndAccountStateNot(String username, AccountState accountState){
        return memberRepository.findByUsernameAndAccountStateNot(username, accountState)
                .orElse(null);
    }

    public Member findByPhoneNumberAndAccountStateNot(String phoneNumber, AccountState accountState){
        return memberRepository.findByPhoneNumberAndAccountStateNot(phoneNumber, accountState)
                .orElse(null);
    }

    @Transactional(readOnly = false)
    public void deleteMember(Member member){
        member.delete();
    }

    @Transactional(readOnly = false)
    public void updateMember(Member member, String username, String profileImage, String phoneNumber){
        try {
            member.update(username, profileImage, phoneNumber);
        } catch (RuntimeException ex) {
            throw new DataIntegrityViolationException("잘못된 데이터가 입력되었습니다");
        }
    }
}
