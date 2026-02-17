package com.rey.courier.domain.delivery;
import java.time.LocalDateTime;
import java.util.UUID;

public class DeliveryPackage {
    private final UUID id;
    private final UUID senderUserId;
    private final String status;
    private final LocalDateTime createdAt;

    public DeliveryPackage(UUID id, UUID senderUserId, String status, LocalDateTime createdAt) {
        this.id = id;
        this.senderUserId = senderUserId;
        this.status = status;
        this.createdAt = createdAt;
    }

    public UUID getId() { return id; }
    public UUID getSenderUserId() { return senderUserId; }
    public String getStatus() { return status; }
    public LocalDateTime getCreatedAt() { return createdAt; }
}