package com.qaq.demo.controllers;

import static com.qaq.base.utils.HttpContextUtils.getBodyString;

import java.util.stream.Collectors;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.support.DefaultMessageSourceResolvable;
import org.springframework.http.MediaType;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import com.qaq.base.response.ApiResponse;
import com.qaq.demo.auth.exception.UnAuthorizedException;

import io.sentry.Sentry;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RestControllerAdvice
public class ExceptionController {

    @Value("${sentry.flag}")
    private Boolean sentryFlag;

    @Autowired
    private HttpServletRequest request;

    @ExceptionHandler(UnAuthorizedException.class)
    public ApiResponse<Object> errorMessageResponse(UnAuthorizedException ex) {
        log.error(ex.toString());
        return new ApiResponse<>(false, 0, ex.getMessage(), null);
    }

    @ExceptionHandler(Exception.class)
    public ApiResponse<Object> handleException(Exception e) {
        e.printStackTrace();
        log.error(e.getMessage());
        if (sentryFlag) {
            String contentType = request.getContentType();
            // 只处理@RequestBody数据
            if (!contentType.isEmpty() && contentType.contains(MediaType.APPLICATION_JSON_VALUE)) {
                Sentry.configureScope(scope -> {
                    scope.setContexts("Body", getBodyString(request));
                });
            }

            Sentry.captureException(e);
        }
        return new ApiResponse<>(false, -1, e.getMessage(), null);
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ApiResponse<Object> handleMethodArgumentNotValidException(MethodArgumentNotValidException e) {
        log.error(e.getMessage(), e);
        String message = e.getBindingResult().getAllErrors().stream()
                .map(DefaultMessageSourceResolvable::getDefaultMessage).collect(Collectors.joining(";"));
        return new ApiResponse<>(false, 500, message, null);
    }

}
