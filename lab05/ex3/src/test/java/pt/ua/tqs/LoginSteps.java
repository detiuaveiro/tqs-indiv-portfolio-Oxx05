package pt.ua.tqs;

import java.time.Duration;
import java.util.List;

import io.cucumber.java.After;
import io.cucumber.java.Before;
import io.cucumber.java.en.And;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;

import io.github.bonigarcia.wdm.WebDriverManager;

import org.openqa.selenium.By;
import org.openqa.selenium.Keys;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import static org.junit.jupiter.api.Assertions.*;

public class LoginSteps {

    private WebDriver driver;
    private WebDriverWait wait;

    @Before
    public void setup() {
        driver = WebDriverManager.chromedriver().create();
        wait = new WebDriverWait(driver, Duration.ofSeconds(10));
    }

    @After
    public void teardown() {
        if (driver != null) {
            driver.quit();
        }
    }

    @When("I navigate to {string}")
    public void iNavigateTo(String url) {
        driver.get(url);
    }

    @And("I search for {string}")
    public void iSearchFor(String title) {
        // Espera o campo de busca estar clicável
        WebElement searchInput = wait.until(ExpectedConditions.elementToBeClickable(
                By.cssSelector(".Navbar_searchBarContainer__3UbnF .Navbar_searchBarInput__w8FwI")
        ));
        searchInput.click();
        searchInput.sendKeys(title, Keys.ENTER);
    }

    @Then("I should see {int} result")
    @Then("I should see {int} results")
    public void iShouldSee(int expectedCount) {
        // Tenta encontrar elementos visíveis, mas não lança exceção se não houver nenhum
        List<WebElement> results = driver.findElements(By.cssSelector(".SearchList_bookTitle__1wo4a"));

        // Se esperava >0 resultados, podemos esperar até que pelo menos um esteja presente
        if (expectedCount > 0) {
            WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10));
            results = wait.until(ExpectedConditions.visibilityOfAllElementsLocatedBy(
                    By.cssSelector(".SearchList_bookTitle__1wo4a")
            ));
        }

        assertEquals(expectedCount, results.size(), "Número de resultados diferente do esperado");
    }

    @And("one of them should be titled {string}")
    public void foundTitle(String title) {
        List<WebElement> results = driver.findElements(By.cssSelector(".SearchList_bookTitle__1wo4a"));
        boolean titleFound = results.stream()
                .anyMatch(element -> element.getText().equalsIgnoreCase(title));
        assertTrue(titleFound, "Book title not found: " + title);
        System.out.println("Title found: " + title);
    }
}
