package pt.ua.deti.tqs.roadrunnerbackend.services;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import pt.ua.deti.tqs.roadrunnerbackend.data.PackageRepository;
import pt.ua.deti.tqs.roadrunnerbackend.data.PickUpLocationRepository;
import pt.ua.deti.tqs.roadrunnerbackend.data.ShopRepository;
import pt.ua.deti.tqs.roadrunnerbackend.data.UserRepository;

import java.util.UUID;

@Slf4j
@Service
@RequiredArgsConstructor
public class AdminService {

    private final PackageRepository packageRepository;
    private final PickUpLocationRepository pickUpLocationRepository;
    private final ShopRepository shopRepository;
    private final UserRepository userRepository;

    public Object deletePickUpLocation(UUID pickUpLocationId) {
        return null;
    }

    public Object acceptedPickUpLocation(UUID pickUpLocationId) {
        return null;
    }

}
