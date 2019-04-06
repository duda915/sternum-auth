package com.mdud.sternumauth.user;

import lombok.AllArgsConstructor;
import lombok.Data;

import javax.persistence.*;

@Entity
@Table(name = "service_user_authority")
@Data
public class UserAuthority {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "authority")
    @Enumerated(EnumType.STRING)
    private AuthorityType authorityType;

    public UserAuthority(AuthorityType authorityType) {
        this.authorityType = authorityType;
    }
}
