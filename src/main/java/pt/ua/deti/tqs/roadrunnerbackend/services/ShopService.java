package pt.ua.deti.tqs.roadrunnerbackend.services;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import pt.ua.deti.tqs.roadrunnerbackend.data.*;
import pt.ua.deti.tqs.roadrunnerbackend.model.Package;
import pt.ua.deti.tqs.roadrunnerbackend.model.*;
import pt.ua.deti.tqs.roadrunnerbackend.model.dto.HistoryStateDTO;
import pt.ua.deti.tqs.roadrunnerbackend.model.dto.OrderDto;
import pt.ua.deti.tqs.roadrunnerbackend.model.enums.Status;

import java.util.List;
import java.util.Objects;
import java.util.UUID;

@Slf4j
@Service
@RequiredArgsConstructor
public class ShopService {
    private final PickUpLocationRepository pickUpLocationRepository;
    private final CustomerRepository customerRepository;
    private final PackageRepository packageRepository;
    private final StateRepository stateRepository;
    private final ShopRepository shopRepository;
    private final AuthService authService;

    public UUID createPackage(OrderDto orderDto){
        return null;
    }
    public boolean updatePackage(UUID packId, String newState, String token ) {
        return false;
    }

    public List<PickUpLocation> getAllPickUpLocations() {
        return null;
    }


    public HistoryStateDTO getHistory() {
        return null;
    }

}
