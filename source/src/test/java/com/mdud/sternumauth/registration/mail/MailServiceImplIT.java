package com.mdud.sternumauth.registration.mail;

import com.mdud.sternumauth.registration.RegistrationToken;
import com.mdud.sternumauth.user.User;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import static org.junit.Assert.*;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest
public class MailServiceImplIT {

    @Autowired
    private MailServiceImpl mailService;

    @Test
    public void sendMail() {
        RegistrationToken registrationToken = new RegistrationToken(new User());
        mailService.sendMail("test@example.com", registrationToken);
    }
}