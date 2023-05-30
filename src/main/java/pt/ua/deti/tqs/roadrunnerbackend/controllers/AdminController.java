package pt.ua.deti.tqs.roadrunnerbackend.controllers;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import pt.ua.deti.tqs.roadrunnerbackend.model.Shop;
import pt.ua.deti.tqs.roadrunnerbackend.model.dto.ErrorDTO;
import pt.ua.deti.tqs.roadrunnerbackend.model.dto.SuccessDTO;
import pt.ua.deti.tqs.roadrunnerbackend.services.AdminService;

import java.util.UUID;

import static org.springframework.http.MediaType.APPLICATION_JSON;

@Slf4j
@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
public class AdminController {

    private final AdminService adminService;

    @DeleteMapping("/pickup/{id}")
    @PreAuthorize("@authService.isAdmin(#token)")
    public ResponseEntity<Object> deletePickUpLocation(@RequestHeader("Authorization") String token,
                                                       @PathVariable UUID id) {
        log.info("Admin Controller -- Delete pick up location -- request received");
        Object response = adminService.deletePickUpLocation(id);
        if (response instanceof ErrorDTO) {
            log.error("Admin Controller -- Delete pick up location -- " + ((ErrorDTO) response).getMessage());
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .contentType(APPLICATION_JSON)
                    .body(response);
        }
        log.info("Admin Controller -- Delete pick up location -- Pick Up Location removed");
        return ResponseEntity.status(HttpStatus.OK)
                .contentType(APPLICATION_JSON)
                .body(response);
    }

    @PutMapping("/pickup/{id}")
    @PreAuthorize("@authService.isAdmin(#token)")
    public ResponseEntity<Object> acceptPickUpLocation(@RequestHeader("Authorization") String token,
                                                       @PathVariable UUID id ) {
        log.info("Admin Controller -- Accept pick up location -- request received");
        Object response = adminService.acceptedPickUpLocation(id);
        if (response instanceof ErrorDTO) {
            log.error("Admin Controller -- Accept pick up location -- " + ((ErrorDTO) response).getMessage());
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .contentType(APPLICATION_JSON)
                    .body(response);
        }
        log.info("Admin Controller -- Accept pick up location -- Pick Up Location accepted");
        return ResponseEntity.status(HttpStatus.OK)
                .contentType(APPLICATION_JSON)
                .body(response);
    }
}
