package com.mdud.sternumauth.user;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.security.Principal;

@Controller
public class UserController {

    private final UserService userService;

    @Autowired
    public UserController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping("/me")
    public @ResponseBody
    UserDTO getLoggedUser(Principal principal) {
        return userService.getUserByUsername(principal.getName());
    }

    @GetMapping("/")
    public String userInfo(Principal principal, Model model) {
        UserDTO user = userService.getUserByUsername(principal.getName());

        model.addAttribute("user", user);
        return "index";
    }


}
