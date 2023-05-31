package pt.ua.deti.tqs.roadrunnerbackend.controllers;

import io.restassured.module.mockmvc.RestAssuredMockMvc;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import pt.ua.deti.tqs.roadrunnerbackend.model.auth.LoginRequest;
import pt.ua.deti.tqs.roadrunnerbackend.model.auth.LoginResponse;
import pt.ua.deti.tqs.roadrunnerbackend.model.auth.RegisterRequest;
import pt.ua.deti.tqs.roadrunnerbackend.model.dto.ErrorDTO;
import pt.ua.deti.tqs.roadrunnerbackend.model.dto.SuccessDTO;
import pt.ua.deti.tqs.roadrunnerbackend.model.dto.UserDto;
import pt.ua.deti.tqs.roadrunnerbackend.services.AuthService;

import java.util.UUID;

import static org.hamcrest.Matchers.equalTo;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

// unit tests with WebMvcTest, Mockito, RestAssured and hamcrest
@WebMvcTest(AuthController.class)
@AutoConfigureMockMvc(addFilters = false)
@ActiveProfiles("test")
class AuthControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private AuthService authService;

    private RegisterRequest registerRequest;

    private LoginRequest loginRequest;

    @BeforeEach
    void setUp() {
        RestAssuredMockMvc.mockMvc(mockMvc);

        registerRequest = new RegisterRequest();
        registerRequest.setEmail("client@gmail.com");
        registerRequest.setPassword("123456");
        registerRequest.setFirstName("Client");
        registerRequest.setLastName("Client");

        loginRequest = new LoginRequest("client@gmail.com", "123456");

    }

    @Test
    @DisplayName("When signup with user that does not exist, then returns created and token")
    void whenSignUp_UserDoesNotExist_thenReturnsCreatedAndToken() {
        SuccessDTO<String> response = new SuccessDTO<String>("User created successfully");
        when(authService.signup(any())).thenReturn(response);

        RestAssuredMockMvc.given()
                .contentType("application/json")
                .body(registerRequest)
                .when()
                .post("/api/auth/signup")
                .then()
                .statusCode(201)
                .body("message", equalTo(response.getMessage()));

        verify(authService, times(1)).signup(registerRequest);
        verify(authService, times(0)).login(loginRequest);
        verify(authService, times(0)).getCurrentUser(any());
        verify(authService, times(0)).logout(any());
    }

    @Test
    @DisplayName("When signup with user that already exists, then returns bad request")
    void whenSignUp_UserAlreadyExists_thenReturnsBadRequest() {
        ErrorDTO errorDTO = new ErrorDTO("User already exists");
        when(authService.signup(any())).thenReturn(errorDTO);

        RestAssuredMockMvc.given()
                .contentType("application/json")
                .body(registerRequest)
                .when()
                .post("/api/auth/signup")
                .then()
                .statusCode(400)
                .body("message", equalTo(errorDTO.getMessage()));

        verify(authService, times(1)).signup(registerRequest);
        verify(authService, times(0)).login(loginRequest);
        verify(authService, times(0)).getCurrentUser(any());
        verify(authService, times(0)).logout(any());
    }

    @Test
    @DisplayName("When login with user that exists, then returns token")
    void whenLogin_UserExists_thenReturnsToken() {
        LoginResponse response = new LoginResponse("Token");
        when(authService.login(any())).thenReturn(response);

        RestAssuredMockMvc.given()
                .contentType("application/json")
                .body(loginRequest)
                .when()
                .post("/api/auth/login")
                .then()
                .statusCode(200)
                .body("token", equalTo(response.getToken()));

        verify(authService, times(0)).signup(registerRequest);
        verify(authService, times(1)).login(loginRequest);
    }

    @Test
    @DisplayName("When login with invalid data, then returns bad request")
    void whenLogin_InvalidData_thenReturnsBadRequest() {
        ErrorDTO errorDTO = new ErrorDTO("Invalid credentials");
        when(authService.login(any())).thenReturn(errorDTO);

        RestAssuredMockMvc.given()
                .contentType("application/json")
                .body(loginRequest)
                .when()
                .post("/api/auth/login")
                .then()
                .statusCode(400)
                .body("message", equalTo(errorDTO.getMessage()));

        verify(authService, times(0)).signup(registerRequest);
        verify(authService, times(1)).login(loginRequest);
    }

    @Test
    @WithMockUser
    @DisplayName("When logout with valid token, then returns ok")
    void WhenLogout_ValidToken_ThenReturnsOK() {
        String validToken = "valid_token";
        SuccessDTO<String> successDTO = new SuccessDTO<String>("Logout successful");

        when(authService.logout(validToken)).thenReturn(true);

        RestAssuredMockMvc.given()
                .header("Authorization", validToken)
                .contentType("application/json")
                .when()
                .put("/api/auth/logout")
                .then()
                .statusCode(200)
                .body("message", equalTo(successDTO.getMessage()));

        verify(authService, times(1)).logout(validToken);
    }

    @Test
    @WithMockUser
    @DisplayName("When logout with invalid token, then returns unauthorized")
    void WhenLogout_InvalidToken_ThenReturnsUnauthorized() {
        String invalidToken = "invalid_token";

        when(authService.logout(invalidToken)).thenReturn(false);

        RestAssuredMockMvc.given()
                .header("Authorization", invalidToken)
                .contentType("application/json")
                .when()
                .put("/api/auth/logout")
                .then()
                .statusCode(400)
                .body("message", equalTo("Invalid credentials"));
    }


    @Test
    @WithMockUser
    @DisplayName("When me with valid token, then returns user")
    void WhenMe_ValidToken_ThenReturnsOk(){
        String validToken = "valid_token";
        UserDto user = new UserDto();
        user.setId(UUID.randomUUID());
        user.setEmail("client@gmail.com");
        user.setFirstName("Client");
        user.setLastName("Client");

        when(authService.getCurrentUser(validToken)).thenReturn(user);

        RestAssuredMockMvc.given()
                .header("Authorization", validToken)
                .contentType("application/json")
                .when()
                .get("/api/auth/me")
                .then()
                .statusCode(200)
                .body("email", equalTo(user.getEmail()))
                .body("firstName", equalTo(user.getFirstName()))
                .body("lastName", equalTo(user.getLastName()));


        verify(authService, times(1)).getCurrentUser(validToken);
    }

    @Test
    @WithMockUser
    @DisplayName("When me with invalid token, then returns unauthorized")
    void WhenMe_InvalidToken_ThenReturnsBadRequest(){
        String invalid = "invalid_token";

        when(authService.getCurrentUser(invalid)).thenReturn(null);

        RestAssuredMockMvc.given()
                .header("Authorization", invalid)
                .contentType("application/json")
                .when()
                .get("/api/auth/me")
                .then()
                .statusCode(400)
                .body("message", equalTo("Client not found"));


        verify(authService, times(1)).getCurrentUser(invalid);
    }

}