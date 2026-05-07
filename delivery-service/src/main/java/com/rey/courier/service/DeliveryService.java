package com.rey.courier.service;

import io.micrometer.core.instrument.MeterRegistry;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

@Service
public class DeliveryService {

    private static final Logger log = LoggerFactory.getLogger(DeliveryService.class);
    private final MeterRegistry meterRegistry;

    
    public DeliveryService(MeterRegistry meterRegistry) {
        this.meterRegistry = meterRegistry;
    }

    public void processDelivery(boolean isSuccess) {
        if (!isSuccess) {
            
            meterRegistry.counter("business.delivery.failures", "type", "system_error").increment();
            log.error(" Delivery failed! Metric incremented.");
        } else {
            log.info(" Delivery successful!");
        }
    }
}