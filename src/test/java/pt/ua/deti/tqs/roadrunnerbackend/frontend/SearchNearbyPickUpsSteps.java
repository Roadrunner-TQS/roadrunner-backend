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

import static org.junit.jupiter.api.Assertions.assertTrue;

public class SearchNearbyPickUpsSteps {

    private WebDriver driver;
    private WebDriverWait wait;

    private SignInPage signInPage;

    private AdminHomePage adminHomePage;

    private PartnerHomePage partnerHomePage;

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

    @Given("I am logged in as an partner")
    public void iAmLoggedInAsAnPartner() {
        driver.get("http://localhost:8085/signin");
        signInPage.fillEmail("user2@example.com");
        signInPage.fillPassword("user123");
        signInPage.submit();
        partnerHomePage = new PartnerHomePage(driver);
        wait.until(driver -> driver.getCurrentUrl().equals("http://localhost:5173/packages"));
    }

    @And("I am in the pickup point management page")
    public void iAmInThePickupPointManagementPage() {
        adminHomePage = new AdminHomePage(driver);
    }

    @When("I search for pickup points in {string}")
    public void iSearchForPickupPointsIn(String city) {
        wait.until(driver -> driver.getCurrentUrl().equals("http://localhost:5173/packages"));
        adminHomePage.clickPickupPointTab();
        wait.until(driver -> driver.getCurrentUrl().equals("http://localhost:5173/pickups"));
        adminHomePage.enterCity(city);
    }

    @Then("I should see a list of pickup points")
    public void iShouldSeeAListOfPickupPoints() {
        assertTrue(adminHomePage.isPackageTabDisplayed());
    }

    @When("I click on the pickup location {string}")
    public void iClickOnThePickupLocation(String pickupLocation) {

    }

   @Given("I have an order")
    public void iHaveAnOrder() {
       wait.until(driver -> driver.getCurrentUrl().equals("http://localhost:5173/packages"));
         partnerHomePage.clickPackageTab();
   }
}
