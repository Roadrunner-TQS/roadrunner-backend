package pt.ua.deti.tqs.roadrunnerbackend.frontend;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;

public class PartnerHomePage {

    public PartnerHomePage(WebDriver driver) {
        PageFactory.initElements(driver,this);
    }

    @FindBy(xpath = "/html/body/div/div/nav/div/div[2]/ul/a[1]/li/a")
    private WebElement packageTab;

    @FindBy(xpath = "/html/body/div/div/div/div[2]/table/tbody/tr/td[4]/span/span")
    private WebElement statePackage;

    public void clickPackageTab() {
        packageTab.click();
    }

    public String getStatePackage() {
        return statePackage.getText();
    }
}
