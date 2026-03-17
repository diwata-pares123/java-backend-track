package com.rey.courier.event;

import java.time.LocalDateTime;

// This is the "Message" we will broadcast to the system.
public class PackageCreatedEvent {
    private final String packageId;
    private final LocalDateTime timestamp;

    public PackageCreatedEvent(String packageId) {
        this.packageId = packageId;
        this.timestamp = LocalDateTime.now();
    }

    public String getPackageId() { return packageId; }
    public LocalDateTime getTimestamp() { return timestamp; }
}