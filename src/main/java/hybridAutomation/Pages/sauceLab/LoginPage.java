package hybridAutomation.Pages.sauceLab;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;
import org.openqa.selenium.support.ui.WebDriverWait;

public class LoginPage {

    private final WebDriver driver;
    public LoginPage(WebDriver driver) {
        this.driver = driver;
        PageFactory.initElements(driver, this);

    }

    @FindBy(css = "input#user-name")
    private WebElement userNameInput;

    @FindBy(css = "input#password")
    private WebElement passwordInput;

    @FindBy(css = "input#login-button")
    private WebElement loginButton;


    public void launchApplication() {
        driver.get("https://www.saucedemo.com/");
    }

    public void login(String username, String password) {
        userNameInput.sendKeys(username);
        passwordInput.sendKeys(password);
        loginButton.click();
    }
}



