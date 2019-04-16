package com.mdud.sternumauth.user;

import com.mdud.sternumauth.user.dto.CredentialUserDTO;
import com.mdud.sternumauth.user.dto.UserDTO;
import lombok.*;

import javax.persistence.*;
import java.util.HashSet;
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
        return new UserDTO(this.username, this.email, this.imageLink,
                authoritySet.stream().map(UserAuthority::getAuthorityType).collect(Collectors.toSet()));
    }

    public static User fromCredentialUserDTO(CredentialUserDTO credentialUserDTO) {
        return new User(credentialUserDTO.getUsername(), credentialUserDTO.getEmail(), credentialUserDTO.getPlainPassword(),
                credentialUserDTO.getImage(),
                credentialUserDTO.getAuthorities().stream().map(UserAuthority::new)
                        .collect(Collectors.toSet()));
    }


    private String encodePassword(String password) {
        return PasswordEncoder.getEncoder().encode(password);
    }

    public static class UserBuilder {
        private String username;
        private String email;
        private String password;
        private String imageLink;
        private Set<UserAuthority> authorities;
        private boolean active;

        public UserBuilder() {
            this.authorities = new HashSet<>();
            this.active = false;
        }

        public UserBuilder username(String username) {
            this.username = username;
            return this;
        }

        public UserBuilder email(String email) {
            this.email = email;
            return this;
        }

        public UserBuilder password(String password) {
            this.password = password;
            return this;
        }

        public UserBuilder imageLink(String imageLink) {
            this.imageLink = imageLink;
            return this;
        }

        public UserBuilder addAuthority(AuthorityType authorityType) {
            this.authorities.add(new UserAuthority(authorityType));
            return this;
        }

        public UserBuilder setActive(boolean active) {
            this.active = active;
            return this;
        }

        public User createUser() {
            User user = new User(username, email, password, imageLink, authorities);
            user.setActive(active);
            return user;
        }
    }

}

