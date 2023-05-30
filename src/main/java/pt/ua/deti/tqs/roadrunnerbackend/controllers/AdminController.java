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

    @GetMapping("/shop")
    @PreAuthorize("@authService.isAdmin(#token)")
    public ResponseEntity<Object> getShops(@RequestHeader("Authorization") String token) {
        return null;
    }

    @PostMapping("/shop")
    @PreAuthorize("@authService.isAdmin(#token)")
    public ResponseEntity<Object> addShop(@RequestHeader("Authorization") String token,
                                          @RequestBody Shop shopRequest) {
        return null;
    }

    @GetMapping("/shop/{id}")
    @PreAuthorize("@authService.isAdmin(#token)")
    public ResponseEntity<Object> getShop(@RequestHeader("Authorization") String token,
                                          @PathVariable UUID id){
        return null;
    }

    @DeleteMapping("/shop/{id}")
    @PreAuthorize("@authService.isAdmin(#token)")
    public ResponseEntity<Object> deleteShop(@RequestHeader("Authorization") String token,
                                             @PathVariable UUID id) {
        return null;
    }

    @GetMapping("/shop/package")
    @PreAuthorize("@authService.isAdmin(#token)")
    public ResponseEntity<Object> getPackagesByShop(@RequestHeader("Authorization") String token,
                                                    @RequestParam(value="id") UUID id){
        return null;
    }

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


    @GetMapping("/pickup")
    @PreAuthorize("@authService.isAdmin(#token)")
    public ResponseEntity<Object> getPickUpLocations(@RequestHeader("Authorization") String token,
                                                     @RequestParam(value="city", required = false) String city,
                                                     @RequestParam(value="accepted", required = false) Boolean accepted) {
        log.info("RoadRunnerController -- Get pick up locations -- request received");
        Object response = adminService.getPickUpLocations(city, accepted);
        if (response instanceof ErrorDTO) {
            log.error("RoadRunnerController -- Get pick up locations -- " + ((ErrorDTO) response).getMessage());
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .contentType(APPLICATION_JSON)
                    .body(response);
        }
        log.info("RoadRunnerController -- Get pick up locations -- pick Up Locations found");
        return ResponseEntity.status(HttpStatus.OK)
                .contentType(APPLICATION_JSON)
                .body(response);
    }

    @PreAuthorize("@authService.isAdmin(#token)")
    @GetMapping("/pickup/package")
    public ResponseEntity<Object> getPackagesByPickUpLocation(@RequestHeader("Authorization") String token,
                                                              @RequestParam(value="pickupId") UUID pickupId){
        log.info("RoadRunnerController -- Get packages by pick up location -- request received");
        Object response = adminService.getPackagesByPickUpLocation(pickupId);
        if (response instanceof ErrorDTO) {
            log.error("RoadRunnerController -- Get packages by pick up location -- " + ((ErrorDTO) response).getMessage());
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .contentType(APPLICATION_JSON)
                    .body(response);
        }
        log.info("RoadRunnerController -- Get packages by pick up location -- Packages found");
        return ResponseEntity.status(HttpStatus.OK)
                .contentType(APPLICATION_JSON)
                .body(response);
    }

}
