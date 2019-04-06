package com.mdud.sternumauth.user;

import lombok.Data;

@Data
public class UserDTO {
    private String username;
    private String password;
    private String email;
    private String image;
}
