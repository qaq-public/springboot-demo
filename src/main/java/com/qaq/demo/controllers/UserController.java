package com.qaq.demo.controllers;

import com.qaq.demo.auth.CheckPermission;
import com.qaq.demo.auth.CheckType;
import com.qaq.demo.auth.Permission;
import com.qaq.demo.auth.model.Auth;
import com.qaq.demo.models.User;
import com.qaq.demo.models.repositories.UserRepository;
import com.qaq.demo.utils.ApiResponse;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;

@RestController
@Api(tags = "User")
public class UserController {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private HttpServletRequest request;

    @ApiOperation(value = "获取user")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "projectId", value = "项目ID", required = true, paramType = "query", dataType = "String", example = "G100"),
            @ApiImplicitParam(name = "id", value = "用户id", required = true, paramType = "path", dataType = "String", example = "629b7345aa054b20bc096aa3"),
    })
    @CheckPermission(value = "projectId", type = CheckType.PROJECT_ID, permission = Permission.READ)
    @GetMapping(value = "/user/{id}")
    public ApiResponse<User> getUser(@RequestParam String projectId, @PathVariable String id) {
        var userOptional = userRepository.findById(id);
        if (userOptional.isPresent()) {
            return new ApiResponse<>(true, userOptional.get(), "");
        }
        return new ApiResponse<>(true, null, "");
    }

    @ApiOperation(value = "添加user")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "projectId", value = "项目ID", required = true, paramType = "query", dataType = "String", example = "G100")
    })
    @CheckPermission(value = "projectId", type = CheckType.PROJECT_ID, permission = Permission.READ)
    @PostMapping(value = "/user")
    public ApiResponse<User> addUser(@RequestParam String projectId, @RequestBody @Valid User user) {

        Auth auth = (Auth) request.getAttribute("auth");
        user.setLastModifyUser(auth.getName());
        user.setId(null);
        userRepository.save(user);
        return new ApiResponse<>(true, user, "");
    }

}
