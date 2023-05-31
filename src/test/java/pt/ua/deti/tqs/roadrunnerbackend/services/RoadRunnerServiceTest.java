package pt.ua.deti.tqs.roadrunnerbackend.services;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import pt.ua.deti.tqs.roadrunnerbackend.model.Package;
import pt.ua.deti.tqs.roadrunnerbackend.model.PickUpLocation;
import pt.ua.deti.tqs.roadrunnerbackend.model.dto.ErrorDTO;
import pt.ua.deti.tqs.roadrunnerbackend.model.dto.UserDto;

import java.util.List;
import java.util.UUID;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.instanceOf;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class RoadRunnerServiceTest {
    private static final String token_valid_admin = "token_valid_admin";
    private static final String token_valid_partner = "token_valid_partner";
    private static final String token_invalid = "token_invalid";
    private final UUID pickUpLocationId = UUID.randomUUID();
    private final UUID packageId = UUID.randomUUID();
    private PickUpLocation pickUpLocation;
    private Package packageObj;


    @Mock(lenient = true)
    private AdminService adminService;

    @Mock(lenient = true)
    private AuthService authService;

    @Mock(lenient = true)
    private PartnerService partnerService;

    @InjectMocks
    private RoadRunnerService roadRunnerService;

    @BeforeEach
    void setUp() {
        when(authService.isAdmin(token_invalid)).thenReturn(false);
        when(authService.isPartner(token_invalid)).thenReturn(false);
        when(authService.getCurrentUser(token_invalid)).thenReturn(null);

        when(authService.isAdmin(token_valid_admin)).thenReturn(true);
        when(authService.isAdmin(token_valid_partner)).thenReturn(false);

        when(authService.isPartner(token_valid_admin)).thenReturn(false);
        when(authService.isPartner(token_valid_partner)).thenReturn(true);

        pickUpLocation = new PickUpLocation();
        pickUpLocation.setId(UUID.randomUUID());
        packageObj = new Package();
        packageObj.setId(UUID.randomUUID());
        UserDto userDto = new UserDto();
        userDto.setPickUpLocation(pickUpLocation);
        when(authService.getCurrentUser(token_valid_partner)).thenReturn(userDto);

        when(adminService.getPickUpLocationById(pickUpLocation.getId())).thenReturn(pickUpLocation);
        when(adminService.getPickUpLocationById(pickUpLocationId)).thenReturn(new ErrorDTO("Pick Up Location not found"));
        when(adminService.getPackageById(packageId)).thenReturn(new ErrorDTO("Package not found"));
        when(adminService.getPackageById(packageObj.getId())).thenReturn(packageObj);

        when(partnerService.getPackageById(pickUpLocation.getId(),packageId)).thenReturn(new ErrorDTO("Package not found"));
        when(partnerService.getPackageById(pickUpLocation.getId(),packageObj.getId())).thenReturn(packageObj);

        when(adminService.getPackages("status")).thenReturn(List.of(packageObj));
        when(adminService.getPackages("error")).thenReturn(new ErrorDTO("Packages not found"));

        when(partnerService.getAllPackages(pickUpLocation.getId(),"status")).thenReturn(List.of(packageObj));
        when(partnerService.getAllPackages(pickUpLocation.getId(),"error")).thenReturn(new ErrorDTO("Packages not found"));

    }

    @Test
    @DisplayName("Get pick up location by id - Unauthorized")
    void getPickUpLocationById_unauthorized() {
        Object response = roadRunnerService.getPickUpLocationById(UUID.randomUUID(), token_invalid);
        assertThat(response, instanceOf(ErrorDTO.class));
        assertEquals("Unauthorized", ((ErrorDTO) response).getMessage());
    }

    @Test
    @DisplayName("Get pick up location by id - Admin and pick up location not found")
    void getPickUpLocationById_admin_pick_up_location_not_found() {
        Object response = roadRunnerService.getPickUpLocationById(pickUpLocationId, token_valid_admin);
        assertThat(response, instanceOf(ErrorDTO.class));
        assertEquals("Pick Up Location not found", ((ErrorDTO) response).getMessage());
    }

    @Test
    @DisplayName("Get pick up location by id - Admin and pick up location found")
    void getPickUpLocationById_admin_pick_up_location_found() {
        Object response = roadRunnerService.getPickUpLocationById(pickUpLocation.getId(), token_valid_admin);
        assertThat(response, instanceOf(PickUpLocation.class));
        assertEquals(pickUpLocation.getId(), ((PickUpLocation) response).getId());
    }

    @Test
    @DisplayName("Get pick up location by id - Partner and pick up location not found")
    void getPickUpLocationById_partner_pick_up_location_not_found() {
        Object response = roadRunnerService.getPickUpLocationById(pickUpLocationId, token_valid_partner);
        assertThat(response, instanceOf(ErrorDTO.class));
        assertEquals("Unauthorized", ((ErrorDTO) response).getMessage());
    }

    @Test
    @DisplayName("Get pick up location by id - Partner and pick up location found")
    void getPickUpLocationById_partner_pick_up_location_found() {
        Object response = roadRunnerService.getPickUpLocationById(pickUpLocation.getId(), token_valid_partner);
        assertThat(response, instanceOf(PickUpLocation.class));
        assertEquals(pickUpLocation.getId(), ((PickUpLocation) response).getId());
    }

    @Test
    @DisplayName("Get package by id - Unauthorized")
    void getPackageById_unauthorized() {
        Object response = roadRunnerService.getPackageById(UUID.randomUUID(), token_invalid);
        assertThat(response, instanceOf(ErrorDTO.class));
        assertEquals("Unauthorized", ((ErrorDTO) response).getMessage());
    }

    @Test
    @DisplayName("Get package by id - Admin and package not found")
    void getPackageById_admin_pick_up_location_not_found() {
        Object response = roadRunnerService.getPackageById(packageId, token_valid_admin);
        assertThat(response, instanceOf(ErrorDTO.class));
        assertEquals("Package not found", ((ErrorDTO) response).getMessage());
    }

    @Test
    @DisplayName("Get package by id - Admin and package found")
    void getPackageById_admin_pick_up_location_found() {
        Object response = roadRunnerService.getPackageById(packageObj.getId(), token_valid_admin);
        assertThat(response, instanceOf(Package.class));
        assertEquals(packageObj.getId(), ((Package) response).getId());
    }

    @Test
    @DisplayName("Get package by id - Partner and package not found")
    void getPackageById_partner_pick_up_location_not_found() {
        Object response = roadRunnerService.getPackageById(packageId, token_valid_partner);
        assertThat(response, instanceOf(ErrorDTO.class));
        assertEquals("Package not found", ((ErrorDTO) response).getMessage());
    }

    @Test
    @DisplayName("Get package by id - Partner and package found")
    void getPackageById_partner_pick_up_location_found() {
        Object response = roadRunnerService.getPackageById(packageObj.getId(), token_valid_partner);
        assertThat(response, instanceOf(Package.class));
        assertEquals(packageObj.getId(), ((Package) response).getId());
    }

    @Test
    @DisplayName("Get All Packages - Unauthorized")
    void getAllPackages_unauthorized() {
        Object response = roadRunnerService.getAllPackages("status", token_invalid);
        assertThat(response, instanceOf(ErrorDTO.class));
        assertEquals("Unauthorized", ((ErrorDTO) response).getMessage());
    }

    @Test
    @DisplayName("Get All Packages - Admin and packages found")
    void getAllPackages_admin() {
        Object response = roadRunnerService.getAllPackages( "status", token_valid_admin);
        assertThat(response, instanceOf(List.class));
        assertEquals(1, ((List) response).size());
    }

    @Test
    @DisplayName("Get All Packages - Admin and packages not found")
    void getAllPackages_partner() {
        Object response = roadRunnerService.getAllPackages("error", token_valid_admin);
        assertThat(response, instanceOf(ErrorDTO.class));
        assertEquals("Packages not found", ((ErrorDTO) response).getMessage());
    }


    @Test
    @DisplayName("Get All Packages - Partner and packages found")
    void getAllPackages_partner_found() {
        Object response = roadRunnerService.getAllPackages("status", token_valid_partner);
        assertThat(response, instanceOf(List.class));
        assertEquals(1, ((List) response).size());
    }

    @Test
    @DisplayName("Get All Packages - Partner and packages not found")
    void getAllPackages_partner_not_found() {
        Object response = roadRunnerService.getAllPackages("error", token_valid_partner);
        assertThat(response, instanceOf(ErrorDTO.class));
        assertEquals("Packages not found", ((ErrorDTO) response).getMessage());
    }










}