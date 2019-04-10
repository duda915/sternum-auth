package com.mdud.sternumauth.user;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.BAD_REQUEST)
public class UserAlreadyExistsException extends RuntimeException {
    public UserAlreadyExistsException(Type type) {
        super(type.toString() + " already exists");
    }

    public enum Type {
        Username, Email
    }
}
