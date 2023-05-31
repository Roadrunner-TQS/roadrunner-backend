package pt.ua.deti.tqs.roadrunnerbackend.services;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.modelmapper.ModelMapper;
import pt.ua.deti.tqs.roadrunnerbackend.data.PickUpLocationRepository;
import pt.ua.deti.tqs.roadrunnerbackend.data.UserRepository;
import pt.ua.deti.tqs.roadrunnerbackend.model.PickUpLocation;
import pt.ua.deti.tqs.roadrunnerbackend.model.User;
import pt.ua.deti.tqs.roadrunnerbackend.model.auth.LoginRequest;
import pt.ua.deti.tqs.roadrunnerbackend.model.auth.LoginResponse;
import pt.ua.deti.tqs.roadrunnerbackend.model.auth.RegisterRequest;
import pt.ua.deti.tqs.roadrunnerbackend.model.dto.ErrorDTO;
import pt.ua.deti.tqs.roadrunnerbackend.model.dto.SuccessDTO;
import pt.ua.deti.tqs.roadrunnerbackend.model.dto.UserDto;
import pt.ua.deti.tqs.roadrunnerbackend.model.enums.Roles;

import java.util.Optional;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.instanceOf;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;


// unit tests with Mockito and Hamcrest
@ExtendWith(MockitoExtension.class)
class AuthServiceTest {

    private static final String TEST_EMAIL = "test@example.com";
    private static final String TEST_TOKEN = "testToken";
    private final ModelMapper modelMapper = new ModelMapper();

    @Mock(lenient = true)
    private UserRepository userRepository;

    @Mock(lenient = true)
    private PickUpLocationRepository pickUpLocationRepository;

    @Mock(lenient = true)
    private JwtTokenService jwtTokenService;

    @InjectMocks
    private AuthService authService;

    @BeforeEach
    void setUp() {
        authService.cleanWhiteList();
    }

    @Test
    void login_ValidCredentials_SuccessfulLoginResponse()  {
        String email = "test@mail.com";
        String password = "password";

        User user = new User();
        user.setEmail(email);
        user.setPassword(password);
        user.setRole(Roles.ROLE_PARTNER);

        LoginRequest loginRequest = new LoginRequest(email, password);

        when(userRepository.findByEmail(email)).thenReturn(Optional.of(user));
        when(jwtTokenService.generateToken(user)).thenReturn("mocked_token");
        when(jwtTokenService.getEmailFromToken("mocked_token")).thenReturn(email);
        when(jwtTokenService.validateToken("mocked_token")).thenReturn(true);


        Object result = authService.login(loginRequest);

        assertThat(result, instanceOf(LoginResponse.class));
        LoginResponse loginResponse = (LoginResponse) result;
        assertThat(loginResponse.getToken(), equalTo("mocked_token"));

    }

    @Test
    void login_InvalidCredentials_ErrorDTO() {
        String email = "test@example.com";
        String password = "password123";
        User user = new User();
        user.setEmail(email);
        user.setPassword("wrong_password");
        user.setRole(Roles.ROLE_PARTNER);

        LoginRequest loginRequest = new LoginRequest(email, password);

        when(userRepository.findByEmail(email)).thenReturn(Optional.of(user));

        Object result = authService.login(loginRequest);

        assertThat(result, instanceOf(ErrorDTO.class));
        ErrorDTO errorDTO = (ErrorDTO) result;
        assertThat(errorDTO.getMessage(), equalTo("Invalid credentials"));
    }

    @Test
    void login_NotAuthorized_ErrorDTO() {
        String email = "test@example.com";
        String password = "password123";
        User user = new User();
        user.setEmail(email);
        user.setPassword(password);
        user.setRole(Roles.ROLE_NOT_VERIFIED);

        LoginRequest loginRequest = new LoginRequest(email, password);

        when(userRepository.findByEmail(email)).thenReturn(Optional.of(user));

        Object result = authService.login(loginRequest);

        assertThat(result, instanceOf(ErrorDTO.class));
        ErrorDTO errorDTO = (ErrorDTO) result;
        assertThat(errorDTO.getMessage(), equalTo("Not Authorized"));
    }

