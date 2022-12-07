package com.qaq.demo.config;

import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestTemplate;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

@Slf4j
@Configuration
public class BeanConfig {

    @Bean(name = "crawlExecutorPool")
    public ExecutorService crawlExecutorPool() {
        // 获取Java虚拟机的可用的处理器数，最佳线程个数，处理器数*2。根据实际情况调整
        int curSystemThreads = Runtime.getRuntime().availableProcessors() * 2;
        log.info("------------系统可用线程池个数：" + curSystemThreads);
        // 创建线程池
        ExecutorService pool = Executors.newFixedThreadPool(curSystemThreads);
        return pool;
    }

    @Bean
    public RestTemplate restTemplate() {
        return new RestTemplate();
    }
}
