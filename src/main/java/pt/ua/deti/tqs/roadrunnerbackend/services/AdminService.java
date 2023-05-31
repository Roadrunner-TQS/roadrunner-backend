package pt.ua.deti.tqs.roadrunnerbackend.services;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import pt.ua.deti.tqs.roadrunnerbackend.data.PackageRepository;
import pt.ua.deti.tqs.roadrunnerbackend.data.PickUpLocationRepository;
import pt.ua.deti.tqs.roadrunnerbackend.data.ShopRepository;
import pt.ua.deti.tqs.roadrunnerbackend.data.UserRepository;
import pt.ua.deti.tqs.roadrunnerbackend.model.Package;
import pt.ua.deti.tqs.roadrunnerbackend.model.PickUpLocation;
import pt.ua.deti.tqs.roadrunnerbackend.model.Shop;
import pt.ua.deti.tqs.roadrunnerbackend.model.User;
import pt.ua.deti.tqs.roadrunnerbackend.model.dto.ErrorDTO;
import pt.ua.deti.tqs.roadrunnerbackend.model.dto.SuccessDTO;
import pt.ua.deti.tqs.roadrunnerbackend.model.enums.Roles;
import pt.ua.deti.tqs.roadrunnerbackend.model.enums.Status;

import java.util.List;
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


    public Object getPackages(String state){
        log.info("Admin service -- getPackages -- request received");
        if (!Status.contains(state) && state != null) {
            log.error("Admin service -- getPackages -- State not valid");
            return new ErrorDTO("State not valid");
        }
        if (state == null) {
            log.info("Admin service -- getPackages -- Sucess (without state)");
            return packageRepository.findAll();
        }
        log.info("Admin service -- getPackages -- Sucess (with state)");
        return packageRepository.findByStatus(Status.valueOf(state));
    }

    public Object getPackageById(UUID packageId){
        log.info("Admin service -- getPackageById -- request received");
         Package pack = packageRepository.findById(packageId).orElse(null);
        if (pack == null) {
            log.error("Admin service -- getPackageById -- Package not found");
            return new ErrorDTO("Package not found");
        }
        log.info("Admin service -- getPackageById -- Package found");
        return pack;
    }

    public Object getShops() {
        log.info("Admin service -- getShops -- request received");
        List<Shop> shops = shopRepository.findALLByDisabled(false);
        log.info("Admin service -- getShops -- Sucess");
        return shops;
    }

    public Shop addShop(Shop shop) {
        log.info("Admin service -- addShop -- request received");
        try {
            shop.setSlugs(shop.getName().toLowerCase().replaceAll("\\s+", "-"));
            shopRepository.save(shop);
            log.info("Admin service -- addShop -- Shop added");
            return shop;
        } catch (Exception e) {
            log.error("Admin service -- addShop -- Error adding shop");
            return null;
        }
    }

    public Object getShopById(UUID shopId) {
        log.info("Admin service -- getShop -- request received");
        Shop shop = shopRepository.findByIdAndDisabled(shopId, false).orElse(null);
        if (shop == null) {
            log.error("Admin service -- getShop -- Shop not found");
            return new ErrorDTO("Shop not found");
        }
        log.info("Admin service -- getShop -- Shop found");
        return shop;
    }

    public Boolean deleteShop(UUID shopId) {
        log.info("Admin service -- deleteShop -- request received");
        Shop shop = shopRepository.findByIdAndDisabled(shopId, false).orElse(null);
        if (shop == null) {
            log.error("Admin service -- deleteShop -- Shop not found");
            return false;
        }
        shop.setDisabled(true);
        shopRepository.save(shop);
        log.info("Admin service -- deleteShop -- Shop deleted");
        return true;
    }

    public Object getPackagesByShop(UUID shopId) {
        log.info("Admin service -- getPackagesByShop -- request received");
        Shop shop = shopRepository.findByIdAndDisabled(shopId,false).orElse(null);
        if (shop == null) {
            log.error("Admin service -- getPackagesByShop -- Shop not found");
            return new ErrorDTO("Shop not found");
        }
        log.info("Admin service -- getPackagesByShop -- Sucess");
        return packageRepository.findAllByShopId(shopId);
    }

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