    @Test
    void signup_NewUser_SuccessfulSignupResponse() {
        RegisterRequest userRequest = new RegisterRequest();
        userRequest.setEmail("test@example.com");
        userRequest.setPassword("password");
        userRequest.setFirstName("first name");
        userRequest.setLastName("last name");

        User existingUser = new User();
        existingUser.setEmail(userRequest.getEmail());
        when(userRepository.findByEmail(userRequest.getEmail())).thenReturn(Optional.empty());

        PickUpLocation existingPickUpLocation = new PickUpLocation();
        existingPickUpLocation.setName("name of location");
        existingPickUpLocation.setAddress("address");
        existingPickUpLocation.setLatitude(1L);
        existingPickUpLocation.setLongitude(1L);
        userRequest.setPickUpLocation(existingPickUpLocation);
        when(pickUpLocationRepository.findByAddress(userRequest.getPickUpLocation().getAddress())).thenReturn(Optional.empty());

        PickUpLocation savedPickUpLocation = new PickUpLocation();
        when(pickUpLocationRepository.save(any(PickUpLocation.class))).thenReturn(savedPickUpLocation);

        User savedUser = new User();
        when(userRepository.save(any(User.class))).thenReturn(savedUser);

        Object result = authService.signup(userRequest);

        assertThat(result, instanceOf(SuccessDTO.class));
        SuccessDTO<?> successDTO = (SuccessDTO<?>) result;
        assertThat(successDTO.getMessage(), equalTo("User created"));
    }

    @Test
    void signup_ExistingUser_ErrorDTO() {
        RegisterRequest userRequest = new RegisterRequest();
        userRequest.setEmail("existing@example.com");

        User existingUser = new User();
        when(userRepository.findByEmail(userRequest.getEmail())).thenReturn(Optional.of(existingUser));

        Object result = authService.signup(userRequest);

        assertThat(result, instanceOf(ErrorDTO.class));
        ErrorDTO errorDTO = (ErrorDTO) result;
        assertThat(errorDTO.getMessage(), equalTo("User already exists"));
    }

    @Test
    void signup_ExistingPickUpLocation_ErrorDTO() {
        RegisterRequest userRequest = new RegisterRequest();
        userRequest.setEmail("test@example.com");

        when(userRepository.findByEmail(userRequest.getEmail())).thenReturn(Optional.empty());

        PickUpLocation existingPickUpLocation = new PickUpLocation();
        existingPickUpLocation.setAddress("existing address");
        userRequest.setPickUpLocation(existingPickUpLocation);
        when(pickUpLocationRepository.findByAddress(userRequest.getPickUpLocation().getAddress())).thenReturn(Optional.of(existingPickUpLocation));

        Object result = authService.signup(userRequest);

        assertThat(result, instanceOf(ErrorDTO.class));
        ErrorDTO errorDTO = (ErrorDTO) result;
        assertThat(errorDTO.getMessage(), equalTo("PickUpLocation already exists"));
    }

    @Test
    void logout_WithValidToken_ShouldReturnTrue() {
        User user = new User();
        user.setEmail("test@mail.com");
        user.setPassword("password");
        user.setRole(Roles.ROLE_PARTNER);

        LoginRequest loginRequest = new LoginRequest("test@mail.com", "password");

        when(userRepository.findByEmail(loginRequest.getEmail())).thenReturn(Optional.of(user));
        when(jwtTokenService.generateToken(any(User.class))).thenReturn("token");
        when(jwtTokenService.getEmailFromToken("token")).thenReturn(loginRequest.getEmail());

        authService.login(loginRequest);

        Assertions.assertTrue(authService.logout("token"));

    }

