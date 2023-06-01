package pt.ua.deti.tqs.roadrunnerbackend.frontend;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;

public class SignInPage {

    @FindBy(id = "email")
    private WebElement emailInput;

    @FindBy(id = "password")
    private WebElement passwordInput;

    @FindBy(xpath = "/html/body/div/div/div/div/form/button")
    private WebElement submit;

    public SignInPage(WebDriver driver) {
        PageFactory.initElements(driver,this);
    }

    public void fillEmail(String email) {
        emailInput.sendKeys(email);
    }

    public void fillPassword(String password) {
        passwordInput.sendKeys(password);
    }

    public void submit() {
        submit.click();
    }




}
