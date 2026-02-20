package com.rey.courier.domain; // Remember to move this out of the 'delivery' subfolder!

import jakarta.persistence.*;
import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Table(name = "packages")
public class DeliveryPackage {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID packageId;

    @Column(unique = true, nullable = false)
    private String trackingNumber;

    @Column(nullable = false)
    private String status;

    @Column(name = "created_at", updatable = false)
    private LocalDateTime createdAt;

    // ✅ M4: NEW - The Many-to-One relationship
    // Many packages belong to one Sender (User)
    @ManyToOne(fetch = FetchType.LAZY) 
    @JoinColumn(name = "sender_id", nullable = false) 
    private User sender;

    protected DeliveryPackage() {
    }

    // ✅ M4: Updated constructor to require a sender
    public DeliveryPackage(String trackingNumber, String status, User sender) {
        this.trackingNumber = trackingNumber;
        this.status = status;
        this.sender = sender; // Link the user here
        this.createdAt = LocalDateTime.now();
    }

    // Getters and Setters
    public UUID getPackageId() { return packageId; }
    public String getTrackingNumber() { return trackingNumber; }
    public String getStatus() { return status; }
    public LocalDateTime getCreatedAt() { return createdAt; }
    
    // ✅ M4: NEW - Getter for the sender
    public User getSender() { return sender; }
}