    @Test
    void logout_WithInvalidToken_ShouldReturnFalse() {
        User user = new User();
        user.setEmail("test@mail.com");
        user.setPassword("password");
        user.setRole(Roles.ROLE_PARTNER);

        LoginRequest loginRequest = new LoginRequest("test@mail.com", "password");

        when(jwtTokenService.getEmailFromToken("token")).thenReturn(loginRequest.getEmail());

        authService.login(loginRequest);

        Assertions.assertFalse(authService.logout("invalid_token"));
    }

    @Test
    void logout_WithInvalidEmail_ShouldReturnFalse() {
        User user = new User();
        user.setEmail("test@mail.com");
        user.setPassword("password");
        user.setRole(Roles.ROLE_PARTNER);

        LoginRequest loginRequest = new LoginRequest("test@mail.com", "password");

        when(jwtTokenService.getEmailFromToken("invalid_token")).thenReturn("invalid_email");

        authService.login(loginRequest);

        Assertions.assertFalse(authService.logout("invalid_token"));
    }

    @Test
    void logout_WithOldToken_ShouldReturnFalse() {
        User user = new User();
        user.setEmail("test@mail.com");
        user.setPassword("password");
        user.setRole(Roles.ROLE_PARTNER);

        LoginRequest loginRequest = new LoginRequest("test@mail.com", "password");

        when(jwtTokenService.getEmailFromToken("old_token")).thenReturn(loginRequest.getEmail());

        authService.login(loginRequest);

        Assertions.assertFalse(authService.logout("old_token"));
    }

    @Test
    @DisplayName("Test isAuthenticated with valid token")
    void isAuthenticated_WithValidToken_ShouldReturnTrue() {
        String email = "test.example.com";
        String password = "password";

        User user = new User();
        user.setEmail(email);
        user.setPassword(password);
        user.setRole(Roles.ROLE_PARTNER);

        when(userRepository.findByEmail(email)).thenReturn(Optional.of(user));
        when(jwtTokenService.generateToken(user)).thenReturn("mocked_token");
        when(jwtTokenService.getEmailFromToken("mocked_token")).thenReturn(email);
        when(jwtTokenService.validateToken("mocked_token")).thenReturn(true);
        LoginRequest loginRequest = new LoginRequest(email, password);

        Object resultLogin = authService.login(loginRequest);

        assertThat(resultLogin, instanceOf(LoginResponse.class));
        LoginResponse loginResponse = (LoginResponse) resultLogin;
        assertThat(loginResponse.getToken(), equalTo("mocked_token"));

        Boolean resultIsAuthenticated = authService.isAuthenticated("Bearer mocked_token");
        assertThat(resultIsAuthenticated, instanceOf(Boolean.class));
        assertThat(resultIsAuthenticated, equalTo(true));
    }

    @Test
    @DisplayName("Test isAuthenticated with invalid token")
    void isAuthenticated_WithInvalidToken_ShouldReturnFalse() {
        String email = "test.example.com";
        String password = "password";

        User user = new User();
        user.setEmail(email);
        user.setPassword(password);
        user.setRole(Roles.ROLE_PARTNER);

        when(userRepository.findByEmail(email)).thenReturn(Optional.of(user));
        when(jwtTokenService.generateToken(user)).thenReturn("mocked_token");
        when(jwtTokenService.getEmailFromToken("mocked_token")).thenReturn(email);
        when(jwtTokenService.validateToken("mocked_token")).thenReturn(true);
        LoginRequest loginRequest = new LoginRequest(email, password);

        Object resultLogin = authService.login(loginRequest);

        assertThat(resultLogin, instanceOf(LoginResponse.class));
        LoginResponse loginResponse = (LoginResponse) resultLogin;
        assertThat(loginResponse.getToken(), equalTo("mocked_token"));

        Boolean resultIsAuthenticated = authService.isAuthenticated("Bearer invalid_token");
        assertThat(resultIsAuthenticated, instanceOf(Boolean.class));
        assertThat(resultIsAuthenticated, equalTo(false));
    }

