package com.mdud.sternumauth.cdn;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.BAD_REQUEST)
public class ImageException extends RuntimeException{
    public ImageException() {
        super("file is not image");
    }
}
