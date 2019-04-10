package com.mdud.sternumauth.user;

import com.fasterxml.jackson.annotation.JsonIgnore;
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
