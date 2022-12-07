package com.qaq.demo.services;

import com.qaq.demo.utils.ApiResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpMethod;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.List;

@Service
public class NotifyService {

    @Value("${notify.url}")
    private String messageUrl;

    @Value("${notify.url}")
    private String emailUrl;

    @Autowired
    private HTTPService httpService;

    @Async
    public ApiResponse notify(String content, String username) {
        return httpService.exchange(messageUrl, HttpMethod.POST, new Message(content, "markdown", Arrays.asList(username)), ApiResponse.class);

    }

    @Async
    public String sendEmail(MailRequest mailRequest) {
        return httpService.exchange(emailUrl, HttpMethod.POST, mailRequest, String.class);
    }

    public record MailRequest(
            List<String> touser,
            List<String> recipients,
            String subject,
            String content,
            String senderAddress) {
    }

    public record Message(
            String content,
            String msgtype,
            List<String> touser) {
    }

}
