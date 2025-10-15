package pt.ua.tqs;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;

public class BlazeHomePage {
    private WebDriver driver;

    @FindBy(name = "fromPort")
    WebElement fromPortSelect;

    @FindBy(name = "toPort")
    WebElement toPortSelect;

    @FindBy(css = ".btn-primary")
    WebElement findFlightsButton;

    public BlazeHomePage(WebDriver driver) {
        this.driver = driver;
        PageFactory.initElements(driver, this);
        driver.get("https://blazedemo.com/");
    }

    public void selectFromCity(String city) {
        fromPortSelect.sendKeys(city);
    }

    public void selectToCity(String city) {
        toPortSelect.sendKeys(city);
    }

    public FlightsPage findFlights() {
        findFlightsButton.click();
        return new FlightsPage(driver);
    }
}
