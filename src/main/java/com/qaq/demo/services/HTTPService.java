package com.qaq.demo.services;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.util.MimeType;
import org.springframework.util.MimeTypeUtils;
import org.springframework.web.client.RestTemplate;

import java.nio.charset.StandardCharsets;

/**
 * http请求工具类
 */
@Slf4j
@Service
public class HTTPService {
    @Autowired
    private RestTemplate restTemplate;

    public <T, B> T exchange(String url, HttpMethod method, B body, Class<T> bodyType) {
        HttpHeaders headers = new HttpHeaders();
        MimeType mimeType = MimeTypeUtils.parseMimeType("application/json");
        MediaType mediaType = new MediaType(mimeType.getType(), mimeType.getSubtype(), StandardCharsets.UTF_8);
        headers.setContentType(mediaType);
        HttpEntity<B> entity = new HttpEntity<>(body, headers);
        ResponseEntity<T> resultEntity = this.restTemplate.exchange(url, method, entity, bodyType);
        T response = resultEntity.getBody();
        log.info("response {} request: {} {}", (response == null) ? null : response.toString(), method, url);
        return response;
    }

    public <T, B> T exchangeWithAuth(String url, HttpMethod method, B body, Class<T> bodyType) {
        HttpHeaders headers = new HttpHeaders();
        MimeType mimeType = MimeTypeUtils.parseMimeType("application/json");
        MediaType mediaType = new MediaType(mimeType.getType(), mimeType.getSubtype(), StandardCharsets.UTF_8);
        headers.setContentType(mediaType);
        headers.set("Authorization",
                "Bearer eyJ0eXAiOiJKV1QiLCJhbGciOiJSUzI1NiJ9.eyJmdWxsbmFtZSI6IuadjueZvSIsImV4cCI6MTY3NzEzMzk1NiwidXNlcmlkIjoiMDE5Mzg0IiwiZW1haWwiOiJsaWZhamluQGJpbGliaWxpLmNvbSJ9.jVSsgLB5_Q0sN71wB8JL-_Uk0AP7njNuSMtqA34zp_sfl5BLKfy77vlhKLzg8yguPXoSYLLlbdfc6PzUi0XApIkOMYwzD0rY3h2Xe5-LOvubBvRmzERqR5FodF3sFI076135KVknye5rsFkbLr4v3s-EVzTki5IfTT3FiTHVRHg");
        HttpEntity<B> entity = new HttpEntity<>(body, headers);
        ResponseEntity<T> resultEntity = this.restTemplate.exchange(url, method, entity, bodyType);
        T response = resultEntity.getBody();
        log.info("request: {} {}", method, url);
        return response;
    }
}
