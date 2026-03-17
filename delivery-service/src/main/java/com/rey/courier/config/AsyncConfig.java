package com.rey.courier.config;

import org.springframework.aop.interceptor.AsyncUncaughtExceptionHandler;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.AsyncConfigurer;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.retry.annotation.EnableRetry; // --- NEW ---
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import java.util.concurrent.Executor;

@Configuration
@EnableAsync
@EnableRetry // --- NEW: Tells Spring to activate the self-healing retry logic ---
public class AsyncConfig implements AsyncConfigurer { 

    @Bean(name = "smsTaskExecutor")
    public Executor smsTaskExecutor() {
        ThreadPoolTaskExecutor executor = new ThreadPoolTaskExecutor();
        executor.setCorePoolSize(3);      
        executor.setMaxPoolSize(10);      
        executor.setQueueCapacity(50);    
        executor.setThreadNamePrefix("SMS-Worker-"); 
        executor.initialize();
        return executor;
    }

    @Override
    public AsyncUncaughtExceptionHandler getAsyncUncaughtExceptionHandler() {
        return new CustomAsyncExceptionHandler();
    }
}