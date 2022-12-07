package com.qaq.demo.utils;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import lombok.AllArgsConstructor;
import lombok.Data;

@AllArgsConstructor
@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class ApiResponse<T> {

    private boolean success;

    private T data;

    private String message = "";

    private int code;

    public static <T> ApiResponse<T> successInstance(T data) {
        return new ApiResponse<>(true, data, null, 0);
    }

    public static ApiResponse<Object> failureInstance(String message, int errorCode) {
        return new ApiResponse<>(false, null, message, errorCode);
    }

    public static ApiResponse<Object> failureInstance(String message) {
        return new ApiResponse<>(false, null, message, 0);
    }

    public ApiResponse(T data) {
        this.success = true;
        this.data = data;
    }

    public ApiResponse(boolean success, T data, String message) {
        this.success = success;
        this.message = message;
        this.data = data;
    }

}