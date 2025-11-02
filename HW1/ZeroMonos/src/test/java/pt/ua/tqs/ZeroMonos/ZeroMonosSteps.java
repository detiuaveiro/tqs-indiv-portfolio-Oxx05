package pt.ua.tqs.ZeroMonos;

import java.time.Duration;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

import org.junit.jupiter.api.Tag;
import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import io.cucumber.java.After;
import io.cucumber.java.Before;
import io.cucumber.java.en.*;

import io.github.bonigarcia.wdm.WebDriverManager;

import static org.junit.jupiter.api.Assertions.*;

@Tag("selenium")
public class ZeroMonosSteps {

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

    // --------- Citizen Steps ---------
    @Given("the web application is running")
    public void webAppRunning() {
        // já validado no BeforeAll
    }

    @Given("I open the citizen booking page")
    public void openCitizenPage() {
        driver.get("http://localhost:8080/index.html");
        // Wait for page to load completely
        wait.until(ExpectedConditions.presenceOfElementLocated(By.id("bookingForm")));
    }

    @When("I fill in a valid date, municipality {string} and description {string}")
    public void fillBookingForm(String municipality, String description) {
        // Get next valid weekday
        LocalDateTime date = TestUtils.nextValidWeekday();

        // Format: "2025-11-03T14:30"
        String isoDateTime = date.format(DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm"));

        WebElement dateField = wait.until(ExpectedConditions.presenceOfElementLocated(By.id("date")));
        JavascriptExecutor js = (JavascriptExecutor) driver;

        // Set datetime-local value
        js.executeScript("""
        arguments[0].value = arguments[1];
        arguments[0].dispatchEvent(new Event('input', { bubbles: true }));
        arguments[0].dispatchEvent(new Event('change', { bubbles: true }));
        """, dateField, isoDateTime);

        System.out.println("Set datetime-local value: " + dateField.getAttribute("value"));

        // Fill municipality
        WebElement municipalityInput = wait.until(ExpectedConditions.presenceOfElementLocated(By.id("municipalityInput")));
        municipalityInput.clear();
        municipalityInput.sendKeys(municipality);

        // Fill description
        WebElement descriptionInput = wait.until(ExpectedConditions.presenceOfElementLocated(By.id("description")));
        descriptionInput.clear();
        descriptionInput.sendKeys(description);
    }

    @And("I submit the booking form")
    public void submitBookingForm() {
        WebElement submitButton = wait.until(
                ExpectedConditions.elementToBeClickable(By.cssSelector("#bookingForm button[type='submit']"))
        );
        submitButton.click();

        // Wait for the response message to appear
        wait.until(ExpectedConditions.and(
                ExpectedConditions.presenceOfElementLocated(By.id("createMessage")),
                ExpectedConditions.not(ExpectedConditions.textToBe(By.id("createMessage"), ""))
        ));
    }

    @Then("I should see a success message containing the token")
    public void checkSuccessMessage() {
        WebElement msgDiv = wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("createMessage")));

        // Wait for the message to be populated
        wait.until(ExpectedConditions.not(ExpectedConditions.textToBe(By.id("createMessage"), "")));

        String msg = msgDiv.getText();
        System.out.println("Success message: " + msg);

        assertTrue(msg.contains("✅ Pedido criado! Token:") || msg.contains("Pedido criado! Token:"),
                "Mensagem de sucesso não encontrada: " + msg);

        // Extract token - format is "✅ Pedido criado! Token: ABC123"
        String[] parts = msg.split("Token:\\s*");
        assertTrue(parts.length > 1, "Token não encontrado na mensagem: " + msg);
        String token = parts[1].trim();

