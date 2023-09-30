package net.app.savable.controller;

import net.app.savable.global.config.auth.LoginMember;
import net.app.savable.global.config.auth.dto.SessionMember;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class IndexController {

    @GetMapping("/")
    public String index(Model model, @LoginMember SessionMember member){
        System.out.printf("member: %s\n", member);

        if (member != null) {
            model.addAttribute("userName", member.getName());
        }
        return "index";
    }
}
