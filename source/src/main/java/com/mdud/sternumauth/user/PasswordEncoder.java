package com.mdud.sternumauth.user;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

public class PasswordEncoder {

    private static BCryptPasswordEncoder bCryptPasswordEncoder = new BCryptPasswordEncoder();

    public static BCryptPasswordEncoder getEncoder() {
        return  bCryptPasswordEncoder;
    }
}
