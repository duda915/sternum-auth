package com.mdud.sternumauth.user.dto;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.mdud.sternumauth.user.AuthorityType;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.Collections;
import java.util.HashSet;

@Data
@EqualsAndHashCode(callSuper = true)
public class CredentialUserDTO extends UserDTO {

    @JsonIgnore
    private String plainPassword;

    public CredentialUserDTO(String username, String plainPassword, String email, String image) {
        super(username, email, image, new HashSet<>(Collections.singletonList(AuthorityType.USER)));
        this.plainPassword = plainPassword;
    }

    public UserDTO toDTO() {
        return new UserDTO(this.getUsername(), this.getEmail(), this.getImage(), this.getAuthorities());
    }
}
