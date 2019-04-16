package com.mdud.sternumauth.util;

import org.springframework.validation.BindingResult;

import java.util.concurrent.atomic.AtomicReference;

public class ValidationUtils {

    public static String combineBindingErrors(BindingResult bindingResult) {
        AtomicReference<String> errors = new AtomicReference<>();

        bindingResult.getFieldErrors().forEach(error -> {
            errors.set(errors.get() + error.getDefaultMessage() + "\n");
        });

        return errors.get();
    }
}
