package com.mdud.sternumauth.login;


import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class LoginController {

    @GetMapping("/login")
    public String login() {
        return "login";
    }

    @GetMapping("/oauth/confirm_access")
    public String confirmOAuthAccess(Model model, @RequestParam("client_id") String clientId) {
        model.addAttribute("appToAuth", clientId);
        return "approveoauth";
    }
}
