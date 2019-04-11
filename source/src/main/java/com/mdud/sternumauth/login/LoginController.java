package com.mdud.sternumauth.login;


import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.SessionAttributes;

@Controller
@SessionAttributes({"authorizationRequest"})
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
