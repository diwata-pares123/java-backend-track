package com.rey.courier.domain.repository;

import com.rey.courier.domain.DeliveryPackage; // This was failing because the file was missing!
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.UUID;

@Repository
public interface PackageRepository extends JpaRepository<DeliveryPackage, UUID> {
}