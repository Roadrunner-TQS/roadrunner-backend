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

import static org.junit.jupiter.api.Assertions.*;

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
        driver.get("http://localhost:8085/signin");
        signInPage.fillEmail("admin1@example.com");
        signInPage.fillPassword("admin123");
        signInPage.submit();
    }

    @Given("I am logged in as a partner")
    public void iAmLoggedInAsAPartner() {
        driver.get("http://localhost:8085/signin");
        signInPage.fillEmail("user2@example.com");
        signInPage.fillPassword("user123");
        signInPage.submit();
        wait.until(driver -> driver.getCurrentUrl().equals("http://localhost:8085/packages"));
    }

    @And("I am in the pickup point management page")
    public void iAmInThePickupPointManagementPage() {
        wait.until(driver -> driver.getCurrentUrl().equals("http://localhost:8085/packages"));
        adminHomePage = new AdminHomePage(driver);
        adminHomePage.clickPickupPointTab();
        wait.until(driver -> driver.getCurrentUrl().equals("http://localhost:8085/pickups"));
    }
    @And("I am in the package management page")
    public void iAmInThePackageManagementPage() {
        wait.until(driver -> driver.getCurrentUrl().equals("http://localhost:8085/packages"));
        adminHomePage = new AdminHomePage(driver);
        adminHomePage.clickPackageTab();
        wait.until(driver -> driver.getCurrentUrl().equals("http://localhost:8085/packages"));
    }

    @And("I am in the partner shop management page")
    public void iAmInThePartnerShopManagementPage() {
        wait.until(driver -> driver.getCurrentUrl().equals("http://localhost:8085/packages"));
        adminHomePage = new AdminHomePage(driver);
        adminHomePage.clickShopsTab();
        wait.until(driver -> driver.getCurrentUrl().equals("http://localhost:8085/shops"));
    }

    @When("I search for pickup points in {string}")
    public void iSearchForPickupPointsIn(String city) {
        wait.until(driver -> driver.getCurrentUrl().equals("http://localhost:8085/pickups"));
        adminHomePage.enterCity(city);
    }

    @When("I select the pickup point {string}")
    public void iClickOnThePickupLocation(String pickupLocation) {
        adminHomePage.selectPickupPoint(pickupLocation);
    }

    @When("I click on the add partner button")
    public void iClickOnTheAddPartnerButton() {
        adminHomePage.clickAddShopButton();
    }

    @Then("I should see a list of pickup points")
    public void iShouldSeeAListOfPickupPoints() {
        assertTrue(adminHomePage.isPackageTabDisplayed());
    }

    @Then("I should see a list of packages")
    public void iShouldSeeAListOfPackages() {
        assertTrue(adminHomePage.isPackageTabDisplayed());
    }

    @Then("I should see the partner dashboard")
    public void iShouldSeeThePartnerDashboard() {
        partnerHomePage = new PartnerHomePage(driver);
        partnerHomePage.clickPackageTab();
        wait.until(driver -> driver.getCurrentUrl().equals("http://localhost:8085/packages"));
        assertTrue(partnerHomePage.isPackageTabDisplayed());
    }
    @Then("I should see the new partner in the list of shops")
    public void iShouldSeeTheNewPartnerInTheListOfShops() {
        assertTrue(adminHomePage.isPackageTabDisplayed());
    }

    @Then("I should see a list of partner shops")
    public void iShouldSeeAListOfPartnerShops() {
        assertTrue(adminHomePage.isPackageTabDisplayed());
    }

   @Given("I have an order")
    public void iHaveAnOrder() {
       wait.until(driver -> driver.getCurrentUrl().equals("http://localhost:8085/packages"));
       partnerHomePage.clickPackageTab();

   }

    @When("I click on the accept button of the pickup point")
    public void iClickOnTheAcceptButtonOfThePickupPoint() {
        adminHomePage.clickAcceptPickupPointButton();
    }

    @Then("I should see a pickup point in the list of pickup points with accepted status Yes")
    public void iShouldSeeAPickupPointInTheListOfPickupPointsWithAcceptedStatusYes() {
        assertTrue(adminHomePage.isPackageTabDisplayed());
        assertTrue(adminHomePage.changeStatusPickupPointButton());
    }

    @When("I click on the remove button of the pickup point")
    public void iClickOnTheRemoveButtonOfThePickupPoint() {
        assertTrue(adminHomePage.isPackageTabDisplayed());
        adminHomePage.clickRemovePickupPointButton();
    }

    @Then("I should not see a pickup point in the list of pickup")
    public void iShouldNotSeeAPickupPointInTheListOfPickupPoints() {
        assertTrue(adminHomePage.isPackageTabDisplayed());
        assertTrue(adminHomePage.removePickupPointButton());
    }

    @And("I should see the package status as {string}")
    public void iShouldSeeThePackageStatusAs(String status) {
        assertTrue(adminHomePage.isPackageTabDisplayed());
        assertEquals(status, adminHomePage.getPackageStatus(status));
        assertNotEquals(0, adminHomePage.numberOfPackages(status));
    }

    @When("I change the status of the package to {string}")
    public void iChangeTheStatusOfThePackageTo(String status) {
        assertTrue(adminHomePage.isPackageTabDisplayed());
        adminHomePage.changeStatusPackage(status);
    }

    @Then("I should see a package in the list of packages with status {string}")
    public void iShouldSeeAPackageInTheListOfPackagesWithStatus(String status) {
        assertTrue(adminHomePage.isPackageTabDisplayed());
        assertNotEquals(0, adminHomePage.numberOfPackages(status));
    }

    @And("I am in the statistics page")
    public void iAmInTheStatisticsPage() {
        adminHomePage = new AdminHomePage(driver);
        wait.until(driver -> driver.getCurrentUrl().equals("http://localhost:8085/packages"));
        adminHomePage.clickStatsTab();
        wait.until(driver -> driver.getCurrentUrl().equals("http://localhost:8085/statistics"));
    }

    @Then("I should see the statistics")
    public void iShouldSeeTheStatistics() {
        assertTrue(adminHomePage.isStatsDisplayed());
    }

    @When("I select the package")
    public void iSelectThePackage() {
        adminHomePage.selectPackage();
    }

    @Then("I should see the package details")
    public void iShouldSeeThePackageDetails() {
        assertTrue(adminHomePage.isPackageDetailsDisplayed());
    }

    @And("I should see the order status as {string}")
    public void iShouldSeeTheOrderStatusAs(String status){
        assertEquals(status, partnerHomePage.getPackageStatus(status));
        assertNotEquals(0, partnerHomePage.numberOfPackages(status));
    }

    @When("I checkin the order")
    public void iCheckinTheOrder() {
        partnerHomePage.clickChangeStatusButton();
        partnerHomePage.clickAcceptButton();
    }

    @When("I checkout the order")
    public void iCheckoutTheOrder() {
        partnerHomePage.clickChangeStatusButton();
        partnerHomePage.clickCheckoutButton();
    }

    @When("I return the order")
    public void iReturnTheOrder() {
        partnerHomePage.clickChangeStatusButton();
        partnerHomePage.clickReturnButton();
    }

    @When("I click on the remove button of the partner shop")
    public void iClickOnTheRemoveButtonOfThePartnerShop() {
        adminHomePage.clickRemoveShopButton();
    }

    @Then("I should not see the partner shop in the list of partner shops")
    public void iShouldNotSeeThePartnerShopInTheListOfPartnerShops() {
        assertTrue(adminHomePage.shopstabDisplayed());
    }
}
