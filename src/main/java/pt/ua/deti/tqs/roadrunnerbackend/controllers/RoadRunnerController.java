package pt.ua.deti.tqs.roadrunnerbackend.controllers;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import pt.ua.deti.tqs.roadrunnerbackend.model.dto.ErrorDTO;
import pt.ua.deti.tqs.roadrunnerbackend.services.RoadRunnerService;

import java.util.UUID;

import static org.springframework.http.MediaType.APPLICATION_JSON;

@Slf4j
@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
public class RoadRunnerController {

    private final RoadRunnerService roadRunnerService;

    @GetMapping("/pickup/{id}")
    @PreAuthorize("@authService.isAuthenticated(#token)")
    public ResponseEntity<Object> getPickUpLocationById(@RequestHeader("Authorization") String token,
                                                        @PathVariable UUID id){
        log.info("RoadRunnerController -- Get pick up location by id -- request received");
        Object response = roadRunnerService.getPickUpLocationById(id, token);
        if (response instanceof ErrorDTO) {
            log.error("RoadRunnerController -- Get pick up location by id -- " + ((ErrorDTO) response).getMessage());
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .contentType(APPLICATION_JSON)
                    .body(response);
        }
        log.info("RoadRunnerController -- Get pick up location by id -- Pick Up Location found");
        return ResponseEntity.status(HttpStatus.OK)
                .contentType(APPLICATION_JSON)
                .body(response);
    }

    @GetMapping("/package")
    @PreAuthorize("@authService.isAuthenticated(#token)")
    public ResponseEntity<Object> getPackages(@RequestHeader("Authorization") String token,
                                              @RequestParam(value="state", required = false) String state) {
        log.info("RoadRunnerController -- Get packages -- request received");
        Object response = roadRunnerService.getAllPackages(state, token);
        if (response instanceof ErrorDTO) {
            log.error("RoadRunnerController -- Get packages -- " + ((ErrorDTO) response).getMessage());
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .contentType(APPLICATION_JSON)
                    .body(response);
        }
        log.info("RoadRunnerController -- Get packages -- Packages found");
        return ResponseEntity.status(HttpStatus.OK)
                .contentType(APPLICATION_JSON)
                .body(response);
    }

    @GetMapping("/package/{id}")
    @PreAuthorize("@authService.isAuthenticated(#token)")
    public ResponseEntity<Object> getPackageById(@RequestHeader("Authorization") String token,
                                                 @PathVariable UUID id){
        log.info("RoadRunnerController -- Get package by id -- request received");
        Object response = roadRunnerService.getPackageById(id, token);
        if (response instanceof ErrorDTO) {
            log.error("RoadRunnerController -- Get package by id -- " + ((ErrorDTO) response).getMessage());
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .contentType(APPLICATION_JSON)
                    .body(response);
        }
        log.info("RoadRunnerController -- Get package by id -- Package found");
        return ResponseEntity.status(HttpStatus.OK)
                .contentType(APPLICATION_JSON)
                .body(response);
    }
}
