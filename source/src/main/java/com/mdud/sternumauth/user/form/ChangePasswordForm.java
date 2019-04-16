package com.mdud.sternumauth.user.form;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ChangePasswordForm {
    @Size(min = 6)
    @NotBlank
    private String password;

    private String confirmPassword;
}