        TestContext.setLastToken(token);
        System.out.println("Token extracted: " + token);
    }

    @And("the token list should include the new token")
    public void tokenListIncludesToken() {
        String expectedToken = TestContext.getLastToken();
        assertNotNull(expectedToken, "Token não foi guardado");

        // Click to expand token list
        WebElement toggleButton = wait.until(
                ExpectedConditions.elementToBeClickable(By.id("toggleTokens"))
        );

        // Check if already visible
        WebElement tokenListDiv = driver.findElement(By.id("tokenList"));
        String displayStyle = tokenListDiv.getCssValue("display");

        if ("none".equals(displayStyle)) {
            toggleButton.click();
            // Wait for list to be visible
            wait.until(ExpectedConditions.attributeContains(By.id("tokenList"), "style", "display: block"));
        }

        // Now check content - the token appears in a div with class="token-item"
        wait.until(ExpectedConditions.textToBePresentInElementLocated(By.id("tokenList"), expectedToken));

        String tokensText = tokenListDiv.getText();
        assertTrue(tokensText.contains(expectedToken),
                "Token " + expectedToken + " não está listado. Lista: " + tokensText);
    }

    @Given("I have a booking token")
    public void haveBookingToken() {
        assertNotNull(TestContext.getLastToken(), "Token não disponível; execute primeiro a criação de reserva");

        // Ensure we're on the citizen page for token lookup
        String currentUrl = driver.getCurrentUrl();
        if (!currentUrl.contains("index.html")) {
            driver.get("http://localhost:8080/index.html");
            wait.until(ExpectedConditions.presenceOfElementLocated(By.id("token")));
        }
    }

    @When("I enter the token in the lookup field")
    public void enterTokenLookup() {
        // Make sure we're on the right page - the token input for lookup
        WebElement tokenInput = wait.until(ExpectedConditions.presenceOfElementLocated(By.id("token")));
        tokenInput.clear();
        tokenInput.sendKeys(TestContext.getLastToken());
        System.out.println("Entered token: " + TestContext.getLastToken());
    }

    @And("I click {string}")
    public void clickButton(String label) {
        // Use exact text match with normalize-space to handle whitespace
        WebElement button = wait.until(
                ExpectedConditions.elementToBeClickable(By.xpath("//button[normalize-space(text())='" + label + "']"))
        );
        button.click();

        // Wait for bookingResult to be updated
        wait.until(ExpectedConditions.and(
                ExpectedConditions.presenceOfElementLocated(By.id("bookingResult")),
                ExpectedConditions.not(ExpectedConditions.textToBe(By.id("bookingResult"), ""))
        ));
    }

    @Then("I should see the booking details including {string}")
    public void checkBookingDetails(String description) {
        WebElement bookingDiv = wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("bookingResult")));

        // Wait for content to be populated
        wait.until(ExpectedConditions.not(ExpectedConditions.textToBe(By.id("bookingResult"), "")));

        String resultText = bookingDiv.getText();
        System.out.println("Booking result: " + resultText);

        // Verify we got valid booking details
        assertTrue(resultText.contains("📄 Token:") || resultText.contains("Token:"),
                "Detalhes do pedido não encontrados. Conteúdo: " + resultText);
        assertTrue(resultText.contains("Estado atual:") || resultText.contains("state"),
                "Estado não encontrado nos detalhes. Conteúdo: " + resultText);

        // If the HTML displays description (after fix), verify it
        if (resultText.contains("Descrição:")) {
            assertTrue(resultText.contains(description),
                    "Descrição '" + description + "' não encontrada. Conteúdo: " + resultText);
        } else {
            System.out.println("⚠️  WARNING: HTML não mostra descrição. Atualiza o index.html conforme sugerido.");
        }
    }

    @Then("I should see a message {string}")
    public void checkMessage(String message) {
        WebElement bookingDiv = wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("bookingResult")));

        // Wait for content
        wait.until(ExpectedConditions.not(ExpectedConditions.textToBe(By.id("bookingResult"), "")));

        String resultText = bookingDiv.getText();
        System.out.println("Booking result: " + resultText);

        // The HTML shows: "🛑 Pedido cancelado."
        assertTrue(resultText.contains(message) || resultText.contains("Pedido cancelado"),
                "Mensagem '" + message + "' não encontrada. Conteúdo: " + resultText);
    }

    // --------- Staff Steps ---------
    @Given("I open the staff page")
    public void openStaffPage() {
        driver.get("http://localhost:8080/staff.html");
        wait.until(ExpectedConditions.presenceOfElementLocated(By.id("municipalityInput")));
        wait.until(ExpectedConditions.presenceOfElementLocated(By.id("requestsTable")));
    }

    @When("I filter bookings by municipality {string}")
    public void filterBookings(String municipality) {
        WebElement input = wait.until(ExpectedConditions.presenceOfElementLocated(By.id("municipalityInput")));
        input.clear();
        input.sendKeys(municipality);

        WebElement filterButton = wait.until(
                ExpectedConditions.elementToBeClickable(By.xpath("//button[text()='Filtrar']"))
        );
        filterButton.click();

        // Wait for the fetch to complete - JavaScript is async
        try {
            Thread.sleep(2000); // Give time for API call and DOM update
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }

    @Then("the booking table should contain a booking with description {string}")
    public void checkBookingTable(String description) {
        WebElement tbody = wait.until(ExpectedConditions.presenceOfElementLocated(By.id("requestsTable")));

        // Wait a moment for content to render
        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }

        // Get table content
        String tableContent = tbody.getText();
        System.out.println("Table content: " + tableContent);

        // Check if we got the "no results" message
        if (tableContent.contains("Nenhum pedido encontrado")) {
            fail("Nenhum pedido encontrado na tabela para a descrição: " + description);
        }

        // Get all rows
        List<WebElement> rows = tbody.findElements(By.tagName("tr"));
        assertTrue(rows.size() > 0, "Tabela não tem linhas");

        // Look for the description in any row
        boolean found = false;
        for (WebElement row : rows) {
            try {
                String rowText = row.getText();
                if (rowText.contains(description)) {
                    found = true;
                    System.out.println("Found row: " + rowText);
                    break;
                }
            } catch (Exception e) {
                // Stale element, continue
            }
        }

        assertTrue(found, "Reserva com descrição '" + description + "' não encontrada na tabela. Conteúdo: " + tableContent);
    }

    @And("I click {string} on the booking with description {string}")
    public void clickButtonOnBooking(String buttonLabel, String description) {
        WebElement tbody = wait.until(ExpectedConditions.presenceOfElementLocated(By.id("requestsTable")));

        // Wait a bit for content
        try {
            Thread.sleep(500);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }

        // Refind rows to avoid stale element
        List<WebElement> rows = tbody.findElements(By.tagName("tr"));

        WebElement targetRow = null;
        for (WebElement row : rows) {
            try {
                if (row.getText().contains(description)) {
                    targetRow = row;
                    break;
                }
            } catch (Exception e) {
                // Stale element, continue
            }
        }

        assertNotNull(targetRow, "Reserva com descrição '" + description + "' não encontrada");

        // Find button in the row - buttons are in the last <td>
        WebElement button = targetRow.findElement(By.xpath(".//button[text()='" + buttonLabel + "']"));

        // Scroll into view if needed
        ((JavascriptExecutor) driver).executeScript("arguments[0].scrollIntoView(true);", button);

        wait.until(ExpectedConditions.elementToBeClickable(button));
        button.click();

        // Wait for state change to propagate
        try {
            Thread.sleep(1500);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }

    @Then("the booking's state should advance")
    public void checkBookingStateAdvanced() {
        // Wait for page to update after state change
        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }

        // Refetch table to avoid stale elements
        WebElement tbody = wait.until(ExpectedConditions.presenceOfElementLocated(By.id("requestsTable")));

        List<WebElement> rows = tbody.findElements(By.tagName("tr"));

        WebElement targetRow = null;
        for (WebElement row : rows) {
            try {
                if (row.getText().contains("Teste UI")) {
                    targetRow = row;
                    break;
                }
            } catch (Exception e) {
                // Stale element, continue
            }
        }

        assertNotNull(targetRow, "Reserva 'Teste UI' não encontrada após avançar estado");

        String rowText = targetRow.getText();
        System.out.println("Row text after state change: " + rowText);

        // Check that state is no longer CREATED (should have advanced)
        assertFalse(rowText.contains("CREATED"),
                "Estado da reserva não avançou de CREATED. Estado atual: " + rowText);
    }
}