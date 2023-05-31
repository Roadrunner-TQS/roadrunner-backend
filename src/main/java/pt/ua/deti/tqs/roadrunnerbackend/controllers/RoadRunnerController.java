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
        return null;
    }

    @GetMapping("/package")
    @PreAuthorize("@authService.isAuthenticated(#token)")
    public ResponseEntity<Object> getPackages(@RequestHeader("Authorization") String token,
                                              @RequestParam(value="state", required = false) String state) {
       return null;
    }

    @GetMapping("/package/{id}")
    @PreAuthorize("@authService.isAuthenticated(#token)")
    public ResponseEntity<Object> getPackageById(@RequestHeader("Authorization") String token,
                                                 @PathVariable UUID id){
        return null;
    }
}
