package com.mdud.sternumauth.registration.form;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class RegistrationForm {

    @NotBlank(message = "username may not be blank")
    private String username;
    @NotBlank(message = "password may not be blank")
    @Size(min = 6, message = "password must contain minimum 6 characters")
    private String plainPassword;

    private String confirmPassword;

    @NotBlank(message = "email may not be blank")
    @Email(message = "you need to provide valid email")
    private String email;

    private MultipartFile image;

}
