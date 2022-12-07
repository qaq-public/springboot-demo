package com.qaq.demo.auth.exception;

import org.springframework.http.HttpStatus;

public class UnAuthorizedException extends GeneralHttpException {
    public UnAuthorizedException(String message) {
        super(message);
    }

    @Override
    public HttpStatus getHttpStatus() {
        return HttpStatus.UNAUTHORIZED;
    }

    @Override
    public int getErrCode() {
        return getHttpStatus().value();
    }
}
