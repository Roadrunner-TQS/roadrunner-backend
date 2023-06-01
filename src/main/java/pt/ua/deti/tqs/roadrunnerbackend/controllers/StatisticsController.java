package pt.ua.deti.tqs.roadrunnerbackend.controllers;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import pt.ua.deti.tqs.roadrunnerbackend.services.StatisticsService;

@Slf4j
@RestController
@RequestMapping("/api/statistics")
@RequiredArgsConstructor
public class StatisticsController {

	private final StatisticsService statisticsService;
	
	@GetMapping("/packages_state")
	@PreAuthorize("@authService.isAdmin(#token)")
	public ResponseEntity<Object> getPackageStateStatistics(@RequestHeader("Authorization") String token) {
		return null;
	}

	@GetMapping("/packages_by_pickup")
	@PreAuthorize("@authService.isAdmin(#token)")
	public ResponseEntity<Object> getPackagesByPickupStatistics(@RequestHeader("Authorization") String token) {
		return null;
	}

	@GetMapping("/other_stats")
	@PreAuthorize("@authService.isAdmin(#token)")
	public ResponseEntity<Object> getOtherStatistics(@RequestHeader("Authorization") String token) {
		return null;
	}

}
