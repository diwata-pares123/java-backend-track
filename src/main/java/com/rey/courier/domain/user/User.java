package com.rey.courier.domain.user;

import com.rey.courier.domain.location.Address;
import java.util.UUID;

public class User {
    private final UUID id;            // This ID makes it an Entity!
    private final String email;
    private final String passwordHash;
    private final String role;
    private Address address;          // Embedded Value Object

    public User(UUID id, String email, String passwordHash, String role, Address address) {
        this.id = id;
        this.email = email;
        this.passwordHash = passwordHash;
        this.role = role;
        this.address = address;
    }

    public UUID getId() { return id; }
    public String getEmail() { return email; }
    public String getPasswordHash() { return passwordHash; }
    public String getRole() { return role; }
    public Address getAddress() { return address; }

    // Standard practice: we can update the address, but the User ID stays the same
    public void setAddress(Address address) {
        this.address = address;
    }
}