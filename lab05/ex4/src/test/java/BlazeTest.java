
import pt.ua.tqs.*;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;

public class BlazeTest {
    private WebDriver driver;

    @Before
    public void setUp() {
        driver = new ChromeDriver();
    }

    @After
    public void tearDown() {
        driver.quit();
    }

    @Test
    public void testFlightPurchase() {
        BlazeHomePage home = new BlazeHomePage(driver);
        home.selectFromCity("San Diego");
        home.selectToCity("Cairo");

        FlightsPage flights = home.findFlights();
        PurchasePage purchase = flights.chooseFlight();

        purchase.fillForm("John", "Street 123", "NY", "NY", "10001", "123456789", "John Doe");
        purchase.submit();

        assertEquals("BlazeDemo Confirmation", purchase.getTitle().trim());
    }
}
