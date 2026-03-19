package com.rey.courier.listener;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)
public class NotificationEventContract {
    
    // Default to 1.0. If the publisher sends a V2 message, Jackson will overwrite this!
    private String version = "1.0"; 
    
    // V1 Field (Legacy)
    private String packageId;       
    
    // V2 Fields (New)
    private String trackingNumber;  
    private String deliveryAddress;

    public NotificationEventContract() {}

    public String getVersion() { return version; }
    public void setVersion(String version) { this.version = version; }

    public String getPackageId() { return packageId; }
    public void setPackageId(String packageId) { this.packageId = packageId; }

    public String getTrackingNumber() { return trackingNumber; }
    public void setTrackingNumber(String trackingNumber) { this.trackingNumber = trackingNumber; }

    public String getDeliveryAddress() { return deliveryAddress; }
    public void setDeliveryAddress(String deliveryAddress) { this.deliveryAddress = deliveryAddress; }
}