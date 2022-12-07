package com.qaq.demo.auth;

import java.util.Arrays;

import javax.servlet.http.HttpServletRequest;

import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.CodeSignature;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import com.qaq.demo.auth.model.Auth;

@Aspect
@Component
public class PermissionCheckAspect {

    @Autowired
    private PermissionChecker permissionChecker;

    @Pointcut("@annotation(checkPermission) && execution(* *(..))")
    public void readCheckerPoint(CheckPermission checkPermission) {
    }

    @Before("readCheckerPoint(checkPermission)")
    public void advice(JoinPoint joinPoint, CheckPermission checkPermission) {
        HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder.currentRequestAttributes())
                .getRequest();
        Auth auth = (Auth) request.getAttribute("auth");
        String val = checkPermission.value();
        String id = val.isEmpty() ? null : getIdFromMethodArgs(joinPoint, val);
        Permission permissionNeed = checkPermission.permission();
        CheckType checkType = checkPermission.type();
        switch (checkType) {
            case PROJECT_ID:
                permissionChecker.checkWithProjectId(auth, permissionNeed, id);
                break;
            default:
                throw new RuntimeException("not define this checkType!!!");
        }
    }

    private String getIdFromMethodArgs(JoinPoint joinPoint, String val) {
        CodeSignature codeSignature = (CodeSignature) joinPoint.getSignature();
        String[] paramNames = codeSignature.getParameterNames();
        int idx = Arrays.asList(paramNames).indexOf(val);
        if (idx == -1) {
            throw new RuntimeException("the key not find in method parameters");
        }
        return (String) joinPoint.getArgs()[idx];
    }

}
