package com.rey.courier.listener;

import com.rey.courier.config.RabbitConfig;
import com.rey.courier.event.PackageCreatedEvent;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

@Component
public class CourierMessageListeners {

    @RabbitListener(queues = RabbitConfig.NOTIFICATION_QUEUE)
    public void handleNotification(NotificationEventContract event) {
        
        // Check the version tag!
        if ("2.0".equals(event.getVersion())) {
            System.out.println("🚀 [Notification-Service] Processing V2 Event!");
            System.out.println("   -> Tracking Number: " + event.getTrackingNumber());
            System.out.println("   -> Dispatching driver to: " + event.getDeliveryAddress());
        } else {
            System.out.println("🐢 [Notification-Service] Processing Legacy V1 Event.");
            System.out.println("   -> Package ID: " + event.getPackageId());
            System.out.println("   -> Address unknown. Sending generic SMS.");
        }
    }

    @RabbitListener(queues = RabbitConfig.ANALYTICS_QUEUE)
    public void handleAnalytics(PackageCreatedEvent event) {
        System.out.println("📊 [Analytics-Service] Received Package ID: " + event.getPackageId());
        System.out.println("📊 [Analytics-Service] Updating global delivery metrics...");
    }
}