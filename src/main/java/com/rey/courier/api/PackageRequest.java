package com.rey.courier.api;

import jakarta.validation.constraints.*;
import java.util.UUID;

public class PackageRequest {
    @NotNull(message = "Sender ID is required")
    private UUID senderId;

    @NotBlank(message = "Destination address cannot be empty")
    private String destinationAddress;

    @NotNull(message = "Weight is required")
    @Positive(message = "Weight must be greater than zero")
    private Double weight;

    public PackageRequest() {}

    public UUID getSenderId() { return senderId; }
    public void setSenderId(UUID senderId) { this.senderId = senderId; }

    // This method was missing and causing the "cannot find symbol" error
    public String getDestinationAddress() { return destinationAddress; }
    public void setDestinationAddress(String destinationAddress) { this.destinationAddress = destinationAddress; }

    public Double getWeight() { return weight; }
    public void setWeight(Double weight) { this.weight = weight; }
}