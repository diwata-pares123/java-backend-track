package com.rey.courier.api.validation;

import com.rey.courier.service.DeliveryService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/simulation")
public class SimulationController {

    private final DeliveryService deliveryService;

    public SimulationController(DeliveryService deliveryService) {
        this.deliveryService = deliveryService;
    }

    @GetMapping("/fail")
    public String triggerFailure() {
        deliveryService.processDelivery(false);
        return "Failure simulated! Check /actuator/prometheus now.";
    }
}