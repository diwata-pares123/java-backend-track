package com.rey.courier.application;



import org.springframework.scheduling.annotation.Async;

import org.springframework.stereotype.Service;



@Service

public class NotificationService {



    // --- NEW: We specifically assign this to our custom thread pool ---

    @Async("smsTaskExecutor") 

    public void sendDriverDispatchSms(String packageInfo) {

        

        // Notice we are using Thread.currentThread().getName() so your terminal proves it's working!

        System.out.println("[" + Thread.currentThread().getName() + "] Preparing to send SMS to driver for: " + packageInfo + "...");

        

        try {

            // Simulate slow SMS delay

            Thread.sleep(5000); 

        } catch (InterruptedException e) {

            Thread.currentThread().interrupt();

        }

        

        System.out.println("[" + Thread.currentThread().getName() + "] ✅ SMS successfully sent for: " + packageInfo);

    }

}