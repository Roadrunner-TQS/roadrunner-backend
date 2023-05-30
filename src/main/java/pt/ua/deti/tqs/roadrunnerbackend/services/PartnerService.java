package pt.ua.deti.tqs.roadrunnerbackend.services;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import pt.ua.deti.tqs.roadrunnerbackend.data.PackageRepository;
import pt.ua.deti.tqs.roadrunnerbackend.data.PickUpLocationRepository;
import pt.ua.deti.tqs.roadrunnerbackend.model.Package;
import pt.ua.deti.tqs.roadrunnerbackend.model.PickUpLocation;
import pt.ua.deti.tqs.roadrunnerbackend.model.dto.ErrorDTO;
import pt.ua.deti.tqs.roadrunnerbackend.model.enums.Status;

import java.util.UUID;


@Slf4j
@Service
@RequiredArgsConstructor
public class PartnerService {
    private final PickUpLocationRepository pickUpLocationRepository;
    private final PackageRepository packageRepository;


    public Object getAllPackages(UUID partnerId, String state) {
       return null;
    }

    public Object getPackageById(UUID pickUpLocationId, UUID packageId) {
        return null;
    }
}
