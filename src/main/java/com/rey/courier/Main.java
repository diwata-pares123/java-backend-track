package com.rey.courier;

import com.rey.courier.application.PackageService;
import com.rey.courier.api.PackageRequest;
import com.rey.courier.api.PackageResponse;
import java.util.UUID;

public class Main {
    public static void main(String[] args) {
        // 1. Wiring: Instantiate the service
        PackageService packageService = new PackageService();

        // 2. Data: Create a dummy request
        PackageRequest myRequest = new PackageRequest(
            UUID.randomUUID(), 
            5.5, 
            "123 Java Lane, Manila"
        );

        // 3. Execution: Run the use case
        PackageResponse response = packageService.registerNewPackage(myRequest);

        // 4. Output: Print the result to the console
        System.out.println("--- Courier System Initialized ---");
        System.out.println("Package Registered Successfully!");
        System.out.println("Tracking Number: " + response.getTrackingNumber());
        System.out.println("Status: " + response.getStatus());
    }
}