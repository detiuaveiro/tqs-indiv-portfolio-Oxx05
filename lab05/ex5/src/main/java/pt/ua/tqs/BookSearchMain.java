package pt.ua.tqs;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;
import com.microsoft.playwright.Locator;
import com.microsoft.playwright.Page;
import org.openqa.selenium.support.ui.*;

import java.time.Duration;

public class BookSearchMain {
    private final WebDriver driver;

    @FindBy(css = "input[data-testid='book-search-input']")
    private WebElement searchBox;

    @FindBy(css = "img.Navbar_searchBtn_26UF")
    private WebElement searchButton;  // ou usa By.tagName("button") se só houver um


    public BookSearchMain(WebDriver driver) {
        this.driver = driver;
        driver.get("https://cover-bookstore.onrender.com/");
        PageFactory.initElements(driver, this);
        Wait<WebDriver> wait = new WebDriverWait(driver, Duration.ofSeconds(10));
        wait.until(ExpectedConditions.visibilityOf(searchBox));
    }

    public void enterSearch(String query) {
        searchBox.click();
        searchBox.clear();
        searchBox.sendKeys(query);
    }

    public BookSearchResults clickSearch() {
        Wait<WebDriver> wait = new WebDriverWait(driver, Duration.ofSeconds(10));
        wait.until(ExpectedConditions.visibilityOf(searchButton));
        searchButton.click();
        return new BookSearchResults(driver);
    }
}
