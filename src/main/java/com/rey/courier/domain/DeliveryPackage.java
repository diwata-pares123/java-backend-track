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

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "sender_id")
    private User sender;

    public DeliveryPackage() {}

    public UUID getId() { return id; }
    public void setId(UUID id) { this.id = id; }
    public String getDestinationAddress() { return destinationAddress; }
    public void setDestinationAddress(String destinationAddress) { this.destinationAddress = destinationAddress; }
    public Double getWeight() { return weight; }
    public void setWeight(Double weight) { this.weight = weight; }
    public User getSender() { return sender; }
    public void setSender(User sender) { this.sender = sender; }
}