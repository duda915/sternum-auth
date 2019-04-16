package com.mdud.sternumauth.user;

import com.mdud.sternumauth.user.dto.UserDTO;
import com.mdud.sternumauth.user.form.ChangePasswordForm;
import com.mdud.sternumauth.util.ValidationUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
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

    @PostMapping("/password")
    public String changePassword(@ModelAttribute("passwordForm") @Valid ChangePasswordForm changePasswordForm, BindingResult bindingResult,
                                 Principal principal, Model model) {

        if (bindingResult.hasErrors()) {
            model.addAttribute("error", ValidationUtils.combineBindingErrors(bindingResult));
            return "index";
        } else if (!changePasswordForm.getPassword().equals(changePasswordForm.getConfirmPassword())) {
            model.addAttribute("error", "passwords must be the same");
            return "index";
        }

        userService.changeUserPassword(principal.getName(), changePasswordForm.getPassword());
        model.addAttribute("info", "password changed");
        return "index";
    }


}
