package com.mdud.sternumauth.registration;

import com.mdud.sternumauth.cdn.CDNService;
import com.mdud.sternumauth.cdn.ImageException;
import com.mdud.sternumauth.user.CredentialUserDTO;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.Valid;
import java.io.IOException;
import java.util.concurrent.atomic.AtomicReference;

@Controller
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

    @PostMapping("/register")
    public String register(@Valid @ModelAttribute("form") RegistrationForm registrationForm, BindingResult bindingResult,
                           @RequestParam(value = "file", required = false) MultipartFile image,
                           Model model) {
        if (bindingResult.hasErrors()) {
            AtomicReference<String> errors = new AtomicReference<>();

            bindingResult.getFieldErrors().forEach(error -> {
                errors.set(errors.get() + error.getDefaultMessage() + "\n");
            });

            model.addAttribute("error", errors.get());
            return "registerView";
        }

        CredentialUserDTO newUser = new CredentialUserDTO(registrationForm.getUsername(), registrationForm.getPlainPassword(),
                registrationForm.getEmail(), null);

        String uploadedImage = null;
        if (image != null) {
            try {
                uploadedImage = cdnService.addImage(image.getBytes()).getResourceURL();
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
