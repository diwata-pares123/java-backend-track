package com.rey.courier.api;

import com.rey.courier.application.PackageOrchestrator;
import com.rey.courier.application.PackageService;
import com.rey.courier.config.RabbitConfig;
import com.rey.courier.event.PackageCreatedEvent;
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
    
    // --- NEW: The RabbitMQ Publisher ---
    private final RabbitTemplate rabbitTemplate;

    private final Set<String> processedKeys = new HashSet<>();

    // Inject RabbitTemplate into the constructor
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
            System.out.println("♻️ [Idempotency Guard] Duplicate request detected for key: " + idempotencyKey + ". Returning safe success response.");
            return ResponseEntity.ok("Package already created (Idempotent Cache Hit)");
        }

        // 2. NORMAL PROCESSING 
        PackageResponse response = packageOrchestrator.orchestrateNewPackage(request, userToken);

        // 3. SAVE THE KEY
        processedKeys.add(idempotencyKey);

        // --- NEW: BROADCAST TO RABBITMQ ---
        // We use a random UUID here to simulate the tracking ID being broadcasted
        String mockPackageId = UUID.randomUUID().toString();
        PackageCreatedEvent event = new PackageCreatedEvent(mockPackageId);
        
        System.out.println("📻 [Main-Web-Thread] Broadcasting Event to RabbitMQ Exchange...");
        rabbitTemplate.convertAndSend(RabbitConfig.EXCHANGE_NAME, "", event);

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
            System.out.println("[SECURITY ALARM] User " + requestUserId + " attempted to modify a package they do not own!");
            return ResponseEntity.status(HttpStatus.FORBIDDEN)
                    .body("Zero-Trust Violation: You do not have permission to modify this specific package.");
        }

        String status = packageService.cancelPackage(id);
        return ResponseEntity.ok(status);
    }

    @DeleteMapping("/{id}/force-cancel")
    public ResponseEntity<String> forceCancelPackage(
            @PathVariable UUID id, 
            @RequestHeader(value = "X-User-Role", required = true) String userRole) {
        
        System.out.println("[Delivery Service] Force Cancel requested by role: " + userRole);

        if (!"ADMIN".equals(userRole.toUpperCase())) {
            System.out.println("[SECURITY ALARM] Non-admin attempted to force cancel!");
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