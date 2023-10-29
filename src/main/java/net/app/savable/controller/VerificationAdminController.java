package net.app.savable.controller;

import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import net.app.savable.domain.challenge.dto.ChallengeResponseDto;
import net.app.savable.domain.challenge.dto.VerificationAdminResponseDto;
import net.app.savable.domain.challenge.dto.VerificationAdminUpdateDto;
import net.app.savable.service.ChallengeAdminService;
import net.app.savable.service.VerificationAdminService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Slf4j
@Controller
@RequiredArgsConstructor
@RequestMapping("/admin")
public class VerificationAdminController {

    private final ChallengeAdminService challengeAdminService;
    private final VerificationAdminService verificationAdminService;

    @GetMapping("/verification")
    public String verificationList(Model model) {
        List<ChallengeResponseDto> challenges = challengeAdminService.getChallenges();
        model.addAttribute("challenges", challenges);

        return "verification-admin";
    }

    @GetMapping("/verification/{challengeId}")
    public String verificationList(@PathVariable Long challengeId, Model model) {
        log.info("VerificationAdminController.verificationAdmin() 실행");

        List<ChallengeResponseDto> challenges = challengeAdminService.getChallenges();
        List<VerificationAdminResponseDto> verifications = verificationAdminService.getVerifications(challengeId);

        model.addAttribute("verifications", verifications);
        model.addAttribute("challenges", challenges);
        model.addAttribute("currentChallengeId", challengeId);
        return "verification-admin";
    }

    @PostMapping("/verification/update")
    public String verificationUpdate(@RequestParam Map<String, String> data,
                                     HttpServletRequest request) {
        log.info("VerificationAdminController.verificationUpdate() 실행");
        System.out.printf("data: %s\n", data.toString());

        verificationAdminService.updateVerification(data);

        String referer = request.getHeader("Referer");
        return "redirect:" + (referer != null ? referer : "/admin/verification");
    }

    @PostMapping("/verification/updateAllSuccess")
    public String verificationUpdateAllSuccess(@RequestParam Map<String, String> data,
                                     HttpServletRequest request) {
        log.info("VerificationAdminController.verificationUpdateAllSuccess() 실행");
        System.out.printf("data: %s\n", data.toString());

        verificationAdminService.updateVerification(data);

        String referer = request.getHeader("Referer");
        return "redirect:" + (referer != null ? referer : "/admin/verification");
    }
}
