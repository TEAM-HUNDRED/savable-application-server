package net.app.savable.controller;

import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import net.app.savable.global.config.auth.LoginMember;
import net.app.savable.global.config.auth.dto.SessionMember;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RequestMapping("/session")
@RestController
@RequiredArgsConstructor
public class SessionTestController {

    private final HttpSession httpSession;

    @GetMapping
    public String sessionTest(@LoginMember SessionMember member) {

        System.out.printf("max_inactive_interval: %d\n", httpSession.getMaxInactiveInterval());
        return "session test";
    }
}
