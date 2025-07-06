package hybridAutomation.tests.cucumber.stepDefination;

import hybridAutomation.Pages.sauceLab.LoginPage;
import hybridAutomation.Pages.sauceLab.ProductsPage;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import hybridAutomation.reporting.TestLog;

import org.testng.Assert;
import org.testng.annotations.Test;


public class StepDefination {

    LoginPage loginPage;
    ProductsPage productsPage;


    @Given("User is on Login page")
    public void user_is_on_login_page() {
        this.loginPage = new LoginPage(Hooks.getWebDriver());
        loginPage.launchApplication();
        TestLog.log().info("SauceLab application is launched on Browser");
    }

    @When("User fills the form with username and password and then click on Submit button")
    public void user_fills_the_form_with_username_and_password_and_then_click_on_Submit_button() {
        this.loginPage.login("standard_user", "secret_sauce");
        TestLog.log().info("User logged on to the application");
    }

    @Then("User navigated to Home page")
    public void user_navigated_to_home_page() {
        this.productsPage = new ProductsPage(Hooks.getWebDriver());
        Assert.assertTrue(productsPage.isCartIconPresent(), "Cart icon is not displayed on Products page");
        TestLog.log().info("Cart Icon is validated in the products page");
    }
}
