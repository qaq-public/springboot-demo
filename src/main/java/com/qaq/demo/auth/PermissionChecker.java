package com.qaq.demo.auth;

import com.qaq.demo.auth.exception.UnAuthorizedException;
import com.qaq.demo.auth.model.Auth;
import org.springframework.stereotype.Component;

@Component
public class PermissionChecker {

    private void check(Auth auth, String projectId, Permission permissionNeed) {
        if (!auth.havePermission(projectId, permissionNeed)) {
            String msg = String.format("[%s][%s] no permission, need permissionNeed %s of project %s",
                    auth.getEmail(), auth.getName(),
                    permissionNeed, projectId);
            throw new UnAuthorizedException(msg);
        }
    }

    // check with project id
    public void checkWithProjectId(Auth auth, Permission permission, String projectId) {
        check(auth, projectId, permission);
    }
}
