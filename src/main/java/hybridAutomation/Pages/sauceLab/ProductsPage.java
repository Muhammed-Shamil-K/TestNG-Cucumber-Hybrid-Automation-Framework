package hybridAutomation.Pages.sauceLab;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;

public class ProductsPage {

    private final WebDriver driver;

    public ProductsPage(WebDriver driver) {
        this.driver = driver;
        PageFactory.initElements(this.driver, this);
    }

    @FindBy(css = ".shopping_cart_link")
    private WebElement cartIcon;

    public boolean isCartIconPresent() {
        return cartIcon.isDisplayed();
    }
}
