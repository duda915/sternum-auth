package com.mdud.sternumauth.user.dto;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.mdud.sternumauth.user.AuthorityType;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.Set;

@Data
@AllArgsConstructor
public class UserDTO {
    private String username;

    @JsonIgnore
    private String email;
    private String image;

    private Set<AuthorityType> authorities;
}
