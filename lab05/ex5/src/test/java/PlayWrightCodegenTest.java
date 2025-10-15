import com.microsoft.playwright.junit.UsePlaywright;
import com.microsoft.playwright.Page;
import com.microsoft.playwright.options.*;

import org.junit.jupiter.api.*;
import static com.microsoft.playwright.assertions.PlaywrightAssertions.*;

@UsePlaywright
public class PlayWrightCodegenTest {
    @Test
    void test(Page page) {
        page.navigate("https://blazedemo.com/");
        page.locator("select[name=\"fromPort\"]").selectOption("Boston");
        page.locator("select[name=\"toPort\"]").selectOption("Dublin");
        page.getByRole(AriaRole.BUTTON, new Page.GetByRoleOptions().setName("Find Flights")).click();
        page.getByRole(AriaRole.ROW, new Page.GetByRoleOptions().setName("Choose This Flight 43 Virgin")).getByRole(AriaRole.BUTTON).click();
        page.getByRole(AriaRole.TEXTBOX, new Page.GetByRoleOptions().setName("Name").setExact(true)).click();
        page.getByRole(AriaRole.TEXTBOX, new Page.GetByRoleOptions().setName("Name").setExact(true)).fill("dada");
        page.getByRole(AriaRole.TEXTBOX, new Page.GetByRoleOptions().setName("Address")).click();
        page.getByRole(AriaRole.TEXTBOX, new Page.GetByRoleOptions().setName("Address")).fill("adadad");
        page.getByRole(AriaRole.TEXTBOX, new Page.GetByRoleOptions().setName("City")).click();
        page.getByRole(AriaRole.TEXTBOX, new Page.GetByRoleOptions().setName("City")).fill("addad");
        page.getByRole(AriaRole.TEXTBOX, new Page.GetByRoleOptions().setName("State")).click();
        page.getByRole(AriaRole.TEXTBOX, new Page.GetByRoleOptions().setName("Zip Code")).click();
        page.getByRole(AriaRole.TEXTBOX, new Page.GetByRoleOptions().setName("State")).click();
        page.getByRole(AriaRole.TEXTBOX, new Page.GetByRoleOptions().setName("State")).fill("adada");
        page.getByRole(AriaRole.TEXTBOX, new Page.GetByRoleOptions().setName("Zip Code")).click();
        page.getByRole(AriaRole.TEXTBOX, new Page.GetByRoleOptions().setName("State")).fill("adadad");
        page.getByRole(AriaRole.TEXTBOX, new Page.GetByRoleOptions().setName("Zip Code")).fill("ddadad");
        page.getByRole(AriaRole.TEXTBOX, new Page.GetByRoleOptions().setName("Year")).click();
        page.getByRole(AriaRole.TEXTBOX, new Page.GetByRoleOptions().setName("Credit Card Number")).click();
        page.getByRole(AriaRole.TEXTBOX, new Page.GetByRoleOptions().setName("Credit Card Number")).fill("adadada");
        page.getByRole(AriaRole.TEXTBOX, new Page.GetByRoleOptions().setName("Name on Card")).click();
        page.getByRole(AriaRole.TEXTBOX, new Page.GetByRoleOptions().setName("Name on Card")).fill("dadadada");
        page.getByRole(AriaRole.CHECKBOX, new Page.GetByRoleOptions().setName("Remember me")).check();
        page.getByRole(AriaRole.BUTTON, new Page.GetByRoleOptions().setName("Purchase Flight")).click();
    }
}
