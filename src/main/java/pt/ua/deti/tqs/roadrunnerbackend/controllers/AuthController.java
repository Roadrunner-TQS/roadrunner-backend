package pt.ua.deti.tqs.roadrunnerbackend.controllers;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import pt.ua.deti.tqs.roadrunnerbackend.model.auth.LoginRequest;
import pt.ua.deti.tqs.roadrunnerbackend.model.auth.RegisterRequest;
import pt.ua.deti.tqs.roadrunnerbackend.model.dto.ErrorDTO;
import pt.ua.deti.tqs.roadrunnerbackend.model.dto.SuccessDTO;
import pt.ua.deti.tqs.roadrunnerbackend.model.dto.UserDto;
import pt.ua.deti.tqs.roadrunnerbackend.services.AuthService;

import static org.springframework.http.MediaType.APPLICATION_JSON;

@Slf4j
@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthController {

    private final AuthService authService;

    @PostMapping("/signup")
    public ResponseEntity<Object> signup(@RequestBody RegisterRequest registerRequest) {
        log.info("AuthControllers -- signup --  Request received");
        Object response = authService.signup(registerRequest);
        if (response instanceof ErrorDTO) {
            log.error("AuthControllers -- signup -- " + ((ErrorDTO) response).getMessage());
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .contentType(APPLICATION_JSON)
                    .body(response);
        }

        log.info("AuthControllers -- signup -- " + ((SuccessDTO<?>) response).getMessage());
        return ResponseEntity.status(HttpStatus.CREATED)
                .contentType(APPLICATION_JSON)
                .body(response);
    }

    @PostMapping("/login")
    public ResponseEntity<Object> login(@RequestBody LoginRequest loginRequest) {
        log.info("AuthControllers -- login -- Request received");
        Object response = authService.login(loginRequest);
        if (response instanceof ErrorDTO) {
            log.error("AuthControllers -- login Request -- " + ((ErrorDTO) response).getMessage());
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .contentType(APPLICATION_JSON)
                    .body(response);
        }
        log.info("AuthControllers -- login Request -- Login successful");
        return ResponseEntity.status(HttpStatus.OK)
                .contentType(APPLICATION_JSON)
                .body(response);
    }

    @PutMapping("/logout")
    @PreAuthorize("@authService.isAuthenticated(#token)")
    public ResponseEntity<Object> logout(@RequestHeader("Authorization") String token){
        log.info("AuthControllers -- logout -- Request received");
        if (Boolean.TRUE.equals(authService.logout(token))){
            log.info("AuthControllers -- logout -- Logout successful");
            return ResponseEntity.status(HttpStatus.OK)
                    .contentType(APPLICATION_JSON)
                    .body(new SuccessDTO<>("Logout successful"));
        }
        log.error("AuthControllers -- logout -- Invalid credentials");
        return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                .contentType(APPLICATION_JSON)
                .body(new ErrorDTO("Invalid credentials"));
    }

    @GetMapping("/me")
    @PreAuthorize("@authService.isAuthenticated(#token)")
    public ResponseEntity<Object> me(@RequestHeader("Authorization") String token){
        log.info("AuthControllers -- me -- Request received");
        UserDto client = authService.getCurrentUser(token);
        if (client == null) {
            log.error("AuthControllers -- me -- Client not found");
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .contentType(APPLICATION_JSON)
                    .body(new ErrorDTO("Client not found"));
        }
        log.info("AuthControllers -- me -- Client found");
        return ResponseEntity.status(HttpStatus.OK)
                .contentType(APPLICATION_JSON)
                .body(client);
    }

}
