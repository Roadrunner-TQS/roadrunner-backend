package pt.ua.deti.tqs.roadrunnerbackend.frontend;
import io.cucumber.java.Before;
import io.cucumber.java.en.And;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import io.github.bonigarcia.wdm.WebDriverManager;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Duration;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.endsWith;

public class SearchNearbyPickUpsSteps {

    private WebDriver driver;
    private WebDriverWait wait;

    private SignInPage signInPage;

    private AdminHomePage adminHomePage;

    @Before
    public void setup() {
        driver = WebDriverManager.firefoxdriver().create();
        signInPage = new SignInPage(driver);
        wait = new WebDriverWait(driver, Duration.ofSeconds(10));
    }

    @Given("I am logged in as an admin")
    public void iAmLoggedInAsAnAdmin() {
        driver.get("http://localhost:5173/signin");
        signInPage.fillEmail("admin1@example.com");
        signInPage.fillPassword("admin123");
        signInPage.submit();
    }

    @And("I am in the pickup point management page")
    public void iAmInThePickupPointManagementPage() {
        adminHomePage = new AdminHomePage(driver);
    }

    @When("I search for pickup points in {string}")
    public void iSearchForPickupPointsIn(String city) throws InterruptedException {
        wait.until(driver -> driver.getCurrentUrl().equals("http://localhost:5173/packages"));
        adminHomePage.clickPickupPointTab();
        wait.until(driver -> driver.getCurrentUrl().equals("http://localhost:5173/pickups"));
        Thread.sleep(1000);
        adminHomePage.enterCity(city);
    }

    @Then("I should see a list of pickup points")
    public void iShouldSeeAListOfPickupPoints() {

    }
}
