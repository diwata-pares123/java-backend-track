package com.rey.courier.api;

import com.rey.courier.application.PackageOrchestrator;
import com.rey.courier.application.PackageService;
import com.rey.courier.config.RabbitConfig;
import com.rey.courier.event.PackageCreatedEventV2; // IMPORT V2 EVENT
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.UUID; 

@RestController
@RequestMapping("/api/v1/packages")
public class PackageController {

    private final PackageOrchestrator packageOrchestrator;
    private final PackageService packageService;
    private final RabbitTemplate rabbitTemplate;

    private final Set<String> processedKeys = new HashSet<>();

    public PackageController(PackageOrchestrator packageOrchestrator, PackageService packageService, RabbitTemplate rabbitTemplate) {
        this.packageOrchestrator = packageOrchestrator;
        this.packageService = packageService;
        this.rabbitTemplate = rabbitTemplate;
    }

    @PostMapping 
    public ResponseEntity<Object> createPackage(
            @RequestBody PackageRequest request,
            @RequestHeader(value = "Authorization", required = false) String userToken,
            @RequestHeader(value = "Idempotency-Key", required = true) String idempotencyKey) {

        // 1. CHECK IDEMPOTENCY
        if (processedKeys.contains(idempotencyKey)) {
            System.out.println("♻️ [Idempotency Guard] Duplicate request detected for key: " + idempotencyKey);
            return ResponseEntity.ok("Package already created (Idempotent Cache Hit)");
        }

        // 2. NORMAL PROCESSING 
        PackageResponse response = packageOrchestrator.orchestrateNewPackage(request, userToken);

        // 3. SAVE THE KEY
        processedKeys.add(idempotencyKey);

        // --- UPDATED: BROADCAST V2 EVENT TO RABBITMQ ---
        // We generate a mock tracking number and pull the address from the request
        String mockTracking = "TRK-" + UUID.randomUUID().toString().substring(0, 8).toUpperCase();
        PackageCreatedEventV2 v2Event = new PackageCreatedEventV2(mockTracking, request.getDestinationAddress());
        
        System.out.println("📻 [Main-Web-Thread] Broadcasting V2 Event to RabbitMQ Exchange...");
        rabbitTemplate.convertAndSend(RabbitConfig.EXCHANGE_NAME, "", v2Event);

        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @GetMapping("/{id}/status")
    public ResponseEntity<String> getPackageStatus(@PathVariable UUID id) {
        String status = packageService.checkPackageFinalStatus(id);
        return ResponseEntity.ok(status);
    }

    @PostMapping("/{id}/cancel")
    public ResponseEntity<String> cancelPackage(
            @PathVariable UUID id,
            @RequestHeader(value = "X-User-Id", required = true) String requestUserId,
            @RequestHeader(value = "X-Simulated-DB-Owner-Id", required = true) String packageOwnerId) {

        System.out.println("[Zero-Trust Policy] Verifying data ownership for Package: " + id);

        if (!requestUserId.equals(packageOwnerId)) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN)
                    .body("Zero-Trust Violation: Access Denied.");
        }

        String status = packageService.cancelPackage(id);
        return ResponseEntity.ok(status);
    }

    @DeleteMapping("/{id}/force-cancel")
    public ResponseEntity<String> forceCancelPackage(
            @PathVariable UUID id, 
            @RequestHeader(value = "X-User-Role", required = true) String userRole) {
        
        if (!"ADMIN".equals(userRole.toUpperCase())) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body("Access Denied: Admins Only");
        }

        packageService.updatePackageStatus(id, "CANCELLED_BY_ADMIN");
        return ResponseEntity.ok("Package " + id + " forcefully cancelled by Admin.");
    }

    @GetMapping
    public List<PackageResponse> getAllPackages(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {
        return packageService.getAllPackages(page, size);
    }
}