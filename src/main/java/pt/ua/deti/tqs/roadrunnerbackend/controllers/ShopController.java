package pt.ua.deti.tqs.roadrunnerbackend.controllers;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import pt.ua.deti.tqs.roadrunnerbackend.model.dto.OrderDto;
import pt.ua.deti.tqs.roadrunnerbackend.services.ShopService;

import java.util.UUID;

@Slf4j
@RestController
@RequestMapping("/api/shop")
@RequiredArgsConstructor
public class ShopController {

    private final ShopService shopService;

    @PostMapping("/package")
    public ResponseEntity<Object> createPackage(@RequestBody OrderDto orderDto) {
        return null;
    }

    @PutMapping("/package/{id}")
    public ResponseEntity<Object> updatePackage(@PathVariable UUID id, @RequestParam String newstate,
                                                @RequestHeader("Authorization") String token) {
       return null;
    }

    @GetMapping("/pickuplocation")
    public ResponseEntity<Object> getAllPickUpLocations() {
       return null;
    }

    @GetMapping("/history")
    public ResponseEntity<Object> getHistory() {
        return null;
    }
}