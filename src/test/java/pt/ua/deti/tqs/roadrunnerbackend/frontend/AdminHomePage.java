package pt.ua.deti.tqs.roadrunnerbackend.frontend;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;

import java.util.Objects;

public class AdminHomePage {
     private final WebDriver driver;

     private Integer numberOfPickupPointsToAccept, numberOfPickupPoints;

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
    private WebElement selectPickupPoint;

    @FindBy(xpath = "/html/body/div[1]/div/div[1]/button")
    private WebElement addShopButton;

    @FindBy(xpath = "/html/body/div/div/div/div[2]/table/tbody/tr[7]/td[1]/a")
    private WebElement firstPickupPoint;

    @FindBy(xpath = "//p[contains(text(),'Accept')]")
    private WebElement acceptPickupPointButton;

    @FindBy(xpath = "//p[contains(text(),'Delete')]")
    private WebElement deletePickupPointButton;

    @FindBy(xpath = "//span[contains(text(),'Change')]")
    private WebElement changeStatusPickupPointButton;

    @FindBy(xpath = "//option[contains(text(),'SHIPPING')]")
    private WebElement shippingStatus;

    @FindBy(xpath = "//option[contains(text(),'DENIED')]")
    private WebElement deniedStatus;

    @FindBy(xpath = "//span[contains(text(),'Save')]")
    private WebElement saveButton;

    @FindBy(xpath = "/html/body/div/div/div/div[1]/div[1]/div/div/h2")
    private WebElement dataStats;

    @FindBy(xpath = "//p[contains(text(),'Delete')]")
    private WebElement deleteShopButton;


    public AdminHomePage(WebDriver driver) {
        this.driver = driver;
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
    public boolean shopstabDisplayed() {
        return shopsTab.isDisplayed();
    }

    public void clickStatsTab() {
        statsTab.click();
    }

    public void clickAddShopButton() {
        addShopButton.click();
    }

    public boolean isPackageTabDisplayed() {return firstPickupPoint.isDisplayed();}

    public void selectPickupPoint(String pickupLocation) {
        selectPickupPoint.sendKeys(pickupLocation);
    }


    public void clickAcceptPickupPointButton() {
        numberOfPickupPointsToAccept = driver.findElements(By.xpath("//p[contains(text(),'Accept')]")).size();
        acceptPickupPointButton.click();
    }

    public Boolean changeStatusPickupPointButton() {
        return numberOfPickupPointsToAccept != driver.findElements(By.xpath("//p[contains(text(),'Accept')]")).size();
    }

    public void clickRemovePickupPointButton() {
        numberOfPickupPoints = driver.findElements(By.xpath("//p[contains(text(),'Delete')]")).size();
        deletePickupPointButton.click();
    }

    public Boolean removePickupPointButton() {
        return numberOfPickupPoints != driver.findElements(By.xpath("//p[contains(text(),'Delete')]")).size();
    }

    public String getPackageStatus(String status) {
        return driver.findElement(By.xpath("//span[contains(text(),'"+status+"')]")).getText();
    }

    public void changeStatusPackage( String status) {
        assert status.equals("SHIPPING") || status.equals("DENIED");
        int previus = driver.findElements(By.xpath("//span[contains(text(), '"+status+"')]")).size();
        changeStatusPickupPointButton.click();
        if (Objects.equals(status, "SHIPPING")) {
            shippingStatus.click();
        } else {
            deniedStatus.click();
        }
        saveButton.click();
        int after = driver.findElements(By.xpath("//span[contains(text(), '"+status+"')]")).size();
        assert previus != after;
    }

    public Integer numberOfPackages(String status) {
        return driver.findElements(By.xpath("//span[contains(text(),'"+status+"')]")).size();
    }

    public boolean isStatsDisplayed() {
        return dataStats.isDisplayed();
    }

    public void selectPackage() {
        firstPickupPoint.click();
    }

    public boolean isPackageDetailsDisplayed() {
        return driver.findElement(By.xpath("/html/body/div/div/div/div/div")).isDisplayed();
    }

    public void clickRemoveShopButton() {
        deleteShopButton.click();
    }
}
