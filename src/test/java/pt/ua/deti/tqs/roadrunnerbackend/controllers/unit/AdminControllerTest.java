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
import pt.ua.deti.tqs.roadrunnerbackend.controllers.AdminController;
import pt.ua.deti.tqs.roadrunnerbackend.model.Customer;
import pt.ua.deti.tqs.roadrunnerbackend.model.Package;
import pt.ua.deti.tqs.roadrunnerbackend.model.PickUpLocation;
import pt.ua.deti.tqs.roadrunnerbackend.model.Shop;
import pt.ua.deti.tqs.roadrunnerbackend.model.dto.ErrorDTO;
import pt.ua.deti.tqs.roadrunnerbackend.model.dto.SuccessDTO;
import pt.ua.deti.tqs.roadrunnerbackend.model.enums.Status;
import pt.ua.deti.tqs.roadrunnerbackend.services.AdminService;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.is;
import static org.mockito.Mockito.*;


@WebMvcTest(AdminController.class)
@AutoConfigureMockMvc(addFilters = false)
@ActiveProfiles("test")
class AdminControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private AdminService adminService;

    private Shop shop;
    private PickUpLocation pickUpLocation;
    private List<Shop> shops;
    private List<Package> packages;
    private List<PickUpLocation> pickUpLocations;


    @BeforeEach
    void setUp() {
        RestAssuredMockMvc.mockMvc(mockMvc);
        shop = new Shop(UUID.randomUUID(), "shop", "shop", "rua", "aveiro", 1, 1, false);
        shops = List.of(shop);
        Package pack = new Package(UUID.randomUUID(),new ArrayList<>(), new Customer(), new PickUpLocation(), new Shop(), System.currentTimeMillis(), Status.SHIPPING);
        packages = List.of(pack);
        pickUpLocation = new PickUpLocation(UUID.randomUUID(), "name", "slug", "Rua de Aveiro", "Aveiro", 1L,1L, false, false);
        pickUpLocations = List.of(pickUpLocation);
    }
    @Test
    @DisplayName("Test getShops() with valid token, then return OK and list of shops")
    void testGetShopsWithValidToken() {
        String validToken = "valid_token";
        when(adminService.getShops()).thenReturn(shops);

        RestAssuredMockMvc.given()
                .contentType("application/json")
                .header("Authorization", validToken)
                .when()
                .get("api/shop")
                .then()
                .status(HttpStatus.OK)
                .body("[0].id", equalTo(shop.getId().toString()))
                .body("[0].name", equalTo(shop.getName()))
                .body("[0].address", equalTo(shop.getAddress()))
                .body("[0].city", equalTo(shop.getCity()));

        verify(adminService,times(1)).getShops();
        verify(adminService,times(0)).addShop(any(Shop.class));
        verify(adminService,times(0)).getShopById(any(UUID.class));
        verify(adminService,times(0)).deleteShop(any(UUID.class));
    }

    @Test
    @DisplayName("Test getShops() with valid token but then return BAD_REQUEST and ErrorDTO")
    void whenGetShopsWithValidToken_thenReturnBadRequest(){
        String validToken = "valid_token";
        when(adminService.getShops()).thenReturn(new ErrorDTO("Error"));

        RestAssuredMockMvc.given()
                .header("Authorization", validToken)
                .contentType("application/json")
                .when()
                .get("api/shop")
                .then()
                .status(HttpStatus.BAD_REQUEST)
                .body("message", equalTo("Error"));

        verify(adminService,times(1)).getShops();
        verify(adminService,times(0)).addShop(any(Shop.class));
        verify(adminService,times(0)).getShopById(any(UUID.class));
        verify(adminService,times(0)).deleteShop(any(UUID.class));
    }

    @Test
    @DisplayName("Test addShop() with valid token, then return OK and shop")
    void whenAddShopWithValidToken_ReturnOKAndShop(){
        String validToken = "valid_token";
        when(adminService.addShop(shop)).thenReturn(shop);

        RestAssuredMockMvc.given()
                .header("Authorization", validToken)
                .contentType("application/json")
                .body(shop)
                .when()
                .post("api/shop")
                .then()
                .status(HttpStatus.OK)
                .body("id", equalTo(shop.getId().toString()))
                .body("name", equalTo(shop.getName()))
                .body("address", equalTo(shop.getAddress()))
                .body("city", equalTo(shop.getCity()));

        verify(adminService,times(0)).getShops();
        verify(adminService,times(1)).addShop(any(Shop.class));
        verify(adminService,times(0)).getShopById(any(UUID.class));
        verify(adminService,times(0)).deleteShop(any(UUID.class));
    }

    @Test
    @DisplayName("Test addShop() with valid token, then return BadRequest and ErrorDTO")
    void whenAddShopWithValidToken_ReturnBadRequest(){
        String validToken = "valid_token";
        when(adminService.addShop(shop)).thenReturn(null);

        RestAssuredMockMvc.given()
                .contentType("application/json")
                .header("Authorization", validToken)
                .body(shop)
                .when()
                .post("api/shop")
                .then()
                .status(HttpStatus.BAD_REQUEST)
                .body("message", equalTo("Error adding shop"));


        verify(adminService,times(0)).getShops();
        verify(adminService,times(1)).addShop(any(Shop.class));
        verify(adminService,times(0)).getShopById(any(UUID.class));
        verify(adminService,times(0)).deleteShop(any(UUID.class));
    }

    @Test
    @DisplayName("Test GetShopById with valid token, then return OK and shop")
    void whenGetShopByIdWithValidToken_ReturnOKAndShop(){
        String validToken = "valid_token";
        when(adminService.getShopById(shop.getId())).thenReturn(shop);

        RestAssuredMockMvc.given()
                .header("Authorization", validToken)
                .contentType("application/json")
                .when()
                .get("api/shop/" + shop.getId())
                .then()
                .status(HttpStatus.OK)
                .body("id", equalTo(shop.getId().toString()))
                .body("name", equalTo(shop.getName()))
                .body("address", equalTo(shop.getAddress()))
                .body("city", equalTo(shop.getCity()));

        verify(adminService,times(0)).getShops();
        verify(adminService,times(0)).addShop(any(Shop.class));
        verify(adminService,times(1)).getShopById(any(UUID.class));
        verify(adminService,times(0)).deleteShop(any(UUID.class));
    }

    @Test
    @DisplayName("Test GetShopById with valid token, then return BadRequest and ErrorDTO")
    void whenGetShopByIdWithValidToken_ReturnBadRequest(){
        String validToken = "valid_token";
        when(adminService.getShopById(shop.getId())).thenReturn(new ErrorDTO("Error"));

        RestAssuredMockMvc.given()
                .contentType("application/json")
                .header("Authorization", validToken)
                .when()
                .get("api/shop/" + shop.getId())
                .then()
                .status(HttpStatus.BAD_REQUEST)
                .body("message", equalTo("Error"));


        verify(adminService,times(0)).getShops();
        verify(adminService,times(0)).addShop(any(Shop.class));
        verify(adminService,times(1)).getShopById(any(UUID.class));
        verify(adminService,times(0)).deleteShop(any(UUID.class));
    }

    @Test
    @DisplayName("Test deleteShop with valid token, then return OK and SuccessDTO")
    void whenDeleteShopWithValidToken_ReturnOKAndSucessMessage(){
        String validToken = "valid_token";
        when(adminService.deleteShop(shop.getId())).thenReturn(true);

        RestAssuredMockMvc.given()
                .header("Authorization", validToken)
                .contentType("application/json")
                .when()
                .delete("api/shop/" + shop.getId())
                .then()
                .status(HttpStatus.OK)
                .body("message", equalTo("Shop removed"));

        verify(adminService,times(0)).getShops();
        verify(adminService,times(0)).addShop(any(Shop.class));
        verify(adminService,times(0)).getShopById(any(UUID.class));
        verify(adminService,times(1)).deleteShop(any(UUID.class));
    }

    @Test
    @DisplayName("Test deleteShop with valid token, then return BadRequest and ErrorDTO")
    void whenDeleteShopWithValidToken_ReturnBadRequest(){
        String validToken = "valid_token";
        when(adminService.deleteShop(shop.getId())).thenReturn(false);

        RestAssuredMockMvc.given()
                .contentType("application/json")
                .header("Authorization", validToken)
                .when()
                .delete("api/shop/" + shop.getId())
                .then()
                .status(HttpStatus.NOT_FOUND)
                .body("message", equalTo("Shop not found"));


        verify(adminService,times(0)).getShops();
        verify(adminService,times(0)).addShop(any(Shop.class));
        verify(adminService,times(0)).getShopById(any(UUID.class));
        verify(adminService,times(1)).deleteShop(any(UUID.class));
    }

    @Test
    @DisplayName("Test getPackagesByShop with valid token, then return OK and list of packages")
    void whengetPackagesByShopWithValidToken_ReturnOKAndPackage(){
        String validToken = "valid_token";
        when(adminService.getPackagesByShop(shop.getId())).thenReturn(packages);

        RestAssuredMockMvc.given()
                .header("Authorization", validToken)
                .contentType("application/json")
                .when()
                .get("api/shop/package?id=" + shop.getId())
                .then()
                .status(HttpStatus.OK)
                .body("[0].id", equalTo(packages.get(0).getId().toString()));


        verify(adminService,times(0)).getShops();
        verify(adminService,times(0)).addShop(any(Shop.class));
        verify(adminService,times(0)).getShopById(any(UUID.class));
        verify(adminService,times(0)).deleteShop(any(UUID.class));
        verify(adminService,times(1)).getPackagesByShop(shop.getId());
    }

    @Test
    @DisplayName("Test getPackagesByShop with valid token, then return BadRequest and ErrorDTO")
    void whengetPackagesByShopWithValidToken_ReturnBadRequest(){
        String validToken = "valid_token";
        when(adminService.getPackagesByShop(shop.getId())).thenReturn(new ErrorDTO("Error"));

        RestAssuredMockMvc.given()
                .contentType("application/json")
                .header("Authorization", validToken)
                .when()
                .get("api/shop/package?id=" + shop.getId())
                .then()
                .status(HttpStatus.BAD_REQUEST)
                .body("message", equalTo("Error"));


        verify(adminService,times(0)).getShops();
        verify(adminService,times(0)).addShop(any(Shop.class));
        verify(adminService,times(0)).getShopById(any(UUID.class));
        verify(adminService,times(0)).deleteShop(any(UUID.class));
        verify(adminService,times(1)).getPackagesByShop(shop.getId());
    }

    @Test
    @DisplayName("Test deletePickUp with valid token, then return OK and SuccessDTO")
    void whendeletePickUpWithValidToken_ReturnOKAndSucessMessage(){
        String validToken = "valid_token";
        when(adminService.deletePickUpLocation(pickUpLocation.getId())).thenReturn(new SuccessDTO<String>("Pick Up Location removed"));

        RestAssuredMockMvc.given()
                .header("Authorization", validToken)
                .contentType("application/json")
                .when()
                .delete("api/pickup/" + pickUpLocation.getId())
                .then()
                .status(HttpStatus.OK)
                .body("message", equalTo("Pick Up Location removed"));

        verify(adminService,times(0)).getShops();
        verify(adminService,times(0)).addShop(any(Shop.class));
        verify(adminService,times(0)).getShopById(any(UUID.class));
        verify(adminService,times(0)).deleteShop(any(UUID.class));
        verify(adminService,times(1)).deletePickUpLocation(any(UUID.class));
    }

    @Test
    @DisplayName("Test deletePickUp with valid token, then return BadRequest and ErrorDTO")
    void whendeletePickUpWithValidToken_ReturnBadRequest(){
        String validToken = "valid_token";
        when(adminService.deletePickUpLocation(pickUpLocation.getId())).thenReturn(new ErrorDTO("Error"));

        RestAssuredMockMvc.given()
                .contentType("application/json")
                .header("Authorization", validToken)
                .when()
                .delete("api/pickup/" + pickUpLocation.getId())
                .then()
                .status(HttpStatus.NOT_FOUND)
                .body("message", equalTo("Error"));


        verify(adminService,times(0)).getShops();
        verify(adminService,times(0)).addShop(any(Shop.class));
        verify(adminService,times(0)).getShopById(any(UUID.class));
        verify(adminService,times(0)).deleteShop(any(UUID.class));
        verify(adminService,times(1)).deletePickUpLocation(any(UUID.class));
    }

    @Test
    @DisplayName("Test acceptPickUpLocation with valid token, then return OK and SuccessDTO")
    void whenacceptPickUpLocationWithValidToken_ReturnOKAndSucessMessage(){
        String validToken = "valid_token";
        when(adminService.acceptedPickUpLocation(pickUpLocation.getId())).thenReturn(new SuccessDTO<>("Pick Up Location accepted"));

        RestAssuredMockMvc.given()
                .header("Authorization", validToken)
                .contentType("application/json")
                .when()
                .put("api/pickup/" + pickUpLocation.getId())
                .then()
                .status(HttpStatus.OK)
                .body("message", equalTo("Pick Up Location accepted"));

        verify(adminService,times(0)).getShops();
        verify(adminService,times(0)).addShop(any(Shop.class));
        verify(adminService,times(0)).getShopById(any(UUID.class));
        verify(adminService,times(0)).deleteShop(any(UUID.class));
        verify(adminService,times(0)).deletePickUpLocation(any(UUID.class));
        verify(adminService,times(1)).acceptedPickUpLocation(any(UUID.class));
    }

    @Test
    @DisplayName("Test acceptPickUpLocation with valid token, then return BadRequest and ErrorDTO")
    void whenacceptPickUpLocationWithValidToken_ReturnBadRequest(){
        String validToken = "valid_token";
        when(adminService.acceptedPickUpLocation(pickUpLocation.getId())).thenReturn(new ErrorDTO("Error"));

        RestAssuredMockMvc.given()
                .contentType("application/json")
                .header("Authorization", validToken)
                .when()
                .put("api/pickup/" + pickUpLocation.getId())
                .then()
                .status(HttpStatus.NOT_FOUND)
                .body("message", equalTo("Error"));


        verify(adminService,times(0)).getShops();
        verify(adminService,times(0)).addShop(any(Shop.class));
        verify(adminService,times(0)).getShopById(any(UUID.class));
        verify(adminService,times(0)).deleteShop(any(UUID.class));
        verify(adminService,times(0)).deletePickUpLocation(any(UUID.class));
        verify(adminService,times(1)).acceptedPickUpLocation(any(UUID.class));
    }

    @Test
    @DisplayName("Test getPickUpLocations with valid token, then return OK and list of PickUpLocationDTO")
    void whengetPickUpLocationsWithValidToken_ReturnOKAndPickLocations(){
        String validToken = "valid_token";
        when(adminService.getPickUpLocations(null, null)).thenReturn(pickUpLocations);

        RestAssuredMockMvc.given()
                .header("Authorization", validToken)
                .contentType("application/json")
                .when()
                .get("api/pickup")
                .then()
                .status(HttpStatus.OK)
                .body("size()", is(1))
                .body("[0].id", equalTo(pickUpLocation.getId().toString()))
                .body("[0].name", equalTo(pickUpLocation.getName()))
                .body("[0].address", equalTo(pickUpLocation.getAddress()));

        verify(adminService,times(0)).getShops();
        verify(adminService,times(0)).addShop(any(Shop.class));
        verify(adminService,times(0)).getShopById(any(UUID.class));
        verify(adminService,times(0)).deleteShop(any(UUID.class));
        verify(adminService,times(0)).deletePickUpLocation(any(UUID.class));
        verify(adminService,times(0)).acceptedPickUpLocation(any(UUID.class));
        verify(adminService,times(1)).getPickUpLocations(any(), any());
    }

    @Test
    @DisplayName("Test getPickUpLocations with valid token, then return BadRequest and ErrorDTO")
    void whengetPickUpLocationsWithValidToken_ReturnBadRequest(){
        String validToken = "valid_token";
        when(adminService.getPickUpLocations(null, null)).thenReturn(new ErrorDTO("Error"));

        RestAssuredMockMvc.given()
                .contentType("application/json")
                .header("Authorization", validToken)
                .when()
                .get("api/pickup")
                .then()
                .status(HttpStatus.BAD_REQUEST)
                .body("message", equalTo("Error"));


        verify(adminService,times(0)).getShops();
        verify(adminService,times(0)).addShop(any(Shop.class));
        verify(adminService,times(0)).getShopById(any(UUID.class));
        verify(adminService,times(0)).deleteShop(any(UUID.class));
        verify(adminService,times(0)).deletePickUpLocation(any(UUID.class));
        verify(adminService,times(0)).acceptedPickUpLocation(any(UUID.class));
        verify(adminService,times(1)).getPickUpLocations(any(), any());
    }

    @Test
    @DisplayName("Test getPackagesByPickUp with valid token, then return OK and list of packages")
    void whengetPackagesByPickupWithValidToken_ReturnOKAndPackage(){
        String validToken = "valid_token";
        when(adminService.getPackagesByPickUpLocation(pickUpLocation.getId())).thenReturn(packages);

        RestAssuredMockMvc.given()
                .header("Authorization", validToken)
                .contentType("application/json")
                .when()
                .get("api/pickup/package?pickupId=" + pickUpLocation.getId())
                .then()
                .status(HttpStatus.OK)
                .body("[0].id", equalTo(packages.get(0).getId().toString()));


        verify(adminService,times(0)).getShops();
        verify(adminService,times(0)).addShop(any(Shop.class));
        verify(adminService,times(0)).getShopById(any(UUID.class));
        verify(adminService,times(0)).deleteShop(any(UUID.class));
        verify(adminService,times(0)).getPackagesByShop(shop.getId());
        verify(adminService,times(0)).deletePickUpLocation(any(UUID.class));
        verify(adminService,times(0)).acceptedPickUpLocation(any(UUID.class));
        verify(adminService,times(0)).getPickUpLocations(any(), any());
        verify(adminService,times(1)).getPackagesByPickUpLocation(any(UUID.class));
    }

    @Test
    @DisplayName("Test getPackagesByPickUp with valid token, then return BadRequest and ErrorDTO")
    void whengetPackagesByPickUpWithValidToken_ReturnBadRequest(){
        String validToken = "valid_token";
        when(adminService.getPackagesByPickUpLocation(pickUpLocation.getId())).thenReturn(new ErrorDTO("Error"));

        RestAssuredMockMvc.given()
                .contentType("application/json")
                .header("Authorization", validToken)
                .when()
                .get("api/pickup/package?pickupId=" + pickUpLocation.getId())
                .then()
                .status(HttpStatus.BAD_REQUEST)
                .body("message", equalTo("Error"));


        verify(adminService,times(0)).getShops();
        verify(adminService,times(0)).addShop(any(Shop.class));
        verify(adminService,times(0)).getShopById(any(UUID.class));
        verify(adminService,times(0)).deleteShop(any(UUID.class));
        verify(adminService,times(0)).getPackagesByShop(shop.getId());
        verify(adminService,times(0)).deletePickUpLocation(any(UUID.class));
        verify(adminService,times(0)).acceptedPickUpLocation(any(UUID.class));
        verify(adminService,times(0)).getPickUpLocations(any(), any());
        verify(adminService,times(1)).getPackagesByPickUpLocation(any(UUID.class));
    }
}