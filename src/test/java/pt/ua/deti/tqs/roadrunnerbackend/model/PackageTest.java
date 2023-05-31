package pt.ua.deti.tqs.roadrunnerbackend.model;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import pt.ua.deti.tqs.roadrunnerbackend.model.enums.Status;

import static org.junit.jupiter.api.Assertions.*;

class PackageTest {

    private Package p;

    @BeforeEach
    void setUp() {
        p = new Package();
    }

    @Test
    @DisplayName("Check if the status CANCELLED can't be updated")
    void testStatusCanBeUpdated_whenStateIsCANCELLED() {
        p.setStatus(Status.CANCELLED);
        assertEquals(Status.CANCELLED, p.getStatus());
        assertFalse(p.canBeUpdated());
    }

    @Test
    @DisplayName("Check if the status RETURNED can't be updated")
    void testStatusCanBeUpdated_whenStateIsRETURNED() {
        p.setStatus(Status.RETURNED);
        assertEquals(Status.RETURNED, p.getStatus());
        assertFalse(p.canBeUpdated());
    }

    @Test
    @DisplayName("Check if the status DENIED can't be updated")
    void testStatusCanBeUpdated_whenStateIsDENIED() {
        p.setStatus(Status.DENIED);
        assertEquals(Status.DENIED, p.getStatus());
        assertFalse(p.canBeUpdated());
    }

    @Test
    @DisplayName("Check if the status FORGOTTEN can't be updated")
    void testStatusCanBeUpdated_whenStateIsFORGOTTEN() {
        p.setStatus(Status.FORGOTTEN);
        assertEquals(Status.FORGOTTEN, p.getStatus());
        assertFalse(p.canBeUpdated());
    }

    @Test
    @DisplayName("Check if the status PENDING can be updated")
    void testStatusCanBeUpdated_whenStateIsPENDING() {
        p.setStatus(Status.PENDING);
        assertEquals(Status.PENDING, p.getStatus());
        assertTrue(p.canBeUpdated());
    }
    @Test
    @DisplayName("Check if the status SHIPPING can be updated")
    void testStatusCanBeUpdated_whenStateIsSHIPPING() {
        p.setStatus(Status.SHIPPING);
        assertEquals(Status.SHIPPING, p.getStatus());
        assertTrue(p.canBeUpdated());
    }

    @Test
    @DisplayName("Check if the status INTRANSIT can be updated")
    void testStatusCanBeUpdated_whenStateIsINTRANSIT() {
        p.setStatus(Status.INTRANSIT);
        assertEquals(Status.INTRANSIT, p.getStatus());
        assertTrue(p.canBeUpdated());
    }

    @Test
    @DisplayName("Check if the status AVAILABLE can be updated")
    void testStatusCanBeUpdated_whenStateIsAVAILABLE() {
        p.setStatus(Status.AVAILABLE);
        assertEquals(Status.AVAILABLE, p.getStatus());
        assertTrue(p.canBeUpdated());
    }

    @Test
    @DisplayName("Check if the status DELIVERED can be updated")
    void testStatusCanBeUpdated_whenStateIsDELIVERED() {
        p.setStatus(Status.DELIVERED);
        assertEquals(Status.DELIVERED, p.getStatus());
        assertTrue(p.canBeUpdated());
    }

}
