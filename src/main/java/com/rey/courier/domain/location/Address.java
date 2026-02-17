package com.rey.courier.domain.location;

public class Address {
    private final String street;
    private final String city;
    private final String zipCode;

    // Constructor
    public Address(String street, String city, String zipCode) {
        this.street = street;
        this.city = city;
        this.zipCode = zipCode;
    }

    // Getters only (Value Objects are immutable!)
    public String getStreet() { return street; }
    public String getCity() { return city; }
    public String getZipCode() { return zipCode; }
}