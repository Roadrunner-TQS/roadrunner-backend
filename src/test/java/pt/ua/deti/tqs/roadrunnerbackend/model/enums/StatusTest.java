package pt.ua.deti.tqs.roadrunnerbackend.model.enums;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

class StatusTest {
    @Test
    @DisplayName("Test contains")
    void testContains() {
        assertTrue(Status.contains("PENDING"));
        assertTrue(Status.contains("SHIPPING"));
        assertTrue(Status.contains("INTRANSIT"));
        assertTrue(Status.contains("AVAILABLE"));
        assertTrue(Status.contains("DELIVERED"));
        assertTrue(Status.contains("RETURNED"));
        assertTrue(Status.contains("DENIED"));
        assertTrue(Status.contains("FORGOTTEN"));
        assertTrue(Status.contains("CANCELLED"));
        assertFalse(Status.contains("NOTHING"));
        assertFalse(Status.contains(" "));
    }
}