package net.app.savable.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import net.app.savable.domain.challenge.dto.MyParticipationChallengeDetailDto;
import net.app.savable.domain.challenge.dto.MyParticipationChallengeDto;
import net.app.savable.global.config.auth.LoginMember;
import net.app.savable.global.config.auth.dto.SessionMember;
import net.app.savable.global.error.ApiResponse;
import net.app.savable.service.MemberService;
import net.app.savable.service.ParticipationChallengeService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@Slf4j
@RequiredArgsConstructor
@RestController
@RequestMapping("/participations")
public class ParticipationChallengeController {

    private final ParticipationChallengeService participationChallengeService;
    private final MemberService memberService;

    @GetMapping()
    public ApiResponse<List<MyParticipationChallengeDto>> participationList(@LoginMember SessionMember member){
        log.info("ParticipationChallengeController.participationList() 실행");

        List<MyParticipationChallengeDto> myParticipationChallengeDtoList = participationChallengeService.findParticipationChallengeByMemberId(member.getId());

        return ApiResponse.success(myParticipationChallengeDtoList);
    }

    @GetMapping("/{participationChallengeId}")
    public ApiResponse<MyParticipationChallengeDetailDto> participationDetails(@PathVariable Long participationChallengeId){
        log.info("ParticipationChallengeController.participationDetails() 실행");

        MyParticipationChallengeDetailDto myParticipationChallengeDetailDtos = participationChallengeService.findParticipationChallengeDetailByParticipationChallengeId(participationChallengeId);

        return ApiResponse.success(myParticipationChallengeDetailDtos);
    }
}
