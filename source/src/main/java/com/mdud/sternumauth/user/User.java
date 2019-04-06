package com.mdud.sternumauth.user;

import lombok.*;

import javax.persistence.*;

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


    public User(String username, String email, String password, String imageLink) {
        this.username = username;
        this.email = email;
        setPassword(password);
        this.imageLink = imageLink;
        this.active = false;
    }

    public void setPassword(String password) {
        this.password = encodePassword(password);
    }

    private String encodePassword(String password) {
        return PasswordEncoder.getEncoder().encode(password);
    }
}
