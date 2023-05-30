package pt.ua.deti.tqs.roadrunnerbackend.controllers;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import pt.ua.deti.tqs.roadrunnerbackend.model.PickUpLocation;
import pt.ua.deti.tqs.roadrunnerbackend.model.dto.ErrorDTO;
import pt.ua.deti.tqs.roadrunnerbackend.model.dto.HistoryStateDTO;
import pt.ua.deti.tqs.roadrunnerbackend.model.dto.OrderDto;
import pt.ua.deti.tqs.roadrunnerbackend.model.dto.SuccessDTO;
import pt.ua.deti.tqs.roadrunnerbackend.services.ShopService;

import java.util.List;
import java.util.UUID;

import static org.springframework.http.MediaType.APPLICATION_JSON;

@Slf4j
@RestController
@RequestMapping("/api/shop")
@RequiredArgsConstructor
public class ShopController {

    private final ShopService shopService;

    @PostMapping("/package")
    public ResponseEntity<Object> createPackage(@RequestBody OrderDto orderDto) {
        log.info("BookStoreController -- createPackage -- request");
        UUID packageId = shopService.createPackage(orderDto);
        if (packageId== null) {
            log.error("BookStoreController -- createPackage -- failed");
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).
                    contentType(APPLICATION_JSON)
                    .body(new ErrorDTO("CREATE PACKAGE FAILED"));
        }
        log.info("BookStoreController -- createPackage -- successful");
        return ResponseEntity.status(HttpStatus.CREATED)
                .contentType(APPLICATION_JSON)
                .body(new SuccessDTO<UUID>(packageId));
    }

    @PutMapping("/package/{id}")
    public ResponseEntity<Object> updatePackage(@PathVariable UUID id, @RequestParam String newstate,
                                                @RequestHeader("Authorization") String token) {
        log.info("BookStoreController -- updatePackage -- request");
        if (!shopService.updatePackage(id, newstate, token)) {
            log.error("BookStoreController -- updatePackage -- failed");
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).
                    contentType(APPLICATION_JSON)
                    .body(new ErrorDTO("UPDATE PACKAGE FAILED"));
        }
        log.info("BookStoreController -- updatePackage -- successful");
        return ResponseEntity.status(HttpStatus.OK)
                .contentType(APPLICATION_JSON)
                .body(new SuccessDTO<String>("PACKAGE UPDATED"));
    }

    @GetMapping("/pickuplocation")
    public ResponseEntity<Object> getAllPickUpLocations() {
        log.info("BookStoreController -- getAllPickUpLocations -- request");
        List<PickUpLocation> pickUpLocations = shopService.getAllPickUpLocations();
        if (pickUpLocations.isEmpty()){
            log.error("BookStoreController -- getAllPickUpLocations -- failed");
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).
                    contentType(APPLICATION_JSON)
                    .body(new ErrorDTO("NO PICK UP LOCATIONS FOUND"));
        }
        log.info("BookStoreController -- getAllPickUpLocations -- successful");
        return ResponseEntity.status(HttpStatus.OK)
                .contentType(APPLICATION_JSON)
                .body(new SuccessDTO<List<PickUpLocation>>(pickUpLocations));
    }

    @GetMapping("/history")
    public ResponseEntity<Object> getHistory() {
        log.info("BookStoreController -- getHistory -- request");
        HistoryStateDTO packageHistory = shopService.getHistory();
        log.info("BookStoreController -- getHistory -- successful");
        return ResponseEntity.status(HttpStatus.OK)
                .contentType(APPLICATION_JSON)
                .body(packageHistory);
    }
}