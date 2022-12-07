package com.qaq.demo.auth;

import com.qaq.demo.auth.exception.UnAuthorizedException;
import com.qaq.demo.auth.model.Auth;
import com.qaq.demo.auth.model.AuthResult;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.NonNull;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Arrays;
import java.util.Base64;
import java.util.List;

@Slf4j
@Component
public class AuthInterceptor implements HandlerInterceptor {

    private static final Base64.Decoder base64UrlDecoder = Base64.getUrlDecoder();

    @Value("${fakeAuth}")
    private boolean fakeCheck;

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) {
        Auth auth = new Auth();
        if (fakeCheck) {
            List<Permission> adminPermissions = Arrays.asList(Permission.READ, Permission.WRITE, Permission.DELETE);
            auth.setAll(adminPermissions);
            auth.setEmail("15656564262@163.com");
            auth.setName("李白");
        } else {
            var tokenStr = request.getHeader("X-Gateway-Token");
            var permissionStr = request.getHeader("X-Gateway-Permission");
            auth = getAuth(tokenStr, permissionStr);
        }
        request.setAttribute("auth", auth);
        return true;
    }

    @Override
    public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler,
            ModelAndView modelAndView) {
    }

    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) {
    }

    private Auth getAuth(@NonNull String tokenStr, @NonNull String permissionStr) {
        var auth = new Auth();
        var objectMapper = new ObjectMapper();
        // Auth 信息一定要有
        if (!tokenStr.trim().isEmpty()) {
            try {
                var token = objectMapper.readValue(base64UrlDecoder.decode(tokenStr), Token.class);
                auth.setName(token.getFullname());
                auth.setEmail(token.getEmail());
                auth.setUserid(token.getUserid());
            } catch (Exception e) {
                log.error("decode the X-Gateway-Token header failed. string: {}, ex:{}", tokenStr, e);
                e.printStackTrace();
                throw new UnAuthorizedException("the auth get from gateway header decode failed");
            }
        } else {
            log.error("can not get auth info from request header");
            throw new UnAuthorizedException("the auth get from gateway header decode failed");
        }
        if (!permissionStr.trim().isEmpty()) {
            try {
                AuthResult authResult = objectMapper.readValue(base64UrlDecoder.decode(permissionStr),
                        AuthResult.class);
                auth.setAll(authResult.getApp());
                auth.setProject(authResult.getProject());
            } catch (Exception e) {
                log.error("decode the X-Gateway-Permission header failed. string: {}, ex:{}", permissionStr, e);
                e.printStackTrace();
                throw new UnAuthorizedException("the auth get from gateway header decode failed");
            }
        } else {
            log.warn("the value of permission header is empty");
            throw new UnAuthorizedException("the auth get from gateway header decode failed");
        }
        return auth;
    }
}
