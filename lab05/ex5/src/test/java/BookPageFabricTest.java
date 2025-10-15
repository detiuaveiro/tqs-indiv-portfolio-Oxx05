import org.junit.jupiter.api.Test;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import pt.ua.tqs.*;

public class BookPageFabricTest {

    @Test
    void testBookSearch() {
        WebDriver driver = new ChromeDriver();

        BookSearchMain mainPage = new BookSearchMain(driver);
        mainPage.enterSearch("Harry Potter");
        BookSearchResults results = mainPage.clickSearch();
        results.selectBook();

        driver.quit();
    }
}
