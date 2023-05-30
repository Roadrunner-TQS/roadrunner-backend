package pt.ua.deti.tqs.roadrunnerbackend.services;

import org.hamcrest.MatcherAssert;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
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
import java.util.Optional;
import java.util.UUID;

import static org.hamcrest.Matchers.instanceOf;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class AdminServiceTest {
    private List<Package> packs;
    private List<Shop> shops;

    private List<PickUpLocation> pickUpLocations;
    private final UUID invalidId = UUID.randomUUID();
    private final UUID validId = UUID.randomUUID();
    private Shop shop2 = new Shop();

    @Mock(lenient = true)
    private PackageRepository packageRepository;
    @Mock(lenient = true)
    private PickUpLocationRepository pickUpLocationRepository;
    @Mock(lenient = true)
    private ShopRepository shopRepository;
    @Mock(lenient = true)
    private UserRepository userRepository;

    @InjectMocks
    private AdminService adminServiceMock;

    @BeforeEach
    void setUp() {
        Package pack = new Package();
        pack.setId(UUID.randomUUID());
        pack.setStatus(Status.CANCELLED);
        packs = List.of(pack);

        Shop shop = new Shop();
        shop.setId(UUID.randomUUID());
        shop.setDisabled(false);
        shop.setName("Shop1");
        shop.setAddress("Address1");
        shop.setCity("City1");
        shop.setLat(1L);
        shop.setLng(1L);
        shops = List.of(shop);

        PickUpLocation pickUpLocation = new PickUpLocation();
        pickUpLocation.setId(validId);
        pickUpLocation.setAccepted(true);

        PickUpLocation pickUpLocation2 = new PickUpLocation();
        pickUpLocation2.setId(UUID.randomUUID());

        PickUpLocation pickUpLocation3 = new PickUpLocation();
        pickUpLocation3.setId(UUID.randomUUID());

        User user = new User();
        user.setId(UUID.randomUUID());
        user.setPickUpLocation(pickUpLocation);
        user.setRole(Roles.ROLE_ADMIN);

        User user2 = new User();
        user2.setId(UUID.randomUUID());
        user2.setPickUpLocation(pickUpLocation2);
        user2.setRole(Roles.ROLE_PARTNER);

        pickUpLocations = List.of(pickUpLocation, pickUpLocation2, pickUpLocation3);

        when(packageRepository.findAll()).thenReturn(packs);
        when(packageRepository.findByStatus(Status.CANCELLED)).thenReturn(packs);

        when(packageRepository.findById(pack.getId())).thenReturn(Optional.of(pack));
        when(packageRepository.findById(invalidId)).thenReturn(Optional.empty());

        when(shopRepository.findALLByDisabled(false)).thenReturn(shops);
        when(shopRepository.save(shop)).thenReturn(shop);
        when(shopRepository.save(shop2)).thenReturn(shop2);

        when(shopRepository.findByIdAndDisabled(shops.get(0).getId(), false)).thenReturn(Optional.of(shop));
        when(shopRepository.findByIdAndDisabled(invalidId, false)).thenReturn(Optional.empty());
        when(packageRepository.findAllByShopId(shops.get(0).getId())).thenReturn(packs);

        when(pickUpLocationRepository.findByIdAndDisable(pickUpLocations.get(0).getId(), false))
                .thenReturn(Optional.ofNullable(pickUpLocations.get(0)));
        when(packageRepository.findByPickUpLocation(pickUpLocations.get(0))).thenReturn(packs);

        when(pickUpLocationRepository.findByIdAndDisable(pickUpLocations.get(1).getId(), false))
                .thenReturn(Optional.ofNullable(pickUpLocations.get(1)));
        when(packageRepository.findByPickUpLocation(pickUpLocations.get(1))).thenReturn(packs);

        when(pickUpLocationRepository.findByIdAndDisable(pickUpLocations.get(2).getId(), false))
                .thenReturn(Optional.ofNullable(pickUpLocations.get(2)));
        when(packageRepository.findByPickUpLocation(pickUpLocations.get(2))).thenReturn(packs);
        when(userRepository.findByPickUpLocation(pickUpLocations.get(0))).thenReturn(Optional.of(user));
        when(userRepository.findByPickUpLocation(pickUpLocations.get(1))).thenReturn(Optional.of(user2));
        when(userRepository.findByPickUpLocation(pickUpLocations.get(2))).thenReturn(Optional.empty());

        when(pickUpLocationRepository.findByDisable(false)).thenReturn(pickUpLocations);
        when(pickUpLocationRepository.findByCityAndDisable("city", false)).thenReturn(pickUpLocations);
        when(pickUpLocationRepository.findByCityAndAcceptedAndDisable("city", false, false))
                .thenReturn(pickUpLocations);
        when(pickUpLocationRepository.findByCityAndAcceptedAndDisable("city", true, false)).thenReturn(pickUpLocations);
        when(pickUpLocationRepository.findByAcceptedAndDisable(true, false)).thenReturn(pickUpLocations);
        when(pickUpLocationRepository.findByAcceptedAndDisable(false, false)).thenReturn(pickUpLocations);

    }

    @Test
    @DisplayName("Test getPackagesByPickUpLocation -- Invalid id")
    void testGetPackagesByPickUpLocationInvalid() {
        Object result = adminServiceMock.getPackagesByPickUpLocation(invalidId);
        MatcherAssert.assertThat(result, instanceOf(ErrorDTO.class));
        assertEquals("PickUpLocation not found", ((ErrorDTO) result).getMessage());
    }

    @Test
    @DisplayName("Test getPackagesByPickUpLocation -- Valid id")
    void testGetPackagesByPickUpLocationValid() {
        Object result = adminServiceMock.getPackagesByPickUpLocation(pickUpLocations.get(0).getId());
        MatcherAssert.assertThat(result, instanceOf(List.class));
        assertEquals(packs, result);
    }

    @Test
    @DisplayName("Test deletePickUpLocation -- Invalid id")
    void testDeletePickUpLocationInvalid() {
        Object result = adminServiceMock.deletePickUpLocation(invalidId);
        MatcherAssert.assertThat(result, instanceOf(ErrorDTO.class));
        assertEquals("PickUpLocation not found", ((ErrorDTO) result).getMessage());
    }

    @Test
    @DisplayName("Test deletePickUpLocation -- valid id but role not admin")
    void testDeletePickUpLocationValidNotAdmin() {
        Object result = adminServiceMock.deletePickUpLocation(pickUpLocations.get(0).getId());
        MatcherAssert.assertThat(result, instanceOf(ErrorDTO.class));
        assertEquals("Error to delete PickUpLocation", ((ErrorDTO) result).getMessage());
    }

    @Test
    @DisplayName("Test deletePickUpLocation -- valid id and role admin")
    void testDeletePickUpLocationValidAdmin() {
        Object result = adminServiceMock.deletePickUpLocation(pickUpLocations.get(1).getId());
        MatcherAssert.assertThat(result, instanceOf(SuccessDTO.class));
        assertEquals("PickUpLocation deleted", ((SuccessDTO<?>) result).getMessage());
    }

    @Test
    @DisplayName("Test deletePickUpLocation -- valid id and not user")
    void testDeletePickUpLocationValidNotUser() {
        Object result = adminServiceMock.deletePickUpLocation(pickUpLocations.get(2).getId());
        MatcherAssert.assertThat(result, instanceOf(ErrorDTO.class));
        assertEquals("Error to delete PickUpLocation", ((ErrorDTO) result).getMessage());
    }

    @Test
    @DisplayName("Test acceptedPickUpLocation -- Invalid id")
    void testAcceptedPickUpLocationInvalid() {
        Object result = adminServiceMock.acceptedPickUpLocation(invalidId);
        MatcherAssert.assertThat(result, instanceOf(ErrorDTO.class));
        assertEquals("PickUpLocation not found", ((ErrorDTO) result).getMessage());
    }

    @Test
    @DisplayName("Test acceptedPickUpLocation -- Valid id but the pickUpLocation is already accepted")
    void testAcceptedPickUpLocationValidAlreadyAccepted() {
        Object result = adminServiceMock.acceptedPickUpLocation(pickUpLocations.get(0).getId());
        MatcherAssert.assertThat(result, instanceOf(ErrorDTO.class));
        assertEquals("PickUpLocation already accepted", ((ErrorDTO) result).getMessage());
    }

    @Test
    @DisplayName("Test acceptedPickUpLocation -- Valid id")
    void testAcceptedPickUpLocationValid() {
        Object result = adminServiceMock.acceptedPickUpLocation(pickUpLocations.get(1).getId());
        MatcherAssert.assertThat(result, instanceOf(SuccessDTO.class));
        assertEquals("PickUpLocation accepted", ((SuccessDTO<?>) result).getMessage());
    }

    @Test
    @DisplayName("Test acceptedPickUpLocation -- Valid id but the userNotFound")
    void testAcceptedPickUpLocationValidUserNotFound() {
        Object result = adminServiceMock.acceptedPickUpLocation(pickUpLocations.get(2).getId());
        MatcherAssert.assertThat(result, instanceOf(ErrorDTO.class));
        assertEquals("User not found", ((ErrorDTO) result).getMessage());
    }

    @Test
    @DisplayName("Test getPickUpLocationById -- Invalid id")
    void testGetPickUpLocationByIdInvalid() {
        Object result = adminServiceMock.getPickUpLocationById(invalidId);
        MatcherAssert.assertThat(result, instanceOf(ErrorDTO.class));
        assertEquals("PickUpLocation not found", ((ErrorDTO) result).getMessage());
    }

    @Test
    @DisplayName("Test getPickUpLocationById -- Valid id")
    void testGetPickUpLocationByIdValid() {
        Object result = adminServiceMock.getPickUpLocationById(pickUpLocations.get(0).getId());
        MatcherAssert.assertThat(result, instanceOf(PickUpLocation.class));
        assertEquals(pickUpLocations.get(0), result);
    }

    @Test
    @DisplayName("Test getPickUpLocations -- without parameters")
    void testGetPickUpLocationsWithoutParameters() {
        Object result = adminServiceMock.getPickUpLocations(null, null);
        MatcherAssert.assertThat(result, instanceOf(List.class));
        assertEquals(pickUpLocations, result);
    }

    @Test
    @DisplayName("Test getPickUpLocations -- with city parameter and without accepted parameter")
    void testGetPickUpLocationsWithCityParameter() {
        Object result = adminServiceMock.getPickUpLocations("city", null);
        MatcherAssert.assertThat(result, instanceOf(List.class));
        assertEquals(pickUpLocations, result);
    }

    @Test
    @DisplayName("Test getPickUpLocations -- with city parameter and with accepted parameter")
    void testGetPickUpLocationsWithCityAndAcceptedParameter() {
        Object result = adminServiceMock.getPickUpLocations("city", true);
        MatcherAssert.assertThat(result, instanceOf(List.class));
        assertEquals(pickUpLocations, result);
    }

    @Test
    @DisplayName("Test getPickUpLocations -- with accepted parameter")
    void testGetPickUpLocationsWithAcceptedParameter() {
        Object result = adminServiceMock.getPickUpLocations(null, true);
        MatcherAssert.assertThat(result, instanceOf(List.class));
        assertEquals(pickUpLocations, result);

    }
}
