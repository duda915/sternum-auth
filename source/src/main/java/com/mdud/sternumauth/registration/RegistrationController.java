package com.mdud.sternumauth.registration;

import com.mdud.sternumauth.cdn.CDNService;
import com.mdud.sternumauth.cdn.ImageException;
import com.mdud.sternumauth.user.CredentialUserDTO;
import lombok.AllArgsConstructor;
import lombok.extern.java.Log;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.Valid;
import java.awt.*;
import java.io.IOException;
import java.util.concurrent.atomic.AtomicReference;

@Controller
@Log
public class RegistrationController {

    private final CDNService cdnService;
    private final RegistrationService registrationService;

    @Autowired
    public RegistrationController(CDNService cdnService, RegistrationService registrationService) {
        this.cdnService = cdnService;
        this.registrationService = registrationService;
    }

    @GetMapping("/register")
    public String registration() {
        return "registerView";
    }

    @PostMapping(value = "/register", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public String register(@Valid @ModelAttribute("form") RegistrationForm registrationForm, BindingResult bindingResult, Model model) {
        if (bindingResult.hasErrors()) {
            AtomicReference<String> errors = new AtomicReference<>();

            bindingResult.getFieldErrors().forEach(error -> {
                errors.set(errors.get() + error.getDefaultMessage() + "\n");
            });

            model.addAttribute("error", errors.get());
            return "registerView";
        }

        if(!registrationForm.getPlainPassword().equals(registrationForm.getConfirmPassword())) {
            model.addAttribute("error", "passwords must be the same");
            return "registerView";
        }

        CredentialUserDTO newUser = new CredentialUserDTO(registrationForm.getUsername(), registrationForm.getPlainPassword(),
                registrationForm.getEmail(), null);

        String uploadedImage = null;
        if (!registrationForm.getImage().isEmpty()) {
            try {
                uploadedImage = cdnService.addImage(registrationForm.getImage().getBytes()).getResourceURL();
                log.info(uploadedImage);
            } catch (IOException e) {
                e.printStackTrace();
                throw new RegistrationException("failed to upload image");
            }
        }
        newUser.setImage(uploadedImage);

        registrationService.register(newUser);
        return "redirect:login";
    }
}
