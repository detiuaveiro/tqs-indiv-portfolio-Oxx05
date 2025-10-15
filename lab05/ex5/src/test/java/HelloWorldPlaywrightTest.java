import com.microsoft.playwright.*;
import org.junit.jupiter.api.*;

public class HelloWorldPlaywrightTest {

    Playwright playwright;
    Browser browser;
    Page page;

    @BeforeEach
    void setup() {
        playwright = Playwright.create();
        browser = playwright.chromium().launch(
                new BrowserType.LaunchOptions()
                        .setHeadless(false)    // mostra o browser
                        .setSlowMo(500)        // espera 0.5s entre ações
        );
        page = browser.newPage();
    }

    @Test
    void testNavigateAndCheckUrl() {
        page.navigate("https://bonigarcia.dev/selenium-webdriver-java/");
        page.click("text=Slow calculator");
        Assertions.assertTrue(page.url().contains("calculator"));
    }

    @AfterEach
    void teardown() {
        browser.close();
        playwright.close();
    }
}
