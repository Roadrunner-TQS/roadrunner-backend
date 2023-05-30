package pt.ua.deti.tqs.roadrunnerbackend.services;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import pt.ua.deti.tqs.roadrunnerbackend.config.JwtConfig;
import pt.ua.deti.tqs.roadrunnerbackend.model.User;
import pt.ua.deti.tqs.roadrunnerbackend.model.enums.Roles;

import java.security.Key;
import java.util.concurrent.TimeUnit;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;
import static org.testcontainers.shaded.org.awaitility.Awaitility.await;

@ExtendWith(MockitoExtension.class)
class JwtTokenServiceTest {
    @Mock
    private JwtConfig jwtConfig;

    private Key jwtKey;

    private JwtTokenService jwtTokenService;

    @BeforeEach
    void setup() {
        jwtKey = Keys.secretKeyFor(SignatureAlgorithm.HS512);
        jwtTokenService = new JwtTokenService(jwtConfig, jwtKey);
    }

    @Test
    @DisplayName("When generateToken is called then a valid token is returned")
    void generateToken_ShouldReturnValidToken() {
        when(jwtConfig.getExpiration()).thenReturn(600);
        User user = new User();
        user.setEmail("email");
        user.setRole(Roles.ROLE_PARTNER);

        String token = jwtTokenService.generateToken(user);

        assertNotNull(token);
        assertTrue(jwtTokenService.validateToken(token));
    }

    @Test
    @DisplayName("When an invalid token is passed to validateToken then false is returned")
    void validateToken_withInvalidToken_ShouldReturnFalse() {
        assertFalse(jwtTokenService.validateToken("invalid_token"));
    }
    @Test
    @DisplayName("When an expired token is passed to validateToken then false is returned")
    void validateToken_withExpiredToken_ShouldReturnFalse() throws InterruptedException {
        when(jwtConfig.getExpiration()).thenReturn(1);
        User user = new User();
        user.setEmail("email");
        user.setRole(Roles.ROLE_PARTNER);

        String token = jwtTokenService.generateToken(user);

        assertNotNull(token);
        await().atMost(1, TimeUnit.SECONDS).untilAsserted(() -> {
            assertFalse(jwtTokenService.validateToken(token));
        });
        assertFalse(jwtTokenService.validateToken(token));
    }

    @Test
    @DisplayName("When a valid token is passed to getEmailFromToken then the email is returned")
    void getEmailFromToken_ShouldReturnEmail() {
        String token = Jwts.builder().setSubject("username").signWith(jwtKey).compact();
        String email = jwtTokenService.getEmailFromToken(token);
        assertEquals("username", email);
    }

    @Test
    @DisplayName("When an invalid token is passed to getEmailFromToken then null is returned")
    void getEmailFromToken_ShouldReturnNull() {
        String email = jwtTokenService.getEmailFromToken("invalid_token");
        assertNull(email);
    }


    @Test
    @DisplayName("When a valid token is passed to getRoleFromToken then the role is returned")
    void getRoleFromToken_withValidToken_ShouldReturnRole() {
        String token = Jwts.builder().claim("role", Roles.ROLE_PARTNER).signWith(jwtKey).compact();
        Roles role = jwtTokenService.getRoleFromToken(token);
        assertEquals(Roles.ROLE_PARTNER, role);
    }

    @Test
    @DisplayName("When an invalid token is passed to getRoleFromToken then null is returned")
    void getRoleFromToken_withInvalidToken_ShouldReturnNull() {
        Roles role = jwtTokenService.getRoleFromToken("invalid_token");
        assertNull(role);
    }

    @Test
    @DisplayName("When an expired token is passed to getRoleFromToken then the role is null")
    void getRoleFromToken_withExpiredToken_ShouldReturnNull(){
        when(jwtConfig.getExpiration()).thenReturn(1);
        User user = new User();
        user.setEmail("email");
        user.setRole(Roles.ROLE_PARTNER);

        String token = jwtTokenService.generateToken(user);

        assertNotNull(token);
        await().atMost(1, TimeUnit.SECONDS).untilAsserted(() -> {
            assertNull(jwtTokenService.getRoleFromToken(token));
        });
        assertNull(jwtTokenService.getRoleFromToken(token));
    }

    @Test
    @DisplayName("When a valid token is passed to getRoleFromToken then the role is returned")
    void getRoleFromToken_withInvalidRole_ShouldReturnNull() {
        String token = Jwts.builder().claim("role", "invalid_role").signWith(jwtKey).compact();
        Roles role = jwtTokenService.getRoleFromToken(token);
        assertNull(role);
    }

    @Test
    @DisplayName("When a valid token is passed to getRoleFromToken then the role is returned")
    void getRoleFromToken_withNullRole_ShouldReturnNull() {
        String token = Jwts.builder().signWith(jwtKey).compact();
        Roles role = jwtTokenService.getRoleFromToken(token);
        assertNull(role);
    }
}