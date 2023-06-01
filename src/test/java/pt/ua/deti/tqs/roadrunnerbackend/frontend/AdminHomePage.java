package pt.ua.deti.tqs.roadrunnerbackend.frontend;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;

public class AdminHomePage {

    @FindBy(xpath = "/html/body/div/div/nav/div/div[2]/ul/a[1]/li/a")
    private WebElement packageTab;

    @FindBy(xpath = "/html/body/div/div/nav/div/div[2]/ul/a[2]/li/a")
    private WebElement pickupPointTab;

    @FindBy(xpath = "/html/body/div/div/nav/div/div[2]/ul/a[3]/li/a")
    private WebElement shopsTab;

    @FindBy(xpath = "/html/body/div/div/nav/div/div[2]/ul/a[4]/li/a")
    private WebElement statsTab;

    @FindBy(xpath ="//*[@id=\"searchBar\"]")
    private WebElement citySearchBar;

    @FindBy(xpath = "/html/body/div/div/div/div[1]/div/div/select")
    private WebElement shopSelect;

    @FindBy(xpath = "/html/body/div[1]/div/div[1]/button")
    private WebElement addShopButton;

    @FindBy(xpath = "/html/body/div/div/div/div[2]/table/tbody/tr[1]")
    private WebElement firstPickupPoint;

    public AdminHomePage(WebDriver driver) {
         PageFactory.initElements(driver,this);
    }

    public void enterCity(String city) {
        citySearchBar.sendKeys(city);
    }

    public void clickPackageTab() {
        packageTab.click();
    }

    public void clickPickupPointTab() {
        pickupPointTab.click();
    }

    public void clickShopsTab() {
        shopsTab.click();
    }

    public void clickStatsTab() {
        statsTab.click();
    }

    public void clickAddShopButton() {
        addShopButton.click();
    }

    public void selectShop(String shop) {
        shopSelect.sendKeys(shop);
    }

    public WebElement getCitySearchBarIsDiplayed() {
        return citySearchBar;
    }

    public boolean isPackageTabDisplayed() {return firstPickupPoint.isDisplayed();}
}
