package com.mdud.sternumauth.registration;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

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

    @NotBlank(message = "email may not be blank")
    @Email(message = "you need to provide valid email")
    private String email;

}
