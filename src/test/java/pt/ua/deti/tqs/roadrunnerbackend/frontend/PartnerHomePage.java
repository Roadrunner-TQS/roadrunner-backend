package pt.ua.deti.tqs.roadrunnerbackend.frontend;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;

import static org.junit.Assert.assertNotEquals;

public class PartnerHomePage {
    private final WebDriver driver;

    public PartnerHomePage(WebDriver driver) {
        this.driver = driver;
        PageFactory.initElements(driver,this);
    }

    @FindBy(xpath = "/html/body/div/div/nav/div/div[2]/ul/a[1]/li/a")
    private WebElement packageTab;

    @FindBy(xpath = "//span[contains(text(),'Change')]")
    private WebElement changeStatusPickupPointButton;

    @FindBy(xpath = "//span[contains(text(),'Save')]")
    private WebElement saveButton;

    @FindBy(xpath = "//option[contains(text(),'AVAILABLE')]")
    private WebElement availableStatus;

    @FindBy(xpath = "//option[contains(text(),'DELIVERED')]")
    private WebElement deliveredStatus;

    @FindBy(xpath = "//option[contains(text(),'RETURNED')]")
    private WebElement returnState;


    public void clickPackageTab() {
        packageTab.click();
    }

    public String getPackageStatus(String status) {
        return driver.findElement(By.xpath("//span[contains(text(),'"+status+"')]")).getText();
    }

    public int numberOfPackages(String status) {
        return driver.findElements(By.xpath("//span[contains(text(),'"+status+"')]")).size();
    }

    public void clickChangeStatusButton() {
        changeStatusPickupPointButton.click();
    }

    public void clickAcceptButton() {
        availableStatus.click();
        saveButton.click();
    }

    public boolean isPackageTabDisplayed() {
        return packageTab.isDisplayed();
    }

    public void clickCheckoutButton() {
        deliveredStatus.click();
        saveButton.click();
    }

    public void clickReturnButton() {
        returnState.click();
        saveButton.click();
    }
}
