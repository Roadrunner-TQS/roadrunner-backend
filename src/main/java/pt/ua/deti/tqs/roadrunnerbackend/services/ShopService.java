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
        log.info("BookStoreService -- createPackage -- request received");
        PickUpLocation pickUpLocation = pickUpLocationRepository.findById(orderDto.getPickUpLocationId()).orElse(null);
        if (pickUpLocation == null) {
            log.error("BookStoreService -- createPackage -- PickUpLocation not found");
            return null;
        }
        Shop shop = shopRepository.findById(orderDto.getShopId()).orElse(null);
        if (shop == null){
            log.error("BookStoreService -- createPackage -- Shop not found");
            return null;
        }
        Customer customer = customerRepository.findByEmail(orderDto.getEmail()).orElse(null);
        if (customer == null){
            log.error("BookStoreService -- createPackage -- Customer not found and Creating new one");
            customer = new Customer(UUID.randomUUID(), orderDto.getEmail(), orderDto.getFirstName(), orderDto.getLastName(),
                    orderDto.getPhone());
            customerRepository.save(customer);
        }
        log.info("BookStoreService -- createPackage -- Customer found or saved");
        Package pack = new Package();
        pack.setCustomer(customer);
        pack.setPickUpLocation(pickUpLocation);
        pack.setShop(shop);
        pack.setStatus(Status.PENDING);
        packageRepository.save(pack);

        State state = new State();
        state.setStatus(Status.PENDING);
        state.setTimestamp(orderDto.getDate());
        stateRepository.save(state);

        pack.getStates().add(state);
        packageRepository.save(pack);
        log.info("BookStoreService -- createPackage -- Package created");
        return pack.getId();
    }
    public boolean updatePackage(UUID packId, String newState, String token ) {
        log.info("BookStoreService -- updatePackage -- request received");
        Package pack = packageRepository.findById(packId).orElse(null);
        if (pack == null) {
            log.error("BookStoreService -- updatePackage -- Package not found");
            return false;
        }
        if(!pack.canBeUpdated()){
            log.error("BookStoreService -- updatePackage -- Package can't be updated");
            return false;
        }

        if (Objects.equals(token, "==shop==") && newState.equals(Status.CANCELLED.name())){
            log.info("BookStoreService -- updatePackage -- Shop");
        } else if (Boolean.TRUE.equals(authService.isAdmin(token)) && (newState.equals(Status.DENIED.name())|| newState.equals(Status.SHIPPING.name()))) {
            log.info("BookStoreService -- updatePackage -- Admin");
        } else if (Boolean.TRUE.equals(authService.isPartner(token)) && !newState.equals(Status.INTRANSIT.name())
                && !newState.equals(Status.CANCELLED.name())) {
            log.info("BookStoreService -- updatePackage -- Partner");
        } else{
            log.error("BookStoreService -- updatePackage -- Token not valid");
            return false;
        }
        State state = new State();
        state.setStatus(Status.valueOf(newState));
        state.setTimestamp(System.currentTimeMillis());
        stateRepository.save(state);
        pack.getStates().add(state);
        pack.setStatus(Status.valueOf(newState));
        pack.sort();
        packageRepository.save(pack);
        log.info("BookStoreService -- updatePackage -- Success");
        return true;
    }

    public List<PickUpLocation> getAllPickUpLocations() {
        log.info("BookStoreService -- getAllPickUpLocations -- request");
        return pickUpLocationRepository.findByDisable(false);
    }


    public HistoryStateDTO getHistory() {
        log.info("BookStoreService -- getHistory -- request");
        List<Package> packages = packageRepository.findAll();
        HistoryStateDTO historyStateDTO = new HistoryStateDTO();
        for (Package pack : packages) {
            historyStateDTO.getStatuses().put(pack.getId(),pack.getStates());
        }
        log.info("BookStoreService -- getHistory -- response");
        return historyStateDTO;
    }

}
