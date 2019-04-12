package com.mdud.sternumauth.registration;

import com.mdud.sternumauth.user.UserDTO;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.sql.Date;

@Data
@AllArgsConstructor
public class RegistrationTokenDTO {
    private Long id;
    private UserDTO userDTO;
    private Date registrationDate;
}
