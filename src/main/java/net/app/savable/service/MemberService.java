package net.app.savable.service;

import lombok.RequiredArgsConstructor;
import net.app.savable.domain.member.Member;
import net.app.savable.domain.member.MemberRepository;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class MemberService {
    private final MemberRepository memberRepository;

    public Member findMemberById(Long memberId){
        return memberRepository.findMemberById(memberId);
    }
}
