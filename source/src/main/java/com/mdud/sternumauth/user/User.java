package com.mdud.sternumauth.user;

import lombok.*;

import javax.persistence.*;
import java.util.Set;
import java.util.stream.Collectors;

@Entity
@Table(name = "service_user")
@Getter
@Setter
@ToString
@EqualsAndHashCode
@NoArgsConstructor
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "user_name")
    private String username;

    @Column(name = "email")
    private String email;

    @Column(name = "password")
    private String password;

    @Column(name = "image_link")
    private String imageLink;

    @Column(name = "active")
    private Boolean active;

    @OneToMany(cascade = CascadeType.ALL)
    @JoinColumn(name = "user_id")
    private Set<UserAuthority> authoritySet;

    public User(String username, String email, String password, String imageLink, Set<UserAuthority> authoritySet) {
        this.username = username;
        this.email = email;
        setPassword(password);
        this.imageLink = imageLink;
        this.active = false;
        this.authoritySet = authoritySet;
    }

    public void setPassword(String password) {
        this.password = encodePassword(password);
    }

    public UserDTO toDTO() {
        return new UserDTO(this.username, this.password, this.email, this.imageLink,
                authoritySet.stream().map(auth -> auth.getAuthorityType()).collect(Collectors.toSet()));
    }

    private String encodePassword(String password) {
        return PasswordEncoder.getEncoder().encode(password);
    }

}

