package com.qaq.demo.models;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import java.io.IOException;
import java.util.Date;

@Slf4j
@ApiModel(value = "用户实体类")
@NoArgsConstructor
@Data
@JsonIgnoreProperties(ignoreUnknown = true)
@Document
public class User {

    @ApiModelProperty(value = "id")
    @Id
    private String id;

    @NotBlank(message = "用户名不能为空")
    @ApiModelProperty(value = "用户名", example = "李白")
    private String fullname;

    @Email
    @ApiModelProperty(value = "用户邮箱", example = "15656564262@163.com")
    private String email;

    @ApiModelProperty(hidden = true)
    private String lastModifyUser = "";

    @ApiModelProperty(hidden = true)
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date lastModifyTime = new Date();

    public User(String fullname, String email) {
        this.setFullname(fullname);
        this.setEmail(email);
    }

    public static User toUser(String jsonStr) {
        if (jsonStr == null) {
            return null;
        }
        try {
            return new ObjectMapper().readValue(jsonStr, User.class);
        } catch (IOException e) {
            log.error("JSON to User object fail.");
            log.error(e.getMessage());
            return null;
        }
    }
}
