package com.mdud.sternumauth.registration.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.BAD_REQUEST)
public class TokenNotFoundException extends RuntimeException {
    public TokenNotFoundException() {
        super("token not found");
    }
}
