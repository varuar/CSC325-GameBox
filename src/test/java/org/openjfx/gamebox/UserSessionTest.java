package org.openjfx.gamebox;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.AfterEach;

import static org.junit.jupiter.api.Assertions.*;

class UserSessionTest {
    private UserSession instance;

    @BeforeEach
    void setUp() {
        UserSession.resetInstance();
        instance = UserSession.getInstance();
    }

    @AfterEach
    void tearDown() {
        UserSession.resetInstance();
    }

    @Test
    void getInstance() {
        UserSession newInstance = UserSession.getInstance();
        assertNotNull(newInstance, "Instance should not be null");
        assertSame(instance, newInstance, "Instances should be the same");
    }

    @Test
    void getUserEmail() {
        String expectedEmail = "test@example.com";
        instance.setUserEmail(expectedEmail); // Setup email to test retrieval
        String retrievedEmail = instance.getUserEmail();
        assertEquals(expectedEmail, retrievedEmail, "The retrieved email should match the set email");
    }

    @Test
    void setUserEmail() {
        String testEmail = "test@example.com";
        instance.setUserEmail(testEmail);
        assertEquals(testEmail, instance.getUserEmail(), "The email should be set correctly in the instance");
    }
}