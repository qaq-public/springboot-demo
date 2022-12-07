package com.qaq.demo.controllers;

import com.qaq.demo.auth.exception.UnAuthorizedException;
import com.qaq.demo.utils.ApiResponse;
import io.sentry.Sentry;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.support.DefaultMessageSourceResolvable;
import org.springframework.http.MediaType;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import javax.servlet.http.HttpServletRequest;

import java.util.stream.Collectors;

import static com.qaq.demo.utils.HttpContextUtils.getBodyString;

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
        return new ApiResponse<>(false, null, ex.getMessage());
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
        return ApiResponse.failureInstance(e.getMessage());
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ApiResponse<Object> handleMethodArgumentNotValidException(MethodArgumentNotValidException e) {
        log.error(e.getMessage(), e);
        String message = e.getBindingResult().getAllErrors().stream().map(DefaultMessageSourceResolvable::getDefaultMessage).collect(Collectors.joining(";"));
        return new ApiResponse<>(false, null, message, 500);
    }

}
