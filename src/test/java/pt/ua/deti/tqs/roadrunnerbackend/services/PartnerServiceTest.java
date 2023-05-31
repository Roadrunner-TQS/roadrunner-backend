package pt.ua.deti.tqs.roadrunnerbackend.services;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import pt.ua.deti.tqs.roadrunnerbackend.data.PackageRepository;
import pt.ua.deti.tqs.roadrunnerbackend.data.PickUpLocationRepository;
import pt.ua.deti.tqs.roadrunnerbackend.model.Package;
import pt.ua.deti.tqs.roadrunnerbackend.model.PickUpLocation;
import pt.ua.deti.tqs.roadrunnerbackend.model.dto.ErrorDTO;
import pt.ua.deti.tqs.roadrunnerbackend.model.enums.Status;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class PartnerServiceTest {
    @Mock
    private PickUpLocationRepository pickUpLocationRepository;
    @Mock
    private PackageRepository packageRepository;

    @InjectMocks
    private PartnerService partnerService;

    private List<Package> packages;
    @BeforeEach
    void setUp() {
        Package package1 = new Package();
        package1.setId(UUID.randomUUID());
        package1.setStatus(Status.PENDING);
        Package package2 = new Package();
        package2.setId(UUID.randomUUID());
        package2.setStatus(Status.PENDING);
        packages = List.of(package1, package2);
    }
    @Test
    @DisplayName("Test getAllPackages with invalid status then return error")
    void testGetAllPackages_InvalidState_ReturnsError() {
        Object result = partnerService.getAllPackages(UUID.randomUUID(), "invalid");
        assertThat(result, instanceOf(ErrorDTO.class));
        ErrorDTO error = (ErrorDTO) result;
        assertThat(error.getMessage(), equalTo("State not valid"));
    }

    @Test
    @DisplayName("Test getAllPackages with Valid status and Invalid partnerId then return error")
    void testGetAllPackages_ValidStateInvalidpartnerId_ReturnsError() {
        UUID partnerId = UUID.randomUUID();
        when(pickUpLocationRepository.existsById(any())).thenReturn(false);

        Object result = partnerService.getAllPackages(partnerId, Status.PENDING.toString());

        assertThat(result, instanceOf(ErrorDTO.class));
        ErrorDTO error = (ErrorDTO) result;
        assertThat(error.getMessage(), equalTo("Partner not found"));
    }

    @Test
    @DisplayName("Test getAllPackages with status but Valid partnerId then return ok with all packages")
    void testGetAllPackages_WithoutStatusButValidpartnerId_ReturnsOkWithAllPackages() {
        UUID partnerId = UUID.randomUUID();
        when(pickUpLocationRepository.existsById(any())).thenReturn(true);
        when(packageRepository.findAllByPickUpLocationId(any())).thenReturn(packages);

        Object result = partnerService.getAllPackages(partnerId, null);

        assertThat(result, instanceOf(List.class));
        List<Package> result_list = (List<Package>) result;
        assertThat(result_list, hasSize(2));
    }

    @Test
    @DisplayName("Test getAllPackages without valid status and Valid partnerId then return ok with all packages")
    void testGetAllPackages_WithoutValidStatusAndValidpartnerId_ReturnsOkWithAllPackages() {
        UUID partnerId = UUID.randomUUID();
        when(pickUpLocationRepository.existsById(any())).thenReturn(true);
        when(packageRepository.findAllByPickUpLocationIdAndStatus(any(), any())).thenReturn(packages);

        Object result = partnerService.getAllPackages(partnerId, Status.PENDING.toString());

        assertThat(result, instanceOf(List.class));
        List<Package> result_list = (List<Package>) result;
        assertThat(result_list, hasSize(2));
    }

    @Test
    @DisplayName("Test getPackageById with Invalid packageId then return error")
    void testGetPackageById_InvalidpackageId_ReturnsError() {
        PickUpLocation pickUpLocation = new PickUpLocation();
        pickUpLocation.setId(UUID.randomUUID());
        Package package1 = new Package();
        package1.setId(UUID.randomUUID());
        package1.setStatus(Status.PENDING);
        package1.setPickUpLocation(pickUpLocation);

        UUID partnerId = pickUpLocation.getId();
        UUID packageId = UUID.randomUUID();
        when(packageRepository.findById(packageId)).thenReturn(Optional.empty());

        Object result = partnerService.getPackageById(partnerId, packageId);

        assertThat(result, instanceOf(ErrorDTO.class));
        ErrorDTO error = (ErrorDTO) result;
        assertThat(error.getMessage(), equalTo("Package not found"));
    }

    @Test
    @DisplayName("Test getPackageById with Invalid partnerId then return error")
    void testGetPackageById_InvalidpartnerId_ReturnsError() {
        PickUpLocation pickUpLocation = new PickUpLocation();
        pickUpLocation.setId(UUID.randomUUID());
        Package package1 = new Package();
        package1.setId(UUID.randomUUID());
        package1.setStatus(Status.PENDING);
        package1.setPickUpLocation(pickUpLocation);

        UUID partnerId = UUID.randomUUID();
        UUID packageId = package1.getId();
        when(packageRepository.findById(packageId)).thenReturn(Optional.of(package1));
        when(pickUpLocationRepository.findById(package1.getPickUpLocation().getId())).thenReturn(Optional.empty());
        Object result = partnerService.getPackageById(partnerId, packageId);

        assertThat(result, instanceOf(ErrorDTO.class));
        ErrorDTO error = (ErrorDTO) result;
        assertThat(error.getMessage(), equalTo("PickUpLocation not found"));
    }
    @Test
    @DisplayName("Test getPackageById with partnerId and packageId then return ok with package")
    void testGetPackageById_ValidpartnerIdAndValidpackageId_ReturnsOkWithPackage() {
        PickUpLocation pickUpLocation = new PickUpLocation();
        pickUpLocation.setId(UUID.randomUUID());
        Package package1 = new Package();
        package1.setId(UUID.randomUUID());
        package1.setStatus(Status.PENDING);
        package1.setPickUpLocation(pickUpLocation);

        UUID partnerId = pickUpLocation.getId();
        UUID packageId = package1.getId();
        when(packageRepository.findById(packageId)).thenReturn(Optional.of(package1));
        when(pickUpLocationRepository.findById(package1.getPickUpLocation().getId())).thenReturn(Optional.of(pickUpLocation));
        Object result = partnerService.getPackageById(partnerId, packageId);

        assertThat(result, instanceOf(Package.class));
        Package result_package = (Package) result;
        assertThat(result_package.getId(), equalTo(package1.getId()));
    }

    @Test
    @DisplayName("Test getPackageById with partnerId and pickuplocationId the packageId is not belong to pickuplocation then return error")
    void testGetPackageById_ValidpartnerIdAndValidpickuplocationId_ReturnsError() {
        PickUpLocation pickUpLocation = new PickUpLocation();
        pickUpLocation.setId(UUID.randomUUID());

        PickUpLocation pickUpLocation2 = new PickUpLocation();
        pickUpLocation2.setId(UUID.randomUUID());

        Package package1 = new Package();
        package1.setId(UUID.randomUUID());
        package1.setStatus(Status.PENDING);
        package1.setPickUpLocation(pickUpLocation);

        UUID partnerId = pickUpLocation2.getId();
        UUID packageId = package1.getId();
        when(packageRepository.findById(packageId)).thenReturn(Optional.of(package1));
        when(pickUpLocationRepository.findById(package1.getPickUpLocation().getId())).thenReturn(Optional.of(pickUpLocation));
        Object result = partnerService.getPackageById(partnerId, packageId);

        assertThat(result, instanceOf(ErrorDTO.class));
        ErrorDTO error = (ErrorDTO) result;
        assertThat(error.getMessage(), equalTo("Package not Authorized"));
    }
}