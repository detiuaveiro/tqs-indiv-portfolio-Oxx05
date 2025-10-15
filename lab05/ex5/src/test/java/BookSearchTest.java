import com.microsoft.playwright.junit.UsePlaywright;
import com.microsoft.playwright.Page;
import com.microsoft.playwright.options.*;

import org.junit.jupiter.api.*;
import static com.microsoft.playwright.assertions.PlaywrightAssertions.*;

@UsePlaywright
public class BookSearchTest {
    @Test
    void test(Page page) {
        page.navigate("https://cover-bookstore.onrender.com/");
        page.getByRole(AriaRole.TEXTBOX, new Page.GetByRoleOptions().setName("Search for books, authors")).waitFor();        page.getByRole(AriaRole.TEXTBOX, new Page.GetByRoleOptions().setName("Search for books, authors,")).click();
        page.getByRole(AriaRole.TEXTBOX, new Page.GetByRoleOptions().setName("Search for books, authors,")).fill("Harry Potter");
        page.getByRole(AriaRole.BUTTON, new Page.GetByRoleOptions().setName("Search")).click();
        page.getByRole(AriaRole.IMG, new Page.GetByRoleOptions().setName("Harry Potter and the Sorcerer")).click();
    }
}