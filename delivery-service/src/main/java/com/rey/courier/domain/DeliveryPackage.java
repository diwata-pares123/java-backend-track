package com.rey.courier.domain;

import jakarta.persistence.*;
import java.util.UUID;

@Entity
@Table(name = "packages")
public class DeliveryPackage {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    private String destinationAddress;
    private Double weight;
    
    // NEW: Added status field for our Saga compensating action
    private String status; 

    // 🚨 THE MICROSERVICE FIX: We no longer store the whole User object or use Foreign Keys.
    // We only store the UUID of the sender!
    @Column(name = "sender_id")
    private UUID senderId;

    public DeliveryPackage() {}

    public UUID getId() { return id; }
    public void setId(UUID id) { this.id = id; }
    public String getDestinationAddress() { return destinationAddress; }
    public void setDestinationAddress(String destinationAddress) { this.destinationAddress = destinationAddress; }
    public Double getWeight() { return weight; }
    public void setWeight(Double weight) { this.weight = weight; }
    
    // NEW: Getters and Setters for status
    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }

    // 🚨 THE MICROSERVICE FIX: Updated Getters/Setters for senderId
    public UUID getSenderId() { return senderId; }
    public void setSenderId(UUID senderId) { this.senderId = senderId; }
}