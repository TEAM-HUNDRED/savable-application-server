package net.app.savable.web;

import net.app.savable.config.auth.LoginUser;
import net.app.savable.config.auth.dto.SessionUser;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class IndexController {

    @GetMapping("/")
    public String index(Model model, @LoginUser SessionUser user){
        System.out.printf("user: %s\n", user);

        if (user != null) {
            model.addAttribute("userName", user.getName());
        }
        return "index";
    }
}
