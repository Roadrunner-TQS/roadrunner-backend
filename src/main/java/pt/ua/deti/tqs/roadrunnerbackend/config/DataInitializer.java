package pt.ua.deti.tqs.roadrunnerbackend.config;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;
import pt.ua.deti.tqs.roadrunnerbackend.data.CustomerRepository;
import pt.ua.deti.tqs.roadrunnerbackend.data.PackageRepository;
import pt.ua.deti.tqs.roadrunnerbackend.data.PickUpLocationRepository;
import pt.ua.deti.tqs.roadrunnerbackend.data.ShopRepository;
import pt.ua.deti.tqs.roadrunnerbackend.data.StateRepository;
import pt.ua.deti.tqs.roadrunnerbackend.data.UserRepository;
import pt.ua.deti.tqs.roadrunnerbackend.model.Customer;
import pt.ua.deti.tqs.roadrunnerbackend.model.PickUpLocation;
import pt.ua.deti.tqs.roadrunnerbackend.model.Shop;
import pt.ua.deti.tqs.roadrunnerbackend.model.State;
import pt.ua.deti.tqs.roadrunnerbackend.model.User;
import pt.ua.deti.tqs.roadrunnerbackend.model.Package;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

@Component
@Slf4j
public class DataInitializer implements CommandLineRunner {
    private final UserRepository userRepository;
    private final PickUpLocationRepository pickUpLocationRepository;
    private final CustomerRepository customerRepository;
    private final ShopRepository shopRepository;
    private final PackageRepository packageRepository;

    public DataInitializer(CustomerRepository customerRepository, ShopRepository shopRepository,
                           PickUpLocationRepository pickUpLocationRepository,
                            UserRepository userRepository,
                           PackageRepository packageRepository) {
        this.customerRepository = customerRepository;
        this.shopRepository = shopRepository;
        this.pickUpLocationRepository = pickUpLocationRepository;
        this.userRepository = userRepository;
        this.packageRepository = packageRepository;
    }


    @Override
    public void run(String... args) throws Exception {
        log.info("DataInitializer started");
        if (customerRepository.count() == 0) {
            log.info("No customers found, creating some...");
            ObjectMapper mapper = new ObjectMapper();
            File file = new File("roadrunner-backend-main/src/main/resources/data/customer.json");
            List<Customer> customers = mapper.readValue(file, new TypeReference<ArrayList<Customer>>() {});
            customerRepository.saveAll(customers);
            log.info("Customers created");
        }

        if (shopRepository.count() == 0) {
            log.info("No shops found, creating some...");
            ObjectMapper mapper = new ObjectMapper();
            File file = new File("roadrunner-backend-main/src/main/resources/data/shop.json");
            List<Shop> shops = mapper.readValue(file, new TypeReference<ArrayList<Shop>>() {});

            shopRepository.saveAll(shops);
            log.info("Shops created");
        }

        if (pickUpLocationRepository.count() == 0) {
            log.info("No pickUpLocations found, creating some...");
            ObjectMapper mapper = new ObjectMapper();
            File file = new File("roadrunner-backend-main/src/main/resources/data/pickUpLocation.json");
            List<PickUpLocation> pickUpLocations = mapper.readValue(file, new TypeReference<ArrayList<PickUpLocation>>() {});

            pickUpLocationRepository.saveAll(pickUpLocations);
            log.info("PickUpLocations created");
        }

        if (userRepository.count() == 0) {
            log.info("No users found, creating some...");
            ObjectMapper mapper = new ObjectMapper();
            File file = new File("roadrunner-backend-main/src/main/resources/data/user.json");
            List<User> users = mapper.readValue(file, new TypeReference<ArrayList<User>>() {});
            
            userRepository.saveAll(users);
            log.info("Users created");
        }

        if(packageRepository.count()==0){
            log.info("No packages found, creating some...");
            ObjectMapper mapper = new ObjectMapper();
            File file = new File("roadrunner-backend-main/src/main/resources/data/packages.json");
            List<Package> packages = mapper.readValue(file, new TypeReference<ArrayList<Package>>() {});

            packageRepository.saveAll(packages);
            log.info("Packages created");
        }
        log.info("DataInitializer finished");
    }

}
