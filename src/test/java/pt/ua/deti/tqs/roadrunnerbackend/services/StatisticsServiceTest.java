package pt.ua.deti.tqs.roadrunnerbackend.services;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import pt.ua.deti.tqs.roadrunnerbackend.data.PackageRepository;
import pt.ua.deti.tqs.roadrunnerbackend.data.PickUpLocationRepository;
import pt.ua.deti.tqs.roadrunnerbackend.data.ShopRepository;
import pt.ua.deti.tqs.roadrunnerbackend.model.Package;
import pt.ua.deti.tqs.roadrunnerbackend.model.PickUpLocation;
import pt.ua.deti.tqs.roadrunnerbackend.model.Shop;
import pt.ua.deti.tqs.roadrunnerbackend.model.enums.Status;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

public class StatisticsServiceTest {

    @Mock
    private PackageRepository packageRepository;

    @Mock
    private PickUpLocationRepository pickUpLocationRepository;

    @Mock
    private ShopRepository shopRepository;

    @InjectMocks
    private StatisticsService statisticsService;

    @BeforeEach
    public void setup() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void testGetOtherStatistics() {
        PickUpLocation pickUpLocation = new PickUpLocation();
        Shop shop = new Shop();
        Package pack = new Package();
        pack.setStatus(Status.PENDING);

        when(pickUpLocationRepository.findAll()).thenReturn(new ArrayList<>(List.of(pickUpLocation)));
        when(shopRepository.findALLByDisabled(false)).thenReturn(new ArrayList<>(List.of(shop)));
        when(packageRepository.findAll()).thenReturn(new ArrayList<>(List.of(pack)));
        when(packageRepository.findByStatus(Status.PENDING)).thenReturn(new ArrayList<>(List.of(pack)));
        when(packageRepository.findByStatus(Status.SHIPPING)).thenReturn(new ArrayList<>());
        when(packageRepository.findByStatus(Status.INTRANSIT)).thenReturn(new ArrayList<>());
        when(packageRepository.findByStatus(Status.AVAILABLE)).thenReturn(new ArrayList<>());

        Map<String, Object> result = (Map<String, Object>) statisticsService.getOtherStatistics();

        assertEquals(1, result.get("nrPickUpLocations"));
        assertEquals(1, result.get("nrShops"));
        assertEquals(1, result.get("nrPackages"));
        assertEquals(1, result.get("nrOnGoingPackages"));

        verify(pickUpLocationRepository, times(1)).findAll();
        verify(shopRepository, times(1)).findALLByDisabled(false);
        verify(packageRepository, times(1)).findAll();
        verify(packageRepository, times(1)).findByStatus(Status.PENDING);
        verify(packageRepository, times(1)).findByStatus(Status.SHIPPING);
        verify(packageRepository, times(1)).findByStatus(Status.INTRANSIT);
        verify(packageRepository, times(1)).findByStatus(Status.AVAILABLE);
    }

    @Test
    public void testGetPackagesByPickUpLocation() {
        PickUpLocation pickUpLocation = new PickUpLocation();
        pickUpLocation.setName("PickUpLocation");

        Package pack = new Package();
        pack.setPickUpLocation(pickUpLocation);

        when(pickUpLocationRepository.findAll()).thenReturn(List.of(pickUpLocation));
        when(packageRepository.findAllByPickUpLocationId(pickUpLocation.getId())).thenReturn(List.of(pack));

        List<Object> result = statisticsService.getPackagesByPickUpLocation();

        assertEquals(1, result.size());

        Map<String, Object> location = (Map<String, Object>) result.get(0);
        assertEquals("PickUpLocation", location.get("name"));
        assertEquals(1, location.get("packages"));

        // Verificar se os métodos dos mocks foram chamados corretamente
        verify(pickUpLocationRepository, times(1)).findAll();
        verify(packageRepository, times(1)).findAllByPickUpLocationId(pickUpLocation.getId());
    }

    @Test
    public void testGetStatesOfPackages() {
        Package pack1 = new Package();
        pack1.setStatus(Status.PENDING);
        Package pack2 = new Package();
        pack2.setStatus(Status.SHIPPING);
        Package pack3 = new Package();
        pack3.setStatus(Status.INTRANSIT);

        when(packageRepository.findByStatus(Status.PENDING)).thenReturn(List.of(pack1));
        when(packageRepository.findByStatus(Status.SHIPPING)).thenReturn(List.of(pack2));
        when(packageRepository.findByStatus(Status.INTRANSIT)).thenReturn(List.of(pack3));
        when(packageRepository.findByStatus(Status.AVAILABLE)).thenReturn(new ArrayList<>());

        List<Object> result = statisticsService.getStatesOfPackages();

        assertEquals(9, result.size());

        Map<String, Object> state1 = (Map<String, Object>) result.get(0);
        assertEquals("PENDING", state1.get("state"));
        assertEquals(1, state1.get("packages"));

        Map<String, Object> state2 = (Map<String, Object>) result.get(1);
        assertEquals("SHIPPING", state2.get("state"));
        assertEquals(1, state2.get("packages"));

        Map<String, Object> state3 = (Map<String, Object>) result.get(2);
        assertEquals("INTRANSIT", state3.get("state"));
        assertEquals(1, state3.get("packages"));

        Map<String, Object> state4 = (Map<String, Object>) result.get(3);
        assertEquals("AVAILABLE", state4.get("state"));
        assertEquals(0, state4.get("packages"));

        // Verificar se os métodos dos mocks foram chamados corretamente
        verify(packageRepository, times(1)).findByStatus(Status.PENDING);
        verify(packageRepository, times(1)).findByStatus(Status.SHIPPING);
        verify(packageRepository, times(1)).findByStatus(Status.INTRANSIT);
        verify(packageRepository, times(1)).findByStatus(Status.AVAILABLE);

    }

}
