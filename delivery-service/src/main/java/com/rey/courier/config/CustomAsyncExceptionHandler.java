package com.rey.courier.config;

import org.springframework.aop.interceptor.AsyncUncaughtExceptionHandler;
import java.lang.reflect.Method;

public class CustomAsyncExceptionHandler implements AsyncUncaughtExceptionHandler {

    @Override
    public void handleUncaughtException(Throwable ex, Method method, Object... params) {
        System.err.println("🚨 [CRITICAL BACKGROUND FAILURE] 🚨");
        System.err.println("Exception message: " + ex.getMessage());
        System.err.println("Method name: " + method.getName());
        for (Object param : params) {
            System.err.println("Parameter value: " + param);
        }
        System.err.println("🚨 [ACTION REQUIRED: Send to Dead Letter Queue or Alert Admin!] 🚨");
    }
}