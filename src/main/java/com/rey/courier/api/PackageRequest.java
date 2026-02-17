package com.rey.courier.api;

import java.util.UUID;

public class PackageRequest {
    private final UUID senderId;
    private final double weight;
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