    @Test
    @DisplayName("Test isAuthenticated with old token")
    void isAuthenticated_WithNotLoginDone_ReturnFalse(){
        Boolean resultIsAuthenticated = authService.isAuthenticated("Bearer mocked_token");
        assertThat(resultIsAuthenticated, instanceOf(Boolean.class));
        assertThat(resultIsAuthenticated, equalTo(false));
    }

    @Test
    @DisplayName("Test isAdmin with valid token, and user is admin")
    void isAdmin_WithTokenValid_UserISAdmin_ReturnTRUE(){
        String email = "test.example.com";
        String password = "password";

        User user = new User();
        user.setEmail(email);
        user.setPassword(password);
        user.setRole(Roles.ROLE_ADMIN);

        when(userRepository.findByEmail(email)).thenReturn(Optional.of(user));
        when(jwtTokenService.generateToken(user)).thenReturn("mocked_token");
        when(jwtTokenService.getEmailFromToken("mocked_token")).thenReturn(email);
        when(jwtTokenService.validateToken("mocked_token")).thenReturn(true);
        LoginRequest loginRequest = new LoginRequest(email, password);

        Object resultLogin = authService.login(loginRequest);

        assertThat(resultLogin, instanceOf(LoginResponse.class));
        LoginResponse loginResponse = (LoginResponse) resultLogin;
        assertThat(loginResponse.getToken(), equalTo("mocked_token"));

        Boolean resultIsAuthenticated = authService.isAuthenticated("Bearer mocked_token");
        assertThat(resultIsAuthenticated, instanceOf(Boolean.class));
        assertThat(resultIsAuthenticated, equalTo(true));

        Boolean resultIsAdmin = authService.isAdmin("Bearer mocked_token");
        assertThat(resultIsAdmin, instanceOf(Boolean.class));
        assertThat(resultIsAdmin, equalTo(true));

    }

    @Test
    @DisplayName("Test isAdmin with valid token, and user is not admin")
    void isAdmin_WithTokenValid_ReturnFALSE(){
        String email = "test.example.com";
        String password = "password";

        User user = new User();
        user.setEmail(email);
        user.setPassword(password);
        user.setRole(Roles.ROLE_PARTNER);

        when(userRepository.findByEmail(email)).thenReturn(Optional.of(user));
        when(jwtTokenService.generateToken(user)).thenReturn("mocked_token");
        when(jwtTokenService.getEmailFromToken("mocked_token")).thenReturn(email);
        when(jwtTokenService.validateToken("mocked_token")).thenReturn(true);
        LoginRequest loginRequest = new LoginRequest(email, password);

        Object resultLogin = authService.login(loginRequest);

        assertThat(resultLogin, instanceOf(LoginResponse.class));
        LoginResponse loginResponse = (LoginResponse) resultLogin;
        assertThat(loginResponse.getToken(), equalTo("mocked_token"));

        Boolean resultIsAuthenticated = authService.isAuthenticated("Bearer mocked_token");
        assertThat(resultIsAuthenticated, instanceOf(Boolean.class));
        assertThat(resultIsAuthenticated, equalTo(true));

        Boolean resultIsAdmin = authService.isAdmin("Bearer mocked_token");
        assertThat(resultIsAdmin, instanceOf(Boolean.class));
        assertThat(resultIsAdmin, equalTo(false));
    }

    @Test
    @DisplayName("Test isAdmin with user not authenticated")
    void isAdmin_WithUserNotAuthenticated_ReturnFALSE(){
        Boolean resultIsAdmin = authService.isAdmin("Bearer mocked_token");
        assertThat(resultIsAdmin, instanceOf(Boolean.class));
        assertThat(resultIsAdmin, equalTo(false));
    }

