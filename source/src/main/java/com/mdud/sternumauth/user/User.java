package com.mdud.sternumauth.user;

import lombok.*;

import javax.persistence.*;
import java.util.Arrays;
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
        return new UserDTO(this.username, this.password, this.email, this.imageLink,
                authoritySet.stream().map(auth -> auth.getAuthorityType()).collect(Collectors.toSet()));
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

        public UserBuilder authority(AuthorityType authorityType) {
            this.authorities = new HashSet<>();
            switch (authorityType) {
                case ADMIN:
                    Arrays.asList(AuthorityType.values()).forEach(aType -> authorities.add(new UserAuthority(aType)));
                    break;
                case MANAGER:
                    authorities.add(new UserAuthority(AuthorityType.MANAGER));
                    authorities.add(new UserAuthority(AuthorityType.USER));
                    break;
                case USER:
                    authorities.add(new UserAuthority(AuthorityType.USER));
            }

            return this;
        }

        public User createUser() {
            return new User(username, email, password, imageLink, authorities);
        }
    }

}

