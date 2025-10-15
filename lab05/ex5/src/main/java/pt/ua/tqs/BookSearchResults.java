package pt.ua.tqs;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;

public class BookSearchResults {

    private final WebDriver driver;

    // Imagem do livro (Harry Potter and the Sorcerer)
    @FindBy(xpath = "//img[@role='img' and contains(@aria-label,'Harry Potter and the Sorcerer')]")
    private WebElement bookImage;

    public BookSearchResults(WebDriver driver) {
        this.driver = driver;
        PageFactory.initElements(driver, this);
    }

    public void selectBook() {
        bookImage.click();
    }
}
