package com.example.courier.controller;

import com.example.courier.dto.PackageRequest;
import com.example.courier.dto.PackageResponse;
import com.example.courier.service.PackageService;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
// ✅ M4: Base path updated to /api/v1/packages.
//    All endpoints in this controller inherit this prefix automatically.
//    Old path (/api/packages) is now GONE — this is a fresh V1 contract.
@RequestMapping("/api/v1/packages")
public class PackageController {

    private final PackageService packageService;

    public PackageController(PackageService packageService) {
        this.packageService = packageService;
    }

    // POST /api/v1/packages
    @PostMapping
    public ResponseEntity<PackageResponse> createPackage(@RequestBody PackageRequest request) {
        PackageResponse response = packageService.createPackage(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    // GET /api/v1/packages/{id}
    @GetMapping("/{id}")
    public ResponseEntity<PackageResponse> getPackageById(@PathVariable Long id) {
        PackageResponse response = packageService.getPackageById(id);
        return ResponseEntity.ok(response);
    }

    // GET /api/v1/packages?page=0&size=10
    // ✅ M3 safeguard: size is capped at 100 in the service/controller layer.
    //    Never trust the frontend to send a sane value.
    @GetMapping
    public ResponseEntity<Page<PackageResponse>> getAllPackages(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {

        int limitedSize = Math.min(size, 100); // Guard against "Jumbo Requests"
        Page<PackageResponse> packages = packageService.getAllPackages(page, limitedSize);
        return ResponseEntity.ok(packages);
    }

    // PUT /api/v1/packages/{id}
    @PutMapping("/{id}")
    public ResponseEntity<PackageResponse> updatePackage(
            @PathVariable Long id,
            @RequestBody PackageRequest request) {
        PackageResponse response = packageService.updatePackage(id, request);
        return ResponseEntity.ok(response);
    }

    // DELETE /api/v1/packages/{id}
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletePackage(@PathVariable Long id) {
        packageService.deletePackage(id);
        return ResponseEntity.noContent().build();
    }
}