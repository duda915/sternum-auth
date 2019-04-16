package com.mdud.sternumauth.registration.dto;

import com.mdud.sternumauth.user.dto.UserDTO;
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
