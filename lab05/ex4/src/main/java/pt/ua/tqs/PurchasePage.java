package pt.ua.tqs;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;

public class PurchasePage {
    private final WebDriver driver;

    @FindBy(id = "inputName")
    WebElement nameField;
    @FindBy(id = "address")
    WebElement addressField;
    @FindBy(id = "city")
    WebElement cityField;
    @FindBy(id = "state")
    WebElement stateField;
    @FindBy(id = "zipCode")
    WebElement zipCodeField;
    @FindBy(id = "creditCardNumber")
    WebElement creditCardField;
    @FindBy(id = "nameOnCard")
    WebElement nameOnCardField;
    @FindBy(css = ".btn-primary")
    WebElement purchaseButton;

    public PurchasePage(WebDriver driver) {
        this.driver = driver;
        PageFactory.initElements(driver, this);
    }

    public void fillForm(String name, String address, String city, String state, String zip, String cc, String nameOnCard) {
        nameField.sendKeys(name);
        addressField.sendKeys(address);
        cityField.sendKeys(city);
        stateField.sendKeys(state);
        zipCodeField.sendKeys(zip);
        creditCardField.sendKeys(cc);
        nameOnCardField.sendKeys(nameOnCard);
    }

    public void submit() {
        purchaseButton.click();
    }

    public String getTitle() {
        return driver.getTitle();
    }
}
