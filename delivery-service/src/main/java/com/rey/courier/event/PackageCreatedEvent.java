package com.rey.courier.event;

import java.time.LocalDateTime;

public class PackageCreatedEvent {
    private String packageId;
    private LocalDateTime timestamp;
    private String priorityLevel; // <-- NEW DISRUPTIVE FIELD!

    public PackageCreatedEvent() {}

    public PackageCreatedEvent(String packageId) {
        this.packageId = packageId;
        this.timestamp = LocalDateTime.now();
        this.priorityLevel = "URGENT"; // The publisher sends more data now!
    }

    public String getPackageId() { return packageId; }
    public void setPackageId(String packageId) { this.packageId = packageId; }
    
    public LocalDateTime getTimestamp() { return timestamp; }
    public void setTimestamp(LocalDateTime timestamp) { this.timestamp = timestamp; }

    public String getPriorityLevel() { return priorityLevel; }
    public void setPriorityLevel(String priorityLevel) { this.priorityLevel = priorityLevel; }
}