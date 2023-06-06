package pt.ua.deti.tqs.roadrunnerbackend.services;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import pt.ua.deti.tqs.roadrunnerbackend.data.PackageRepository;
import pt.ua.deti.tqs.roadrunnerbackend.data.PickUpLocationRepository;
import pt.ua.deti.tqs.roadrunnerbackend.data.ShopRepository;
import pt.ua.deti.tqs.roadrunnerbackend.model.PickUpLocation;
import pt.ua.deti.tqs.roadrunnerbackend.model.enums.Status;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Slf4j
@Service
@RequiredArgsConstructor
public class StatisticsService {

	private final PackageRepository packageRepository;
	private final PickUpLocationRepository pickUpLocationRepository;
	private final ShopRepository shopRepository;

	public Object getOtherStatistics() {
		Map<String, Object> result = new HashMap<>();
		result.put("nrPickUpLocations", pickUpLocationRepository.findAll().size());
		result.put("nrShops", shopRepository.findALLByDisabled(false).size());
		result.put("nrPackages", packageRepository.findAll().size());
		result.put("nrOnGoingPackages", packageRepository.findByStatus(Status.PENDING).size()
		+ packageRepository.findByStatus(Status.SHIPPING).size()
		+ packageRepository.findByStatus(Status.INTRANSIT).size()
		+ packageRepository.findByStatus(Status.AVAILABLE).size());
		return result;
	}

	public List<Object> getPackagesByPickUpLocation() {
		List<Object> result = new ArrayList<>();
		for (PickUpLocation p : pickUpLocationRepository.findAll()) {
			Map<String, Object> m = new HashMap<>();
			m.put("name", p.getName());
			m.put("packages", packageRepository.findAllByPickUpLocationId(p.getId()).size());
			result.add(m);
		}
		return result;
	}

	public List<Object> getStatesOfPackages() {
		List<Object> result = new ArrayList<>();
		for (Status s : Status.values()) {
			Map<String, Object> m = new HashMap<>();
			m.put("state", s.toString());
			m.put("packages", packageRepository.findByStatus(s).size());
			result.add(m);
		}
		return result;
	}

}
