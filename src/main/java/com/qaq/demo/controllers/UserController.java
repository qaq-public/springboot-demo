package com.qaq.demo.controllers;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.qaq.base.response.ApiResponse;
import com.qaq.demo.auth.CheckPermission;
import com.qaq.demo.auth.CheckType;
import com.qaq.demo.auth.Permission;
import com.qaq.demo.auth.model.Auth;
import com.qaq.demo.models.User;
import com.qaq.demo.models.repositories.UserRepository;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;

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
            return new ApiResponse<>(true, 0, "", userOptional.get());
        }
        return new ApiResponse<>(true, 0, "", null);
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
        return new ApiResponse<>(true, 0, "", user);
    }

}
