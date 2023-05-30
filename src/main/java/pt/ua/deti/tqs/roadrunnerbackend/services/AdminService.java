package pt.ua.deti.tqs.roadrunnerbackend.services;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import pt.ua.deti.tqs.roadrunnerbackend.data.PackageRepository;
import pt.ua.deti.tqs.roadrunnerbackend.data.PickUpLocationRepository;
import pt.ua.deti.tqs.roadrunnerbackend.data.ShopRepository;
import pt.ua.deti.tqs.roadrunnerbackend.data.UserRepository;
import pt.ua.deti.tqs.roadrunnerbackend.model.PickUpLocation;
import pt.ua.deti.tqs.roadrunnerbackend.model.User;
import pt.ua.deti.tqs.roadrunnerbackend.model.dto.ErrorDTO;
import pt.ua.deti.tqs.roadrunnerbackend.model.dto.SuccessDTO;
import pt.ua.deti.tqs.roadrunnerbackend.model.enums.Roles;

import java.util.UUID;

@Slf4j
@Service
@RequiredArgsConstructor
public class AdminService {

    private final PackageRepository packageRepository;
    private final PickUpLocationRepository pickUpLocationRepository;
    private final ShopRepository shopRepository;
    private final UserRepository userRepository;
    private static final ErrorDTO ERROR_PICKUP = new ErrorDTO("PickUpLocation not found");
    public Object getPackagesByPickUpLocation(UUID id){
        log.info("Admin service -- getPackagesByPickUpLocation -- request received");

        PickUpLocation pickUpLocation = pickUpLocationRepository.findByIdAndDisable(id,false).orElse(null);
        if (pickUpLocation == null) {
            log.error("Admin service -- getPackagesByPickUpLocation -- PickUpLocation not found");
            return ERROR_PICKUP;
        }
        log.info("Admin service -- getPackagesByPickUpLocation -- Sucess");
        return packageRepository.findByPickUpLocation(pickUpLocation);
    }

    public Object deletePickUpLocation(UUID pickUpLocationId) {
        log.info("Admin service -- deletePickUpLocation -- request received");
        PickUpLocation pickUpLocation = pickUpLocationRepository.findByIdAndDisable(pickUpLocationId, false).orElse(null);
        if (pickUpLocation == null) {
            log.error("Admin service -- deletePickUpLocation -- PickUpLocation not found");
            return ERROR_PICKUP;
        }
        User user = userRepository.findByPickUpLocation(pickUpLocation).orElse(null);
        log.info("Admin service -- deletePickUpLocation -- PickUpLocation found");
        log.info("Admin service -- deletePickUpLocation -- " + user);
        if (user != null && user.getRole() != Roles.ROLE_ADMIN) {
            user.setRole(Roles.ROLE_DELETE);
            userRepository.save(user);
            pickUpLocation.setDisable(true);
            pickUpLocationRepository.save(pickUpLocation);
            log.info("Admin service -- deletePickUpLocation -- Sucess");
            return new SuccessDTO<String>("PickUpLocation deleted");
        }
        log.error("Admin service -- deletePickUpLocation -- PickUpLocation has no user");
        log.error("Admin service -- deletePickUpLocation -- Error to delete PickUpLocation");
        return new ErrorDTO("Error to delete PickUpLocation");
    }

    public Object acceptedPickUpLocation(UUID pickUpLocationId) {
        log.info("Admin service -- acceptedPickUpLocation -- request received");
        PickUpLocation pickUpLocation = pickUpLocationRepository.findByIdAndDisable(pickUpLocationId, false).orElse(null);
        if (pickUpLocation == null) {
            log.error("Admin service -- acceptedPickUpLocation -- PickUpLocation not found");
            return ERROR_PICKUP;
        }
        if (Boolean.TRUE.equals(pickUpLocation.getAccepted())) {
            log.error("Admin service -- acceptedPickUpLocation -- PickUpLocation already accepted");
            return new ErrorDTO("PickUpLocation already accepted");
        }
        User user = userRepository.findByPickUpLocation(pickUpLocation).orElse(null);
        if (user == null) {
            log.error("Admin service -- acceptedPickUpLocation -- User not found");
            return new ErrorDTO("User not found");
        }
        user.setRole(Roles.ROLE_PARTNER);
        pickUpLocation.setAccepted(true);
        userRepository.save(user);
        pickUpLocationRepository.save(pickUpLocation);
        log.info("Admin service -- acceptedPickUpLocation -- PickUpLocation accepted");
        return new SuccessDTO<String>("PickUpLocation accepted");
    }

    public Object getPickUpLocationById(UUID pickUpLocationId) {
        log.info("Admin service -- getPickUpLocationById -- request received");
        PickUpLocation pickUpLocation = pickUpLocationRepository.findByIdAndDisable(pickUpLocationId, false).orElse(null);
        if (pickUpLocation == null) {
            log.error("Admin service -- getPickUpLocationById -- PickUpLocation not found");
            return ERROR_PICKUP;
        }
        log.info("Admin service -- getPickUpLocationById -- PickUpLocation found");
        return pickUpLocation;
    }

    public Object getPickUpLocations(String city, Boolean accepted){
        log.info("RoadRunnerService -- getPickUpLocations -- request received");
        if (city == null && accepted == null) {
            log.info("RoadRunnerService-- getPickUpLocations -- Sucess (without city and accepted)");
            return pickUpLocationRepository.findByDisable(false);
        }
        if (city == null) {
            log.error("RoadRunnerService-- getPickUpLocations -- Without city");
            return pickUpLocationRepository.findByAcceptedAndDisable(accepted, false);
        }
        if (accepted == null) {
            log.error("RoadRunnerService -- getPickUpLocations -- Without accepted");
            return pickUpLocationRepository.findByCityAndDisable(city, false);
        }
        log.error("RoadRunnerService -- getPickUpLocations -- With city and accepted");
        return pickUpLocationRepository.findByCityAndAcceptedAndDisable(city, accepted, false);
    }

}
