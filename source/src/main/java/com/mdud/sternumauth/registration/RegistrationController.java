package com.mdud.sternumauth.registration;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class RegistrationController {

    @GetMapping("/register")
    public String registration() {
        return "register";
    }
}
