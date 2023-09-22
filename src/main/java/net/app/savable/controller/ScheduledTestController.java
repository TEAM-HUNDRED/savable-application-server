package net.app.savable.controller;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import net.app.savable.service.SchedulerService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping
public class ScheduledTestController {

    private final SchedulerService schedulerService;

    @GetMapping("/checkVerification")
    public void checkVerification() {
        log.info("ScheduledTestController.checkVerification() 실행");
        schedulerService.checkVerification();
    }
}
