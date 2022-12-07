package com.qaq.demo.auth.exception;

import org.springframework.http.HttpStatus;

public abstract class GeneralHttpException extends RuntimeException {

    public GeneralHttpException(String message) {
        super(message);
    }

    public abstract HttpStatus getHttpStatus();

    public abstract int getErrCode();
}
