package com.mdud.sternumauth.user;

import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Entity
@Table(name = "service_user_authority")
@Data
@NoArgsConstructor
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
