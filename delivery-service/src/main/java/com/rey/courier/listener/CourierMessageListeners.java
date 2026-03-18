package com.rey.courier.listener;

import com.rey.courier.config.RabbitConfig;
import com.rey.courier.event.PackageCreatedEvent;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

@Component
public class CourierMessageListeners {

    @RabbitListener(queues = RabbitConfig.NOTIFICATION_QUEUE)
    public void handleNotification(PackageCreatedEvent event) {
        System.out.println("📱 [Notification-Service] Received Package ID: " + event.getPackageId());
        System.out.println("📱 [Notification-Service] Sending Dispatch SMS...");
    }

    @RabbitListener(queues = RabbitConfig.ANALYTICS_QUEUE)
    public void handleAnalytics(PackageCreatedEvent event) {
        System.out.println("📊 [Analytics-Service] Received Package ID: " + event.getPackageId());
        System.out.println("📊 [Analytics-Service] Updating global delivery metrics...");
    }
}