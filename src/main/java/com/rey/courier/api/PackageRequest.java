package com.rey.courier.api;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Size;
import java.util.UUID;

public class PackageRequest {

    @NotNull(message = "Sender ID is required")
    private UUID senderId;

    @NotBlank(message = "Destination address cannot be empty")
    @Size(min = 10, max = 200, message = "Address must be between 10 and 200 characters")
    private String destinationAddress;

    // 🔥 NEW: The missing weight validation
    @NotNull(message = "Weight is required")
    @Positive(message = "Weight must be greater than zero")
    private Double weight;

    public PackageRequest() {}

    // ... (Getters and Setters for senderId and destinationAddress) ...

    public Double getWeight() { return weight; }
    public void setWeight(Double weight) { this.weight = weight; }
}