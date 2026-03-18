package com.rey.courier.event;

import java.time.LocalDateTime;

public class PackageCreatedEvent {
    private String packageId;
    private LocalDateTime timestamp;

    // IMPORTANT: Jackson needs this empty constructor to read the JSON from RabbitMQ!
    public PackageCreatedEvent() {}

    public PackageCreatedEvent(String packageId) {
        this.packageId = packageId;
        this.timestamp = LocalDateTime.now();
    }

    public String getPackageId() { return packageId; }
    public void setPackageId(String packageId) { this.packageId = packageId; }
    
    public LocalDateTime getTimestamp() { return timestamp; }
    public void setTimestamp(LocalDateTime timestamp) { this.timestamp = timestamp; }
}