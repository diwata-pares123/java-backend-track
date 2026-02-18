package com.rey.courier.api;

// NEW IMPORTS for validation
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import java.util.UUID;

public class PackageRequest {

    @NotNull(message = "Sender ID cannot be null")
    private final UUID senderId;

    @Positive(message = "Weight must be greater than zero")
    private final double weight;

    @NotBlank(message = "Destination address cannot be empty")
    private final String destinationAddress;

    public PackageRequest(UUID senderId, double weight, String destinationAddress) {
        this.senderId = senderId;
        this.weight = weight;
        this.destinationAddress = destinationAddress;
    }

    public UUID getSenderId() { return senderId; }
    public double getWeight() { return weight; }
    public String getDestinationAddress() { return destinationAddress; }
}