package com.rey.courier.domain;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface PackageRepository extends JpaRepository<DeliveryPackage, UUID> {

    // 1. Unique Lookup: Translates to SELECT * FROM packages WHERE tracking_number = ?
    // Returns Optional because a tracking number might not exist (prevents NullPointerExceptions)
    Optional<DeliveryPackage> findByTrackingNumber(String trackingNumber);

    // 2. Status Filtering: Translates to SELECT * FROM packages WHERE status = ? LIMIT ? OFFSET ?
    // We use Page/Pageable here to protect our server memory!
    Page<DeliveryPackage> findByStatus(String status, Pageable pageable);

    // 3. Duplicate Protection: Translates to SELECT count(*) > 0 FROM packages WHERE tracking_number = ?
    // Extremely fast query just to check if something exists.
    boolean existsByTrackingNumber(String trackingNumber);

    // 4. Date Search: Translates to SELECT * FROM packages WHERE created_at > ?
    // Useful for "Show me all packages created since yesterday"
    List<DeliveryPackage> findByCreatedAtAfter(LocalDateTime date);
}