    @Test
    @DisplayName("Test isPartner with valid token, and user is partner")
    void isPartner_WithTokenValid_UserIsPartener_ReturnTRUE(){
        String email = "test.example.com";
        String password = "password";

        User user = new User();
        user.setEmail(email);
        user.setPassword(password);
        user.setRole(Roles.ROLE_PARTNER);

        when(userRepository.findByEmail(email)).thenReturn(Optional.of(user));
        when(jwtTokenService.generateToken(user)).thenReturn("mocked_token");
        when(jwtTokenService.getEmailFromToken("mocked_token")).thenReturn(email);
        when(jwtTokenService.validateToken("mocked_token")).thenReturn(true);
        LoginRequest loginRequest = new LoginRequest(email, password);

        Object resultLogin = authService.login(loginRequest);

        assertThat(resultLogin, instanceOf(LoginResponse.class));
        LoginResponse loginResponse = (LoginResponse) resultLogin;
        assertThat(loginResponse.getToken(), equalTo("mocked_token"));

        Boolean resultIsAuthenticated = authService.isAuthenticated("Bearer mocked_token");
        assertThat(resultIsAuthenticated, instanceOf(Boolean.class));
        assertThat(resultIsAuthenticated, equalTo(true));

        Boolean resultIsAdmin = authService.isPartner("Bearer mocked_token");
        assertThat(resultIsAdmin, instanceOf(Boolean.class));
        assertThat(resultIsAdmin, equalTo(true));

    }

    @Test
    @DisplayName("Test isPartner with valid token, and user is not Partner")
    void isPartner_WithTokenValid_ReturnFalse(){
        String email = "test.example.com";
        String password = "password";

        User user = new User();
        user.setEmail(email);
        user.setPassword(password);
        user.setRole(Roles.ROLE_ADMIN);

        when(userRepository.findByEmail(email)).thenReturn(Optional.of(user));
        when(jwtTokenService.generateToken(user)).thenReturn("mocked_token");
        when(jwtTokenService.getEmailFromToken("mocked_token")).thenReturn(email);
        when(jwtTokenService.validateToken("mocked_token")).thenReturn(true);
        LoginRequest loginRequest = new LoginRequest(email, password);

        Object resultLogin = authService.login(loginRequest);

        assertThat(resultLogin, instanceOf(LoginResponse.class));
        LoginResponse loginResponse = (LoginResponse) resultLogin;
        assertThat(loginResponse.getToken(), equalTo("mocked_token"));

        Boolean resultIsAuthenticated = authService.isAuthenticated("Bearer mocked_token");
        assertThat(resultIsAuthenticated, instanceOf(Boolean.class));
        assertThat(resultIsAuthenticated, equalTo(true));

        Boolean resultIsAdmin = authService.isPartner("Bearer mocked_token");
        assertThat(resultIsAdmin, instanceOf(Boolean.class));
        assertThat(resultIsAdmin, equalTo(false));
    }

    @Test
    @DisplayName("Test isPartner with user not authenticated")
    void isPartner_WithUserNotAuthenticated_ReturnFALSE(){
        Boolean resultIsAdmin = authService.isPartner("Bearer mocked_token");
        assertThat(resultIsAdmin, instanceOf(Boolean.class));
        assertThat(resultIsAdmin, equalTo(false));
    }

    @Test
    void getCurrentUser_WithValidToken_ShouldReturnCurrentUser() {
        String token = "valid_token";
        String email = "test@example.com";
        User user = new User();
        user.setEmail(email);
        UserDto expectedClientDTO = new UserDto();
        expectedClientDTO.setEmail(email);
        when(jwtTokenService.getEmailFromToken(token)).thenReturn(email);
        when(userRepository.findByEmail(email)).thenReturn(Optional.of(user));
        UserDto result = authService.getCurrentUser(token);
        assertNotNull(result);
        assertEquals(expectedClientDTO, result);
        verify(jwtTokenService, times(1)).getEmailFromToken(token);
        verify(userRepository, times(1)).findByEmail(email);
    }

    @Test
    void testCurrentUser_InvalidToken_ReturnsNull() {
        String token = "invalid_token";
        when(jwtTokenService.getEmailFromToken(token)).thenReturn(null);
        UserDto result = authService.getCurrentUser(token);
        assertNull(result);
        verify(jwtTokenService, times(1)).getEmailFromToken(token);
        verify(userRepository, never()).findByEmail(anyString());
    }











}