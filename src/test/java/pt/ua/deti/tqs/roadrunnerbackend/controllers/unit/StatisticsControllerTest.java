package pt.ua.deti.tqs.roadrunnerbackend.controllers.unit;

import io.restassured.module.mockmvc.RestAssuredMockMvc;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import pt.ua.deti.tqs.roadrunnerbackend.controllers.StatisticsController;
import pt.ua.deti.tqs.roadrunnerbackend.model.enums.Status;
import pt.ua.deti.tqs.roadrunnerbackend.services.StatisticsService;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.hamcrest.Matchers.equalTo;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@WebMvcTest(StatisticsController.class)
@AutoConfigureMockMvc(addFilters = false)
@ActiveProfiles("test")
public class StatisticsControllerTest {

	@Autowired
	private MockMvc mvc;

	@MockBean
	private StatisticsService statisticsService;

	@BeforeEach
	void setUp() {
		RestAssuredMockMvc.mockMvc(mvc);
	}

	@Test
	@WithMockUser
	void whenGetPackageStateStatistics_thenReturnOkAndStatistics() {
		List<Object> result = new ArrayList<>();
		for (Status s : Status.values()) {
			Map<String, Object> m = new HashMap<>();
			m.put("state", s.toString());
			m.put("packages", 2);
			result.add(m);
		}

		when(statisticsService.getStatesOfPackages()).thenReturn(result);

		RestAssuredMockMvc.given()
				.header("Authorization", "Bearer validToken")
				.contentType("application/json")
				.when()
				.get("/api/statistics/packages_state")
				.then()
				.statusCode(200)
				.body("size()", equalTo(9))
				.body("[0].state", equalTo("PENDING"))
				.body("[0].packages", equalTo(2));

		verify(statisticsService).getStatesOfPackages();
	}

	@Test
	@WithMockUser
	void whenGetPackagePickupStatistics_thenReturnOkAndStatistics() {
		List<Object> result = new ArrayList<>();
		Map<String, Object> m = new HashMap<>();
		m.put("pickup", "Aveiro");
		m.put("packages", 2);
		result.add(m);

		when(statisticsService.getPackagesByPickUpLocation()).thenReturn(result);

		RestAssuredMockMvc.given()
				.header("Authorization", "Bearer validToken")
				.contentType("application/json")
				.when()
				.get("/api/statistics/packages_by_pickup")
				.then()
				.statusCode(200)
				.body("size()", equalTo(1))
				.body("[0].pickup", equalTo("Aveiro"))
				.body("[0].packages", equalTo(2));

		verify(statisticsService).getPackagesByPickUpLocation();
	}

	@Test
	@WithMockUser
	void whenGetOtherStatistics_thenReturnOkAndStatistics() {
		Map<String, Object> result = new HashMap<>();
		result.put("nrPickUpLocations", 2);
		result.put("nrShops", 3);
		result.put("nrPackages", 10);
		result.put("nrOnGoingPackages", 8);

		when(statisticsService.getOtherStatistics()).thenReturn(result);

		RestAssuredMockMvc.given()
				.header("Authorization", "Bearer validToken")
				.contentType("application/json")
				.when()
				.get("/api/statistics/other_stats")
				.then()
				.statusCode(200)
				.body("nrPickUpLocations", equalTo(2))
				.body("nrShops", equalTo(3))
				.body("nrPackages", equalTo(10))
				.body("nrOnGoingPackages", equalTo(8));

		verify(statisticsService).getOtherStatistics();
	}

}
