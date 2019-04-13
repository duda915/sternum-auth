package com.mdud.sternumauth.registration.mail;

import com.mdud.sternumauth.registration.RegistrationToken;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

@Service
public class MailServiceImpl implements MailService {

    private final JavaMailSender javaMailSender;

    @Value("${spring.sternum.auth.address}")
    private String authAddress;

    @Autowired
    public MailServiceImpl(JavaMailSender javaMailSender) {
        this.javaMailSender = javaMailSender;
    }

    @Override
    public void sendMail(String mail, RegistrationToken registrationToken) {
        SimpleMailMessage simpleMailMessage = new SimpleMailMessage();
        simpleMailMessage.setTo(mail);
        simpleMailMessage.setSubject("Sternum account activation");
        simpleMailMessage.setText("To activate account follow the link: " + authAddress + "/activate?token=" + registrationToken.getToken());

        javaMailSender.send(simpleMailMessage);
    }
}
