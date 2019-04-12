package com.mdud.sternumauth.registration;

import com.mdud.sternumauth.user.User;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.sql.Date;
import java.util.UUID;

@Entity
@Table(name = "registration_token")
@Data
@NoArgsConstructor
public class RegistrationToken {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToOne(cascade = {CascadeType.DETACH, CascadeType.PERSIST})
    @JoinColumn(name = "user_id")
    private User user;

    @Column(name = "token")
    private String token;

    @Column(name = "registration_date")
    private Date registrationDate;

    public RegistrationToken(User user) {
        this.user = user;
        this.token = UUID.randomUUID().toString();
        this.registrationDate = new Date(new java.util.Date().getTime());
    }
}
