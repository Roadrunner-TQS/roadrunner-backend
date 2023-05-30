package pt.ua.deti.tqs.roadrunnerbackend.services;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import pt.ua.deti.tqs.roadrunnerbackend.data.*;
import pt.ua.deti.tqs.roadrunnerbackend.model.Customer;
import pt.ua.deti.tqs.roadrunnerbackend.model.Package;
import pt.ua.deti.tqs.roadrunnerbackend.model.PickUpLocation;
import pt.ua.deti.tqs.roadrunnerbackend.model.Shop;
import pt.ua.deti.tqs.roadrunnerbackend.model.dto.HistoryStateDTO;
import pt.ua.deti.tqs.roadrunnerbackend.model.dto.OrderDto;
import pt.ua.deti.tqs.roadrunnerbackend.model.enums.Status;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class ShopServiceTest {
    private List<Package> packs;
    private List<PickUpLocation> pickups;
    private HistoryStateDTO historyStateDTO;
    private Shop shop;
    private Customer customer;
    private final UUID invalidId = UUID.randomUUID();
    @Mock(lenient = true)
    private PickUpLocationRepository pickUpLocationRepository;
    @Mock(lenient = true)
    private CustomerRepository customerRepository;
    @Mock(lenient = true)
    private PackageRepository packageRepository;
    @Mock(lenient = true)
    private StateRepository stateRepository;
    @Mock(lenient = true)
    private ShopRepository shopRepository;
    @Mock(lenient = true)
    private AuthService authService;

    @InjectMocks
    private ShopService shopService;

    @BeforeEach
    void setUp(){
        Package pack1 = new Package();
        pack1.setId(UUID.randomUUID());
        pack1.setStatus(Status.CANCELLED);

        Package pack2 = new Package();
        pack2.setId(UUID.randomUUID());
        pack2.setStatus(Status.PENDING);
        packs = List.of(pack1,pack2);

        historyStateDTO = new HistoryStateDTO();
        for (Package pack : packs) {
            historyStateDTO.getStatuses().put(pack.getId(),pack.getStates());
        }
        PickUpLocation pick1 = new PickUpLocation();
        pick1.setId(UUID.randomUUID());
        pickups = List.of(pick1);

        customer = new Customer();
        customer.setId(UUID.randomUUID());
        customer.setEmail("test@ua.pt");

        shop = new Shop();
        shop.setId(UUID.randomUUID());


        when(packageRepository.findAll()).thenReturn(packs);
        when(packageRepository.findById(pack1.getId())).thenReturn(Optional.of(pack1));
        when(packageRepository.findById(pack2.getId())).thenReturn(Optional.of(pack2));
        when(packageRepository.findById(invalidId)).thenReturn(Optional.empty());

        when(pickUpLocationRepository.findByDisable(false)).thenReturn(pickups);
        when(pickUpLocationRepository.findById(pick1.getId())).thenReturn(Optional.of(pick1));
        when(pickUpLocationRepository.findById(invalidId)).thenReturn(Optional.empty());

        when(shopRepository.findById(shop.getId())).thenReturn(Optional.of(shop));
        when(shopRepository.findById(invalidId)).thenReturn(Optional.empty());

        when(customerRepository.findByEmail(customer.getEmail())).thenReturn(Optional.empty());
        when(customerRepository.findByEmail("existing")).thenReturn(Optional.of(customer));

            when(authService.isAdmin("valid_token_admin")).thenReturn(true);
            when(authService.isAdmin("invalid_token")).thenReturn(false);

            when(authService.isPartner("valid_token_partner")).thenReturn(true);
            when(authService.isPartner("invalid_token")).thenReturn(false);

    }

    @Test
    @DisplayName("Test getHistory - return HistoryStateDTO")
    void getHistory_returnHistoryStateDTO(){
        HistoryStateDTO result = shopService.getHistory();
        assertEquals(result, historyStateDTO);
    }

    @Test
    @DisplayName("Test getAllPickUpLocations - return list PickUps")
    void getHistory_returnListPickUps(){
        List<PickUpLocation> result = shopService.getAllPickUpLocations();
        assertEquals(result, pickups);
    }

    @Test
    @DisplayName("Test createPackage - return PickUpLocation not found")
    void createPackage_ReturnPickUpLocationNotFound(){
        OrderDto orderDto = new OrderDto();
        orderDto.setPickUpLocationId(invalidId);
        UUID result = shopService.createPackage(orderDto);
        assertNull(result);
    }

    @Test
    @DisplayName("Test createPackage - return Shop not found")
    void createPackage_ReturnShopNotFound(){
        OrderDto orderDto = new OrderDto();
        orderDto.setPickUpLocationId(pickups.get(0).getId());
        orderDto.setShopId(invalidId);
        UUID result = shopService.createPackage(orderDto);
        assertNull(result);
    }

    @Test
    @DisplayName("Test createPackage - return Customer not found, Creating new customer and creating pack")
    void createPackage_ReturnCustomerNotFoundAndCreatingNewOneAndCreatingPack(){
        OrderDto orderDto = new OrderDto();
        orderDto.setPickUpLocationId(pickups.get(0).getId());
        orderDto.setShopId(shop.getId());
        orderDto.setEmail("existing");
        UUID result = shopService.createPackage(orderDto);
        assertNull(result);
    }

    @Test
    @DisplayName("Test createPackage - return Customer found and Creating pack")
    void createPackage_ReturnCustomerFoundAndCreatingPack(){
        OrderDto orderDto = new OrderDto();
        orderDto.setPickUpLocationId(pickups.get(0).getId());
        orderDto.setShopId(shop.getId());
        orderDto.setEmail(customer.getEmail());
        UUID result = shopService.createPackage(orderDto);
        assertNull(result);
    }

    @Test
    @DisplayName("Test updatePackage - Package not found")
    void updatePackage_PackageNotFound(){
        assertFalse(shopService.updatePackage(invalidId, "state", "token"));
    }

    @Test
    @DisplayName("Test updatePackage - Package can't be updated")
    void updatePackage_PackageCantBeUpdated(){
        assertFalse(shopService.updatePackage(packs.get(0).getId(), Status.RETURNED.name(), "token"));
    }

    @Test
    @DisplayName("Test updatePackage - Token not valid")
    void updatePackage_TokenNotValid(){
        assertFalse(shopService.updatePackage(packs.get(1).getId(), Status.DELIVERED.name(), "invalid_token"));
    }

    @Test
    @DisplayName("Test updatePackage - Toke is admin")
    void updatePackage_TokenIsAdmin(){
        assertTrue(shopService.updatePackage(packs.get(1).getId(), Status.SHIPPING.name(), "valid_token_admin"));
    }

    @Test
    @DisplayName("Test updatePackage - Toke is partner")
    void updatePackage_TokenIsPartner(){
        assertTrue(shopService.updatePackage(packs.get(1).getId(), Status.AVAILABLE.name(), "valid_token_partner"));
    }

    @Test
    @DisplayName("Test updatePackage - Toke is shop")
    void updatePackage_TokenIsCustomer(){
        assertTrue(shopService.updatePackage(packs.get(1).getId(),  Status.CANCELLED.name(), "==shop=="));
    }



}