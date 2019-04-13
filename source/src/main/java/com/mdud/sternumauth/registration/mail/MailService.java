package com.mdud.sternumauth.registration.mail;

import com.mdud.sternumauth.registration.RegistrationToken;

public interface MailService {
    void sendMail(String mail, RegistrationToken registrationToken);
}
