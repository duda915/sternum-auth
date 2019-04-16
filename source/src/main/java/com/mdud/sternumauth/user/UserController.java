package com.mdud.sternumauth.user;

import com.mdud.sternumauth.cdn.CDNEntity;
import com.mdud.sternumauth.cdn.CDNService;
import com.mdud.sternumauth.user.dto.UserDTO;
import com.mdud.sternumauth.user.form.ChangeImageForm;
import com.mdud.sternumauth.user.form.ChangePasswordForm;
import com.mdud.sternumauth.util.ValidationUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.validation.Valid;
import java.io.IOException;
import java.security.Principal;

@Controller
public class UserController {

    private final UserService userService;
    private final CDNService cdnService;

    @Autowired
    public UserController(UserService userService, CDNService cdnService) {
        this.userService = userService;
        this.cdnService = cdnService;
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
                                 Principal principal, RedirectAttributes redirectAttributes) {

        if (bindingResult.hasErrors()) {
            redirectAttributes.addFlashAttribute("error", ValidationUtils.combineBindingErrors(bindingResult));
            return "redirect:/";
        } else if (!changePasswordForm.getPassword().equals(changePasswordForm.getConfirmPassword())) {
            redirectAttributes.addFlashAttribute("error", "password must not be the same");
            return "redirect:/";
        }

        userService.changeUserPassword(principal.getName(), changePasswordForm.getPassword());
        redirectAttributes.addFlashAttribute("info", "password changed");

        return "redirect:/";
    }

    @PostMapping("/image")
    public String changeImage(@ModelAttribute("imageForm") ChangeImageForm changeImageForm, Principal principal) throws IOException {
        if(changeImageForm.getImage().isEmpty()) {
            userService.changeUserImage(principal.getName(), null);
        } else {
            CDNEntity image = cdnService.addImage(changeImageForm.getImage().getBytes());
            userService.changeUserImage(principal.getName(), image.getResourceURL());
        }

        return "redirect:/";
    }


}
