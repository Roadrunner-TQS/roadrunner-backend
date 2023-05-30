package pt.ua.deti.tqs.roadrunnerbackend.controllers;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import pt.ua.deti.tqs.roadrunnerbackend.model.auth.LoginRequest;
import pt.ua.deti.tqs.roadrunnerbackend.model.auth.RegisterRequest;
import pt.ua.deti.tqs.roadrunnerbackend.services.AuthService;


@Slf4j
@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthController {

    private final AuthService authService;

    @PostMapping("/signup")
    public ResponseEntity<Object> signup(@RequestBody RegisterRequest registerRequest) {
        return null;
    }

    @PostMapping("/login")
    public ResponseEntity<Object> login(@RequestBody LoginRequest loginRequest) {
        return null;
    }

    @PutMapping("/logout")
    @PreAuthorize("@authService.isAuthenticated(#token)")
    public ResponseEntity<Object> logout(@RequestHeader("Authorization") String token){
        return null;
    }

    @GetMapping("/me")
    @PreAuthorize("@authService.isAuthenticated(#token)")
    public ResponseEntity<Object> me(@RequestHeader("Authorization") String token){
        return null;
    }

}
