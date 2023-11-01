package net.app.savable.controller;

import lombok.RequiredArgsConstructor;
import net.app.savable.service.SchedulerService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RequestMapping
@RestController
@RequiredArgsConstructor
public class ScheduledVerificationController {

    private final SchedulerService schedulerService;

    @GetMapping("/checkVerification")
    public void checkVerification() {
        schedulerService.scheduledCheckChallengeSuccess();
    }
}
