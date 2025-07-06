package hybridAutomation.Pages.anywhereAction;

import hybridAutomation.Core.TestPageFactory;
import hybridAutomation.elements.TestElement;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.FindBy;


public class Login {

    private final WebDriver driver;

    @FindBy(xpath = "//span[text()='AGENT']")
    private TestElement agentRole;

    @FindBy(xpath = "//input[@type='email']")
    private TestElement email;

    @FindBy(xpath = "//input[@type='password']")
    private TestElement password;

    @FindBy(xpath = "//button[@type='submit']")
    private TestElement submitButton;


    public Login(WebDriver driver) {
        this.driver = driver;
        driver.get("https://qa.anywhereauctions.com.au/login-app/login");
        driver.manage().window().maximize();
        TestPageFactory.initElements(driver, this);
    }

    public void login(String role) {
        if(role.equals("Agent")) {
            agentRole.click2();
        }
        fillCredentialsAndSubmit(role);
    }

    private void fillCredentialsAndSubmit(String role) {
        if (role.equals("Agent")) {
            email.sendKeys("agencyqa@mailinator.com");
            password.sendKeys("Test@123");
        } else {
            // complete the credentials
        }
        submitButton.click2();
    }


}
