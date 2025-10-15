import static java.lang.invoke.MethodHandles.lookup;
import static org.slf4j.LoggerFactory.getLogger;

import com.microsoft.playwright.Page;
import com.microsoft.playwright.junit.UsePlaywright;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Test;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.slf4j.Logger;

import io.github.bonigarcia.wdm.WebDriverManager;


@UsePlaywright
public class PlaywrightTest {


    @Test
    void reachCalculator(Page page) {

        page.navigate("https://bonigarcia.dev/selenium-webdriver-java/");
        page.click("text=Calculator");
        assertThat(page.url()).isEqualTo("https://bonigarcia.dev/selenium-webdriver-java/slow-calculator.html");
    }
}
