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
        log.info("PartnerService -- getAllPackages -- request");
        if (!Status.contains(state) && state != null) {
            log.error("PartnerService -- getAllPackages -- State not valid");
            return new ErrorDTO("State not valid");
        }

        if (!pickUpLocationRepository.existsById(partnerId)) {
            log.error("PartnerService -- getAllPackages -- Partner not found");
            return new ErrorDTO("Partner not found");
        }
        if (state == null) {
            log.info("PartnerService -- getAllPackages -- Success (without state)");
            return packageRepository.findAllByPickUpLocationId(partnerId);
        }

        log.info("Partner Service -- getAllPackages -- Success (with state)");
        return packageRepository.findAllByPickUpLocationIdAndStatus(partnerId, Status.valueOf(state));
    }

    public Object getPackageById(UUID pickUpLocationId, UUID packageId) {
        log.info("Partner Service -- Getting package by id");
        Package pack = packageRepository.findById(packageId).orElse(null);
        if (pack == null) {
            log.info("Partner Service -- getPackageById -- Package not found");
            return new ErrorDTO("Package not found");
        }
        PickUpLocation pickUpLocation = pickUpLocationRepository.findById(pack.getPickUpLocation().getId()).orElse(null);
        if (pickUpLocation == null) {
            log.info("Partner Service -- getPackageById -- PickUpLocation not found");
            return new ErrorDTO("PickUpLocation not found");
        }

        if (!pack.getPickUpLocation().getId().equals(pickUpLocationId)) {
            log.info("Partner Service -- getPackageById -- Package not Authorized");
            return new ErrorDTO("Package not Authorized");
        }
        log.info("Partner Service -- Package found");
        return pack;
    }
}
