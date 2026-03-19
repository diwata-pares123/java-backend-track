package com.rey.courier.event;

public class PackageCreatedEventV2 {
    private final String version = "2.0"; // Explicitly tag as V2
    private final String trackingNumber;
    private final String deliveryAddress;

    public PackageCreatedEventV2(String trackingNumber, String deliveryAddress) {
        this.trackingNumber = trackingNumber;
        this.deliveryAddress = deliveryAddress;
    }

    public String getVersion() { return version; }
    public String getTrackingNumber() { return trackingNumber; }
    public String getDeliveryAddress() { return deliveryAddress; }
}