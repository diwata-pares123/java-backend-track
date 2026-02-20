package com.rey.courier.domain;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.UUID;

@Repository // Marks this as a Data Access Component
public interface PackageRepository extends JpaRepository<DeliveryPackage, UUID> {
    // By extending JpaRepository, we get save(), findAll(), findById(), and delete() for free!
}