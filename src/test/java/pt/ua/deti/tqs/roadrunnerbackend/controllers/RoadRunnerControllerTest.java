package pt.ua.deti.tqs.roadrunnerbackend.controllers;

import io.restassured.module.mockmvc.RestAssuredMockMvc;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpStatus;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import pt.ua.deti.tqs.roadrunnerbackend.controllers.RoadRunnerController;
import pt.ua.deti.tqs.roadrunnerbackend.model.Customer;
import pt.ua.deti.tqs.roadrunnerbackend.model.Package;
import pt.ua.deti.tqs.roadrunnerbackend.model.PickUpLocation;
import pt.ua.deti.tqs.roadrunnerbackend.model.Shop;
import pt.ua.deti.tqs.roadrunnerbackend.model.dto.ErrorDTO;
import pt.ua.deti.tqs.roadrunnerbackend.model.enums.Status;
import pt.ua.deti.tqs.roadrunnerbackend.services.RoadRunnerService;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.UUID;

import static org.hamcrest.Matchers.equalTo;
import static org.mockito.Mockito.*;

@WebMvcTest(RoadRunnerController.class)
@AutoConfigureMockMvc(addFilters = false)
@ActiveProfiles("test")
class RoadRunnerControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private RoadRunnerService roadRunnerService;

    @BeforeEach
    void setUp() {
        RestAssuredMockMvc.mockMvc(mockMvc);
    }

    @Test
    @WithMockUser
    @DisplayName("When get pickup with valid id, then returns ok and pickup")
    void whenGetPickupWithValidId_thenReturnOkAndPickup() {
        String validToken = "validToken";
        UUID validId = UUID.randomUUID();
        PickUpLocation pickUpLocation = new PickUpLocation(validId, "name", "slug", "Rua de Aveiro", "Aveiro", 1L,1L, false, false);

        when(roadRunnerService.getPickUpLocationById(validId, validToken)).thenReturn(pickUpLocation);

        RestAssuredMockMvc.given()
                .header("Authorization", validToken)
                .contentType("application/json")
                .when()
                .get("/api/pickup/" + validId)
                .then()
                .status(HttpStatus.OK)
                .body("id", equalTo(validId.toString()))
                .body("name", equalTo("name"))
                .body("slug", equalTo("slug"))
                .body("address", equalTo("Rua de Aveiro"))
                .body("city", equalTo("Aveiro"))
                .body("latitude", equalTo(1))
                .body("longitude", equalTo(1))
                .body("accepted", equalTo(false))
                .body("disable", equalTo(false));

        verify(roadRunnerService, times(1)).getPickUpLocationById(validId, validToken);
        verify(roadRunnerService, times(0)).getPackageById(validId, validToken);
        verify(roadRunnerService, times(0)).getAllPackages( null, validToken);
    }

    @Test
    @WithMockUser
    @DisplayName("When get pickup with invalid id, then returns error")
    void whenGetPickupWithInvalidId_ReturnBadRequest(){
        String validToken = "validToken";
        UUID InvalidId = UUID.randomUUID();
        ErrorDTO error = new ErrorDTO("Pickup location not found");

        when(roadRunnerService.getPickUpLocationById(InvalidId, validToken)).thenReturn(error);

        RestAssuredMockMvc.given()
                .header("Authorization", validToken)
                .contentType("application/json")
                .when()
                .get("/api/pickup/" + InvalidId)
                .then()
                .status(HttpStatus.BAD_REQUEST)
                .body("message", equalTo("Pickup location not found"));

        verify(roadRunnerService, times(1)).getPickUpLocationById(InvalidId, validToken);
        verify(roadRunnerService, times(0)).getPackageById(InvalidId, validToken);
        verify(roadRunnerService, times(0)).getAllPackages(null, validToken);

    }

    @Test
    @WithMockUser
    @DisplayName("When get package with valid id, then returns ok and package")
    void whenGetPackageWithValidId_thenReturnOkAndPickup() {
        String validToken = "validToken";
        UUID validId = UUID.randomUUID();
        Package pack = new Package(validId,new ArrayList<>(), new Customer(), new PickUpLocation(), new Shop(), System.currentTimeMillis(),Status.SHIPPING);

        when(roadRunnerService.getPackageById(validId, validToken)).thenReturn(pack);

        RestAssuredMockMvc.given()
                .header("Authorization", validToken)
                .contentType("application/json")
                .when()
                .get("/api/package/" + validId)
                .then()
                .status(HttpStatus.OK)
                .body("id", equalTo(validId.toString()))
                .body("status", equalTo("SHIPPING"));


        verify(roadRunnerService, times(0)).getPickUpLocationById(validId, validToken);
        verify(roadRunnerService, times(1)).getPackageById(validId, validToken);
        verify(roadRunnerService, times(0)).getAllPackages(null, validToken);
    }

    @Test
    @WithMockUser
    @DisplayName("When get package with invalid id, then returns error")
    void whenGetPackageWithInvalidId_ReturnBadRequest(){
        String validToken = "validToken";
        UUID InvalidId = UUID.randomUUID();
        ErrorDTO error = new ErrorDTO("PACKAGE NOT FOUND");

        when(roadRunnerService.getPackageById(InvalidId, validToken)).thenReturn(error);

        RestAssuredMockMvc.given()
                .header("Authorization", validToken)
                .contentType("application/json")
                .when()
                .get("/api/package/" + InvalidId)
                .then()
                .status(HttpStatus.BAD_REQUEST)
                .body("message", equalTo("PACKAGE NOT FOUND"));

        verify(roadRunnerService, times(0)).getPickUpLocationById(InvalidId, validToken);
        verify(roadRunnerService, times(1)).getPackageById(InvalidId, validToken);
        verify(roadRunnerService, times(0)).getAllPackages(null, validToken);

    }

    @Test
    @WithMockUser
    @DisplayName("When get packages, then returns ok and packages")
    void whenGetPackages_thenReturnOkAndPickup() {
        String validToken = "validToken";
        UUID validId = UUID.randomUUID();
        Package pack1 = new Package(validId,new ArrayList<>(), new Customer(), new PickUpLocation(), new Shop(), System.currentTimeMillis(), Status.DENIED);
        Package pack2 = new Package(validId,new ArrayList<>(), new Customer(), new PickUpLocation(), new Shop(), System.currentTimeMillis(), Status.SHIPPING);

        when(roadRunnerService.getAllPackages(null, validToken)).thenReturn(Arrays.asList(pack1, pack2));

        RestAssuredMockMvc.given()
                .header("Authorization", validToken)
                .contentType("application/json")
                .when()
                .get("/api/package")
                .then()
                .status(HttpStatus.OK)
                .body("size()", equalTo(2))
                .body("[0].id", equalTo(validId.toString()))
                .body("[0].status", equalTo("DENIED"))
                .body("[1].id", equalTo(validId.toString()))
                .body("[1].status", equalTo("SHIPPING"));

        verify(roadRunnerService, times(0)).getPickUpLocationById(validId, validToken);
        verify(roadRunnerService, times(0)).getPackageById(validId, validToken);
        verify(roadRunnerService, times(1)).getAllPackages( null, validToken);
    }

    @Test
    @WithMockUser
    @DisplayName("When get package, then returns error")
    void whenGetPackages_ReturnBadRequest(){
        String validToken = "validToken";
        UUID InvalidId = UUID.randomUUID();
        ErrorDTO error = new ErrorDTO("PACKAGES NOT FOUND");

        when(roadRunnerService.getAllPackages( null, validToken)).thenReturn(error);

        RestAssuredMockMvc.given()
                .header("Authorization", validToken)
                .contentType("application/json")
                .when()
                .get("/api/package")
                .then()
                .status(HttpStatus.BAD_REQUEST)
                .body("message", equalTo("PACKAGES NOT FOUND"));

        verify(roadRunnerService, times(0)).getPickUpLocationById(InvalidId, validToken);
        verify(roadRunnerService, times(0)).getPackageById(InvalidId, validToken);
        verify(roadRunnerService, times(1)).getAllPackages( null, validToken);
    }




}