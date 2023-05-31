package pt.ua.deti.tqs.roadrunnerbackend.controllers.it;

import io.cucumber.java.AfterAll;
import org.junit.After;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.test.context.TestPropertySource;
import pt.ua.deti.tqs.roadrunnerbackend.data.*;
import pt.ua.deti.tqs.roadrunnerbackend.model.Customer;
import pt.ua.deti.tqs.roadrunnerbackend.model.Package;
import pt.ua.deti.tqs.roadrunnerbackend.model.Shop;
import pt.ua.deti.tqs.roadrunnerbackend.model.User;
import pt.ua.deti.tqs.roadrunnerbackend.model.auth.LoginRequest;
import pt.ua.deti.tqs.roadrunnerbackend.model.auth.LoginResponse;
import pt.ua.deti.tqs.roadrunnerbackend.model.enums.Roles;

import java.util.List;
import java.util.UUID;

import static org.junit.Assert.assertEquals;

@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT)
@TestPropertySource(locations = "application-integrationtest.properties")
class AdminControllerIT {

	@LocalServerPort
	private int port;

	@Autowired
	private TestRestTemplate restTemplate;

	@Autowired
	private PackageRepository packageRepository;

	@Autowired
	private PickUpLocationRepository pickUpLocationRepository;

	@Autowired
	private ShopRepository shopRepository;

	@Autowired
	private CustomerRepository customerRepository;

	@Autowired
	private UserRepository userRepository;

	private HttpEntity<String> header;

	@BeforeEach
	public void setUp() {
		packageRepository.deleteAll();
		customerRepository.deleteAll();
		shopRepository.deleteAll();
		userRepository.deleteAll();
		pickUpLocationRepository.deleteAll();


		User admin = new User(UUID.randomUUID(), null, "admin", "admin", "admin", Roles.ROLE_ADMIN, null);
		admin.setPassword("password");
		userRepository.saveAndFlush(admin);

		LoginRequest loginRequest = new LoginRequest("admin", "password");

		LoginResponse response = restTemplate.postForObject("http://localhost:" + port + "/api/auth/login",
				loginRequest,
				LoginResponse.class);
		String token = response.getToken();

		HttpHeaders headers = new HttpHeaders();
		headers.setBearerAuth(token);
		header = new HttpEntity<>(headers);
	}

	@AfterEach
	public void tearDown() {
		packageRepository.deleteAll();
		customerRepository.deleteAll();
		shopRepository.deleteAll();
		userRepository.deleteAll();
		pickUpLocationRepository.deleteAll();
	}

	@Test
	void givenShops_whenGetShops_thenRetrieveShops() {
		Shop shop = new Shop(UUID.randomUUID(), "loja", "loja", "rua", "cidade", 1234, 1234, false);
		shopRepository.saveAndFlush(shop);

		Shop[] shops = restTemplate.exchange("http://localhost:" + port + "/api/shop", HttpMethod.GET, header,
				Shop[].class).getBody();
		assert shops != null;
		Assertions.assertEquals(shop, shops[0]);
	}

	@Test
	void givenShop_whenAddShop_thenShopAdded() {
		Shop shop = new Shop(UUID.randomUUID(), "loja", "loja", "rua", "cidade", 1234, 1234, false);
		HttpEntity<Shop> request = new HttpEntity<>(shop, header.getHeaders());

		restTemplate.exchange("http://localhost:" + port + "/api/shop", HttpMethod.POST, request, Shop.class);
		List<Shop> shops = shopRepository.findAll();
		Assertions.assertEquals(shop, shops.get(0));
	}

	@Test
	void givenShopId_whenGetShop_thenRetrieveShop() {
		Shop shop = new Shop(UUID.randomUUID(), "loja", "loja", "rua", "cidade", 1234, 1234, false);
		shopRepository.saveAndFlush(shop);

		Shop shop2 = restTemplate.exchange("http://localhost:" + port + "/api/shop/" + shop.getId(), HttpMethod.GET,
				header, Shop.class).getBody();
		Assertions.assertEquals(shop, shop2);
	}

	@Test
	void givenShopId_whenDeleteShop_thenShopDeleted() {
		Shop shop = new Shop(UUID.randomUUID(), "loja", "loja", "rua", "cidade", 1234, 1234, false);
		shopRepository.saveAndFlush(shop);

		restTemplate.exchange("http://localhost:" + port + "/api/shop/" + shop.getId(), HttpMethod.DELETE, header,
				Shop.class);
		List<Shop> shops = shopRepository.findALLByDisabled(false);
		Assertions.assertEquals(0, shops.size());
	}

	@Test
	void givenShopId_whenGetPackagesByShop_thenRetrievePackages() {
		Shop shop = new Shop(UUID.randomUUID(), "loja", "loja", "rua", "cidade", 1234, 1234, false);
		shopRepository.saveAndFlush(shop);

		Package package1 = new Package();
		Customer customer = new Customer(UUID.randomUUID(), "customer", "customer", "customer", 123456789);
		customerRepository.saveAndFlush(customer);
		package1.setShop(shop);
		package1.setCustomer(customer);
		packageRepository.saveAndFlush(package1);

		Package[] packages = restTemplate.exchange(
				"http://localhost:" + port + "/api/shop/package?id=" + shop.getId() , HttpMethod.GET, header,
				Package[].class).getBody();

		assert packages != null;
		Assertions.assertEquals(package1, packages[0]);
		
	}

}