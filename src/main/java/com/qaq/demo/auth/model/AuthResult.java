package com.qaq.demo.auth.model;

import java.util.List;
import java.util.Map;

import com.qaq.demo.auth.Permission;

import lombok.Data;

@Data
public class AuthResult {
    private List<Permission> app;
    private Map<String, List<Permission>> project;
}
