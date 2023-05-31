package pt.ua.deti.tqs.roadrunnerbackend.controllers.unit;

import io.restassured.module.mockmvc.RestAssuredMockMvc;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpStatus;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import pt.ua.deti.tqs.roadrunnerbackend.controllers.ShopController;
import pt.ua.deti.tqs.roadrunnerbackend.model.PickUpLocation;
import pt.ua.deti.tqs.roadrunnerbackend.model.dto.ErrorDTO;
import pt.ua.deti.tqs.roadrunnerbackend.model.dto.HistoryStateDTO;
import pt.ua.deti.tqs.roadrunnerbackend.model.dto.OrderDto;
import pt.ua.deti.tqs.roadrunnerbackend.services.ShopService;

import java.util.List;
import java.util.UUID;

import static org.hamcrest.Matchers.equalTo;
import static org.mockito.Mockito.*;


@WebMvcTest(ShopController.class)
@AutoConfigureMockMvc(addFilters = false)
@ActiveProfiles("test")
class ShopControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private ShopService shopService;

    @BeforeEach
    void setUp() {
        RestAssuredMockMvc.mockMvc(mockMvc);
    }

    @Test
    @DisplayName("When get History, then returns ok")
    void whenGetHistory_thenReturnOK(){
        HistoryStateDTO historyStateDTO = new HistoryStateDTO();
        when(shopService.getHistory()).thenReturn(historyStateDTO);
        RestAssuredMockMvc.given()
                .contentType("application/json")
                .when()
                .get("/api/shop/history")
                .then()
                .statusCode(200);

        verify(shopService, times(0)).getAllPickUpLocations();
        verify(shopService, times(1)).getHistory();
    }

    @Test
    @DisplayName("When get pickuplocations, then returns ok and pickuplocations")
    void whenGetPickUpLocations_thenReturnOKAndListPickup(){
        UUID validId = UUID.randomUUID();
        PickUpLocation pickUpLocation = new PickUpLocation(validId, "name", "slug", "Rua de Aveiro", "Aveiro", 1L,1L, false, false);
        when(shopService.getAllPickUpLocations()).thenReturn(List.of(pickUpLocation));
        RestAssuredMockMvc.given()
                .contentType("application/json")
                .when()
                .get("/api/shop/pickuplocation")
                .then()
                .statusCode(200)
                .body("message.size()", equalTo(1));


        verify(shopService, times(1)).getAllPickUpLocations();
        verify(shopService, times(0)).getHistory();
    }

    @Test
    @DisplayName("When get pickuplocations, then returns bad request and error message")
    void whenGetPickUpLocationsWithEmptyList_thenReturnBadRequest(){
        ErrorDTO errorDTO = new ErrorDTO("NO PICK UP LOCATIONS FOUND");
        when(shopService.getAllPickUpLocations()).thenReturn(List.of());
        RestAssuredMockMvc.given()
                .contentType("application/json")
                .when()
                .get("/api/shop/pickuplocation")
                .then()
                .status(HttpStatus.BAD_REQUEST)
                .body("message", equalTo(errorDTO.getMessage()));

        verify(shopService, times(1)).getAllPickUpLocations();
        verify(shopService, times(0)).getHistory();
    }

    @Test
    @DisplayName("When update package, then returns ok")
    void whenUpdatePackage_thenReturnOK(){
        UUID validId = UUID.randomUUID();
        when(shopService.updatePackage(validId, "newstate", "token")).thenReturn(true);
        RestAssuredMockMvc.given()
                .header("Authorization", "token")
                .contentType("application/json")
                .when()
                .put("/api/shop/package/{id}?newstate={newstate}", validId, "newstate")
                .then()
                .statusCode(200);

        verify(shopService, times(1)).updatePackage(validId, "newstate", "token");
    }

    @Test
    @DisplayName("When update package, then returns bad request")
    void whenUpdatePackage_thenReturnBadRequest(){
        UUID validId = UUID.randomUUID();
        when(shopService.updatePackage(validId, "newstate", "token")).thenReturn(false);
        RestAssuredMockMvc.given()
                .header("Authorization", "token")
                .contentType("application/json")
                .when()
                .put("/api/shop/package/{id}?newstate={newstate}", validId, "newstate")
                .then()
                .statusCode(400);

        verify(shopService, times(1)).updatePackage(validId, "newstate", "token");
    }


    @Test
    @DisplayName("When create package, then returns ok")
    void whenCreatePackage_thenReturnOK(){
        UUID validId = UUID.randomUUID();
        OrderDto orderDto = new OrderDto();
        when(shopService.createPackage(orderDto)).thenReturn(validId);
        RestAssuredMockMvc.given()
                .contentType("application/json")
                .body(orderDto)
                .when()
                .post("/api/shop/package")
                .then()
                .statusCode(201);

        verify(shopService, times(1)).createPackage(orderDto);
    }

    @Test
    @DisplayName("When create package, then returns bad request")
    void whenCreatePackage_thenReturnBadRequest(){
        OrderDto orderDto = new OrderDto();
        when(shopService.createPackage(orderDto)).thenReturn(null);
        RestAssuredMockMvc.given()
                .contentType("application/json")
                .body(orderDto)
                .when()
                .post("/api/shop/package")
                .then()
                .statusCode(400);

        verify(shopService, times(1)).createPackage(orderDto);
    }

}