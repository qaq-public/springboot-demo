package com.qaq.demo.auth;

import com.fasterxml.jackson.annotation.JsonCreator;

public enum Permission {
    DELETE("delete"),
    DELETEALL("deleteAll"),
    READ("read"),
    READALL("deleteAll"),
    WRITE("write"),
    WRITEALL("writeAll"),
    ;

    private String val;

    Permission(String val) {
        this.val = val;
    }

    @JsonCreator
    public static Permission fromString(String permissionStr) {
        System.out.println(permissionStr);
        for (Permission permission : Permission.values()) {
            if (permission.val.equals(permissionStr)) {
                return permission;
            }
        }
        return null;
    }
}