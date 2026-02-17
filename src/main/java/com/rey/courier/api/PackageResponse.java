package com.rey.courier.api;

import java.util.UUID;

public class PackageResponse {
    private final UUID packageId;
    private final String status;
    private final String trackingNumber;

    public PackageResponse(UUID packageId, String status, String trackingNumber) {
        this.packageId = packageId;
        this.status = status;
        this.trackingNumber = trackingNumber;
    }

    public UUID getPackageId() { return packageId; }
    public String getStatus() { return status; }
    public String getTrackingNumber() { return trackingNumber; }
}