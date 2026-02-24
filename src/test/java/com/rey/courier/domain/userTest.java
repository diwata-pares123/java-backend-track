package com.rey.courier.domain;

import com.rey.courier.domain.location.Address;
import com.rey.courier.domain.user.User;
import org.junit.Test;
import java.util.UUID;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

public class UserTest {

    @Test
    public void testUserAndAddressCreation() {
        // 1. Create our Value Object (Address)
        Address testAddress = new Address("123 Main St", "Manila", "1000");

        // 2. Create our Entity (User) and pass in the Address
        UUID randomId = UUID.randomUUID();
        User testUser = new User(randomId, "test@email.com", "secureHash123", "SENDER", testAddress);

        // 3. Assert (Test) that everything was wired up correctly
        assertNotNull("User ID should not be null", testUser.getId());
        assertEquals("test@email.com", testUser.getEmail());
        
        // 4. Test that the Value Object is securely embedded
        assertEquals("Manila", testUser.getAddress().getCity());
        assertEquals("1000", testUser.getAddress().getZipCode());

        System.out.println("✅ SUCCESS: User Entity and Address Value Object are working perfectly!");
    }
}