package net.app.savable.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import net.app.savable.domain.challenge.dto.MyParticipationChallengeDto;
import net.app.savable.service.ParticipationChallengeService;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@Slf4j
@RequiredArgsConstructor
@RestController
public class ParticipationChallengeController {

    private final ParticipationChallengeService participationChallengeService;

    @RequestMapping("/participations")
    public List<MyParticipationChallengeDto> participationList(){
        log.info("ParticipationChallengeController.participationList() 실행");

        Long memberId = 1L;
        List<MyParticipationChallengeDto> myParticipationChallengeDtoList = participationChallengeService.findParticipationChallengeByMemberId(memberId);

        return myParticipationChallengeDtoList;
    }
}
