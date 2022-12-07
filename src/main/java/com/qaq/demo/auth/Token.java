package com.qaq.demo.auth;

import lombok.Data;

@Data
public class Token {
    private String salt;
    private String fullname;
    private String avatar;
    private long exp;
    private String userid;
    private String email;
}