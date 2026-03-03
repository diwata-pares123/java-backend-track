package com.rey.courier.api;

import java.util.UUID;

public class PackageResponse {
    private UUID id;
    private String destinationAddress;
    private Double weight;

    public PackageResponse(UUID id, String destinationAddress, Double weight) {
        this.id = id;
        this.destinationAddress = destinationAddress;
        this.weight = weight;
    }

    // Getters
    public UUID getId() { return id; }
    public String getDestinationAddress() { return destinationAddress; }
    public Double getWeight() { return weight; }
}