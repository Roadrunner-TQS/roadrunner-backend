package pt.ua.deti.tqs.roadrunnerbackend.config;

import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;
import pt.ua.deti.tqs.roadrunnerbackend.data.*;
import pt.ua.deti.tqs.roadrunnerbackend.model.Package;
import pt.ua.deti.tqs.roadrunnerbackend.model.*;
import pt.ua.deti.tqs.roadrunnerbackend.model.enums.Roles;
import pt.ua.deti.tqs.roadrunnerbackend.model.enums.Status;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

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
            List<Customer> customers = new ArrayList<>();
            customers.add(new Customer(UUID.fromString("6c84fb90-12c4-11e1-840d-7b25c5ee775a"), "John", "Doe", "johndoe@example.com", 967892345));
            customers.add(new Customer(UUID.fromString("6c84fb90-12c4-11e1-840d-7b25c5ee775b"), "Jane", "Smith", "janesmith@example.com",987654321));
            customers.add(new  Customer(UUID.fromString("6c84fb90-12c4-11e1-840d-7b25c5ee775c"), "Alice", "Johnson", "alicejohnson@example.com", 912345678));
            customerRepository.saveAll(customers);
            log.info("Customers created");
        }

        if (shopRepository.count() == 0) {
            log.info("No shops found, creating some...");
            List<Shop> shops = new ArrayList<>();
            shops.add(new Shop(UUID.fromString("6c84fb90-12c4-11e1-840d-7b25c5ee775a"), "Papelaria Alegria", "papelaria-alegria", "Rua das Flores, 123", "Aveiro", (long) -23.5505199, (long) -46.6333094, false));
            shops.add(new Shop(UUID.fromString("6c84fb90-12c4-11e1-840d-7b25c5ee775b"), "Bazar dos Sonhos", "bazar-dos-sonhos", "Avenida Central, 456", "São João da Madeira", (long) -23.5505199, (long) -46.6333094, false));
            shops.add(new Shop(UUID.fromString("6c84fb90-12c4-11e1-840d-7b25c5ee775c"), "Loja Criativa", "loja-criativa", "Rua do Comércio, 789", "São Paulo", (long) -23.5505199, (long) -46.6333094, false));
            shops.add(new Shop(UUID.fromString("6c84fb90-12c4-11e1-840d-7b25c5ee775d"), "Papelaria Encantada", "papelaria-encantada", "Avenida dos Artistas, 101", "São José dos Campos", (long) -23.5505199, (long) -46.6333094, false));
            shops.add(new Shop(UUID.fromString("6c84fb90-12c4-11e1-840d-7b25c5ee775e"), "BookIt", "bookit", "Av. Lourenço Pexinho, 1000", "Aveiro", (long) -23.5505199, (long) -46.6333094, false));
            shopRepository.saveAll(shops);
            log.info("Shops created");
        }

        if (pickUpLocationRepository.count() == 0) {
            log.info("No pickUpLocations found, creating some...");
            List<PickUpLocation> pickUpLocations = new ArrayList<>();
            pickUpLocations.add(new PickUpLocation(UUID.fromString("6c84fb90-12c4-11e1-840d-7b25c5ee775a"), "Papelaria Criativa", "papelaria-criativa", "Rua das Artes, 123", "Lisboa", (long) 38.722252, (long) -9.139337, true, false));
            pickUpLocations.add(new PickUpLocation(UUID.fromString("6c84fb90-12c4-11e1-840d-7b25c5ee775b"), "Bomba de Gasolina Central", "bomba-de-gasolina-central", "Avenida Principal, 456", "Porto", (long) 41.14961, (long) -8.61099, true, false));
            pickUpLocations.add(new PickUpLocation(UUID.fromString("6c84fb90-12c4-11e1-840d-7b25c5ee775c"), "Loja de Suprimentos", "loja-de-suprimentos", "Rua dos Materiais, 789", "Lisboa", (long) 38.731128, (long) -9.13751, true, false));
            pickUpLocations.add(new PickUpLocation(UUID.fromString("6c84fb90-12c4-11e1-840d-7b25c5ee775d"), "Posto Express", "posto-express", "Estrada Central, 101", "Porto", (long) 41.15109, (long) -8.61208, false, false));
            pickUpLocations.add(new PickUpLocation(UUID.fromString("6c84fb90-12c4-11e1-840d-7b25c5ee775e"), "PickUp Point Rápido", "pick-up-point-rapido", "Avenida dos Serviços, 222", "Lisboa", (long) 38.724526, (long) -9.141745, false, false));
            pickUpLocations.add(new PickUpLocation(UUID.fromString("6c84fb90-12c4-11e1-840d-7b25c5ee775f"), "Loja de Artesanato", "loja-de-artesanato", "Rua das Belas Artes, 333", "Aveiro", (long) 40.640063, (long) -8.653117, true, false));
            pickUpLocations.add(new PickUpLocation(UUID.fromString("6c84fb90-12c4-11e1-840d-7b25c5ee7760"), "Posto de Recolha Fácil", "posto-de-recolha-facil", "Avenida dos Serviços, 444", "Porto", (long) 41.156149, (long) -8.629574, true, false));
            pickUpLocations.add(new PickUpLocation(UUID.fromString("6c84fb90-12c4-11e1-840d-7b25c5ee7761"), "Loja de Arte Moderna", "loja-de-arte-moderna", "Rua das Cores, 666", "Aveiro", (long) 40.637177, (long) -8.651212, true, false));
            pickUpLocations.add(new PickUpLocation(UUID.fromString("6c84fb90-12c4-11e1-840d-7b25c5ee7762"), "Bomba de Gasolina Norte", "bomba-de-gasolina-norte", "Avenida Central, 777", "Lisboa", (long) 38.71952, (long) -9.135587, false, false));
            pickUpLocationRepository.saveAll(pickUpLocations);

            log.info("PickUpLocations created");
        }

        if (userRepository.count() == 0) {
            log.info("No users found, creating some...");

            List<User> users = new ArrayList<>();
            User user1 = new User(UUID.fromString("6c84fb90-12c4-11e1-840d-7b25c5aa775a"), "admin123",
                    "admin1@example.com", "Maria", "Silva", Roles.ROLE_ADMIN, null);
            user1.setPassword(user1.getPassword());
            users.add(user1);
            User user2 = new User(UUID.fromString("6c84fb90-12c4-11e1-840d-7b25c5aa775b"), "user123", "user1@example.com",
                    "João", "Santos", Roles.ROLE_PARTNER, pickUpLocationRepository
                    .findById(UUID.fromString("6c84fb90-12c4-11e1-840d-7b25c5ee775a")).orElse(null));
            user2.setPassword(user2.getPassword());
            users.add(user2);
            User user3 = new User(UUID.fromString("6c84fb90-12c4-11e1-840d-7b25c5aa775c"), "user123", "user2@example.com",
                    "Diogo", "Martins", Roles.ROLE_PARTNER, pickUpLocationRepository.
                    findById(UUID.fromString("6c84fb90-12c4-11e1-840d-7b25c5ee775b")).orElse(null));
            user3.setPassword(user3.getPassword());
            users.add(user3);
            User user4 = new User(UUID.fromString("6c84fb90-12c4-11e1-840d-7b25c5aa775d"), "user123", "user3@example.com",
                    "Rui", "Gonçalves", Roles.ROLE_PARTNER, pickUpLocationRepository
                            .findById(UUID.fromString("6c84fb90-12c4-11e1-840d-7b25c5ee775c")).orElse(null));
            user4.setPassword(user4.getPassword());
            users.add(user4);
            User user5 = new User(UUID.fromString("6c84fb90-12c4-11e1-840d-7b25c5aa775e"), "user123", " user4@example.com",
                    "André", "Ramos", Roles.ROLE_NOT_VERIFIED, pickUpLocationRepository
                            .findById(UUID.fromString("6c84fb90-12c4-11e1-840d-7b25c5ee775d")).orElse(null));
            user5.setPassword(user5.getPassword());
            users.add(user5);
            User user6 = new User(UUID.fromString("6c84fb90-12c4-11e1-840d-7b25c5aa775f"), "user123","user5@example.com",
                    "Tomás", "Pereira", Roles.ROLE_NOT_VERIFIED, pickUpLocationRepository
                            .findById(UUID.fromString("6c84fb90-12c4-11e1-840d-7b25c5ee775e")).orElse(null));
            user6.setPassword(user6.getPassword());
            users.add(user6);
            User user7 = new User(UUID.fromString("6c84fb90-12c4-11e1-840d-7b25c5aa7760"), "user123", "user6@example.com",
                    "Gonçalo", "Fernandes", Roles.ROLE_PARTNER, pickUpLocationRepository
                            .findById(UUID.fromString("6c84fb90-12c4-11e1-840d-7b25c5ee775f")).orElse(null));
            user7.setPassword(user7.getPassword());
            users.add(user7);
            User user8 = new User(UUID.fromString("6c84fb90-12c4-11e1-840d-7b25c5aa7761"), "user123","user7@example.com",
                    "Tiago", "Costa", Roles.ROLE_PARTNER, pickUpLocationRepository
                            .findById(UUID.fromString("6c84fb90-12c4-11e1-840d-7b25c5ee7760")).orElse(null));
            user8.setPassword(user8.getPassword());
            users.add(user8);
            User user9 = new User(UUID.fromString("6c84fb90-12c4-11e1-840d-7b25c5aa7762"), "user123", "user8@example.com",
                    "Rafael", "Silva", Roles.ROLE_PARTNER, pickUpLocationRepository
                            .findById(UUID.fromString("6c84fb90-12c4-11e1-840d-7b25c5ee7761")).orElse(null));
            user9.setPassword(user9.getPassword());
            users.add(user9);
            User user10 = new User(UUID.fromString("6c84fb90-12c4-11e1-840d-7b25c5aa7763"), "user123", "user9@example.com",
                    "Francisco", "Sousa", Roles.ROLE_NOT_VERIFIED, pickUpLocationRepository
                            .findById(UUID.fromString("6c84fb90-12c4-11e1-840d-7b25c5ee7762")).orElse(null));
            user10.setPassword(user10.getPassword());
            users.add(user10);
            userRepository.saveAll(users);
            log.info("Users created");
        }
        
        if(packageRepository.count()==0){
            log.info("No packages found, creating some...");

            Package pack1 = new Package(UUID.fromString("50bca846-9500-42e6-80d0-153019c3dc68"), new ArrayList<>(),
                    customerRepository.findById(UUID.fromString("6c84fb90-12c4-11e1-840d-7b25c5ee775a")).orElse(null),
                    pickUpLocationRepository.findById(UUID.fromString("6c84fb90-12c4-11e1-840d-7b25c5ee775a")).orElse(null),
                    shopRepository.findById(UUID.fromString("6c84fb90-12c4-11e1-840d-7b25c5ee775a")).orElse(null),
                    System.currentTimeMillis(), Status.PENDING);
            pack1.getStates().add(new State(UUID.randomUUID(), pack1.getTimestamp(), Status.PENDING));

            Package pack2 = new Package(UUID.fromString("f5492e60-3f33-4af9-8b42-29370d2e70c9"), new ArrayList<>(),
                    customerRepository.findById(UUID.fromString("6c84fb90-12c4-11e1-840d-7b25c5ee775b")).orElse(null),
                    pickUpLocationRepository.findById(UUID.fromString("6c84fb90-12c4-11e1-840d-7b25c5ee775b")).orElse(null),
                    shopRepository.findById(UUID.fromString("6c84fb90-12c4-11e1-840d-7b25c5ee775b")).orElse(null),
                    System.currentTimeMillis(), Status.INTRANSIT);
            pack2.getStates().add(new State(UUID.randomUUID(), System.currentTimeMillis(), Status.PENDING));
            pack2.getStates().add(new State(UUID.randomUUID(), System.currentTimeMillis(), Status.SHIPPING));
            pack2.getStates().add(new State(UUID.randomUUID(), System.currentTimeMillis(), Status.INTRANSIT));

            Package pack3 = new Package(UUID.fromString("42d183d5-3f8e-4877-8a69-0195e51d89a7"), new ArrayList<>(),
                                customerRepository.findById(UUID.fromString("6c84fb90-12c4-11e1-840d-7b25c5ee775c")).orElse(null),
                                pickUpLocationRepository.findById(UUID.fromString("6c84fb90-12c4-11e1-840d-7b25c5ee775a")).orElse(null),
                                shopRepository.findById(UUID.fromString("6c84fb90-12c4-11e1-840d-7b25c5ee775c")).orElse(null),
                                System.currentTimeMillis(), Status.AVAILABLE);

            pack3.getStates().add(new State(UUID.randomUUID(), System.currentTimeMillis(), Status.PENDING));
            pack3.getStates().add(new State(UUID.randomUUID(), System.currentTimeMillis(), Status.SHIPPING));
            pack3.getStates().add(new State(UUID.randomUUID(), System.currentTimeMillis(), Status.INTRANSIT));
            pack3.getStates().add(new State(UUID.randomUUID(), System.currentTimeMillis(), Status.AVAILABLE));

            Package pack4 = new Package(UUID.fromString("24052b45-4a6f-4377-854f-c61e548e2a48"), new ArrayList<>(),
                    customerRepository.findById(UUID.fromString("6c84fb90-12c4-11e1-840d-7b25c5ee775a")).orElse(null),
                    pickUpLocationRepository.findById(UUID.fromString("6c84fb90-12c4-11e1-840d-7b25c5ee775d")).orElse(null),
                    shopRepository.findById(UUID.fromString("6c84fb90-12c4-11e1-840d-7b25c5ee775c")).orElse(null),
                    System.currentTimeMillis(), Status.SHIPPING);
            pack4.getStates().add(new State(UUID.randomUUID(), System.currentTimeMillis(), Status.PENDING));
            pack4.getStates().add(new State(UUID.randomUUID(), System.currentTimeMillis(), Status.SHIPPING));

            Package pack5 = new Package(UUID.fromString("59457f4a-30e5-4ae9-b4e5-c87a7bfcde86"), new ArrayList<>(),
                    customerRepository.findById(UUID.fromString("6c84fb90-12c4-11e1-840d-7b25c5ee775b")).orElse(null),
                    pickUpLocationRepository.findById(UUID.fromString("6c84fb90-12c4-11e1-840d-7b25c5ee775a")).orElse(null),
                    shopRepository.findById(UUID.fromString("6c84fb90-12c4-11e1-840d-7b25c5ee775a")).orElse(null),
                    System.currentTimeMillis(), Status.DELIVERED);

            pack5.getStates().add(new State(UUID.randomUUID(), System.currentTimeMillis(), Status.PENDING));
            pack5.getStates().add(new State(UUID.randomUUID(), System.currentTimeMillis(), Status.SHIPPING));
            pack5.getStates().add(new State(UUID.randomUUID(), System.currentTimeMillis(), Status.INTRANSIT));
            pack5.getStates().add(new State(UUID.randomUUID(), System.currentTimeMillis(), Status.AVAILABLE));
            pack5.getStates().add(new State(UUID.randomUUID(), System.currentTimeMillis(), Status.DELIVERED));

            Package pack6 = new Package(UUID.fromString("a6e03c59-71db-4a63-9e4f-7fd0526e3fb0"), new ArrayList<>(),
                    customerRepository.findById(UUID.fromString("6c84fb90-12c4-11e1-840d-7b25c5ee775a")).orElse(null),
                    pickUpLocationRepository.findById(UUID.fromString("6c84fb90-12c4-11e1-840d-7b25c5ee775b")).orElse(null),
                    shopRepository.findById(UUID.fromString("6c84fb90-12c4-11e1-840d-7b25c5ee775a")).orElse(null),
                    System.currentTimeMillis(), Status.DENIED);
            pack6.getStates().add(new State(UUID.randomUUID(), System.currentTimeMillis(), Status.PENDING));
            pack6.getStates().add(new State(UUID.randomUUID(), System.currentTimeMillis(), Status.DENIED));

            Package pack7 = new Package(UUID.fromString("a6e03c59-71db-4a63-9e4f-7fd0526e3fb1"), new ArrayList<>(),
                    customerRepository.findById(UUID.fromString("6c84fb90-12c4-11e1-840d-7b25c5ee775b")).orElse(null),
                    pickUpLocationRepository.findById(UUID.fromString("6c84fb90-12c4-11e1-840d-7b25c5ee775e")).orElse(null),
                    shopRepository.findById(UUID.fromString("6c84fb90-12c4-11e1-840d-7b25c5ee775b")).orElse(null),
                    System.currentTimeMillis(), Status.RETURNED);
            pack7.getStates().add(new State(UUID.randomUUID(), System.currentTimeMillis() , Status.PENDING));
            pack7.getStates().add(new State(UUID.randomUUID(), System.currentTimeMillis(), Status.SHIPPING));
            pack7.getStates().add(new State(UUID.randomUUID(), System.currentTimeMillis(), Status.INTRANSIT));
            pack7.getStates().add(new State(UUID.randomUUID(), System.currentTimeMillis(), Status.AVAILABLE));
            pack7.getStates().add(new State(UUID.randomUUID(), System.currentTimeMillis(), Status.DELIVERED));
            pack7.getStates().add(new State(UUID.randomUUID(), System.currentTimeMillis(), Status.RETURNED));

            Package pack8 = new Package(UUID.fromString("a6e03c59-71db-4a63-9e4f-7fd0526e3fb1"), new ArrayList<>(),
                    customerRepository.findById(UUID.fromString("6c84fb90-12c4-11e1-840d-7b25c5ee775b")).orElse(null),
                    pickUpLocationRepository.findById(UUID.fromString("6c84fb90-12c4-11e1-840d-7b25c5ee775f")).orElse(null),
                    shopRepository.findById(UUID.fromString("6c84fb90-12c4-11e1-840d-7b25c5ee775a")).orElse(null),
                    System.currentTimeMillis(), Status.CANCELLED);
            pack8.getStates().add(new State(UUID.randomUUID(), System.currentTimeMillis() , Status.PENDING));
            pack8.getStates().add(new State(UUID.randomUUID(), System.currentTimeMillis(), Status.SHIPPING));
            pack8.getStates().add(new State(UUID.randomUUID(), System.currentTimeMillis(), Status.CANCELLED));

            List<Package> packages = List.of(pack1, pack2, pack3, pack4, pack5, pack6, pack7, pack8);
            packageRepository.saveAll(packages);
            log.info("Packages created");
        }
        log.info("DataInitializer finished");
    }

}