package com.qaq.demo.auth.model;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.qaq.demo.auth.Permission;

import lombok.Data;

@Data
public class Auth {

    private String email;
    private String name;
    private String userid;
    private List<Permission> all = new ArrayList<>();;
    private Map<String, List<Permission>> project = new HashMap<>();

    public boolean havePermission(String projectId, Permission permission) {
        return (all.contains(permission) || project.getOrDefault(projectId, new ArrayList<>()).contains(permission));
    }
}
