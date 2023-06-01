package pt.ua.deti.tqs.roadrunnerbackend.services;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import pt.ua.deti.tqs.roadrunnerbackend.data.PackageRepository;
import pt.ua.deti.tqs.roadrunnerbackend.data.PickUpLocationRepository;
import pt.ua.deti.tqs.roadrunnerbackend.data.ShopRepository;

import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class StatisticsService {

	private final PackageRepository packageRepository;
	private final PickUpLocationRepository pickUpLocationRepository;
	private final ShopRepository shopRepository;

	public Object getOtherStatistics() {
		return null;
	}

	public List<Object> getPackagesByPickUpLocation() {
		return null;
	}

	public List<Object> getStatesOfPackages() {
		return null;
	}

}
