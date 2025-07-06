package hybridAutomation.tests.testng.UI;

import hybridAutomation.Core.ByDomExpansion;
import hybridAutomation.Core.Tag;
import hybridAutomation.Core.TestProperties;
import hybridAutomation.Pages.sauceLab.LoginPage;
import hybridAutomation.Pages.sauceLab.ProductsPage;
import hybridAutomation.Utilities.RetryUtil;
import hybridAutomation.Utilities.UIUtil;
import hybridAutomation.elements.Impl.DatePickerImpl;
import hybridAutomation.elements.Impl.TestElementImpl;
import hybridAutomation.testng.WebTest;
import hybridAutomation.tests.cucumber.stepDefination.Hooks;
import org.openqa.selenium.By;
import org.openqa.selenium.Dimension;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.ui.Select;
import org.testng.Assert;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import java.util.NoSuchElementException;
import java.util.Optional;

import static hybridAutomation.Utilities.UIUtil.*;


public class Demo extends WebTest{

    @Test
    @TestProperties(id = "TC_3")
    public void demoTest()  {
        WebDriver driver = getWebDriver();
        driver.get("https://www.amazon.com/");
        driver.manage().window().maximize();
        waitUntilVisibilityOf(driver, driver.findElement(By.cssSelector("div.glow-toaster-footer input")));
        driver.findElement(By.cssSelector("div.glow-toaster-footer input")).click();
        waitUntilVisibilityOf(driver, driver.findElement(By.cssSelector("input#twotabsearchtextbox")));
        driver.findElement(By.cssSelector("input#twotabsearchtextbox")).sendKeys("Laptop");
        waitUntilVisibilityOf(driver, driver.findElement(By.cssSelector("input#nav-search-submit-button")));
        driver.findElement(By.cssSelector("input#nav-search-submit-button")).click();
        waitUntilVisibilityOf(driver, driver.findElement(By.cssSelector("div.s-title-instructions-style")));
        Optional<WebElement> laptop = driver.findElements(By.cssSelector("div.s-title-instructions-style")).stream()
                        .filter(element -> element.getText().contains("2022 HP 15.6 FHD Laptop Computer")).findFirst();
        if(laptop.isPresent()) {
            scrollByElement(driver, laptop.get());
            new TestElementImpl(laptop.get().findElement(By.cssSelector("a"))).click();
        } else
            System.out.println("There is no specified Item on Search results !!!");
        UIUtil.sleep(2000);
    }



    @Test
    public void demoDragAndDrop()  {
        WebDriver driver = getWebDriver();
        driver.get("https://demo.automationtesting.in/Dynamic.html");
        driver.manage().window().maximize();
        dragAndDrop(driver, driver.findElement(By.id("angular")), driver.findElement(By.id("droparea")));
        dragAndDrop(driver, driver.findElement(By.id("mongo")), driver.findElement(By.id("droparea")));
        dragAndDrop(driver, driver.findElement(By.id("node")), driver.findElement(By.id("droparea")));
    }

    @Test
    public void demoShadowRoot()  {
        WebDriver driver = getWebDriver();
        driver.get("https://www.htmlelements.com/demos/menu/shadow-dom/index.htm");
        driver.manage().window().maximize();
        ByDomExpansion byDomExpansion = new ByDomExpansion(driver);
        WebElement element = byDomExpansion.findElement(By.cssSelector("smart-menu-items-group"), By.cssSelector("smart-ui-menu"));
        element.click();
        UIUtil.sleep(2000);
    }

    /*
    This test is a Sikuli test
    @Test
    public void demoFileUpload() throws FindFailed {
        driver.get("https://demo.automationtesting.in/FileUpload.html");
        driver.manage().window().maximize();
        Screen screen = new Screen();
        Pattern openButton = new Pattern("/home/qburst/Documents/Training/Frameworks/test-automation/src/main/resources/Images/FileUpload.png");
        screen.click(openButton);
        UIUtil.sleep(3000);

    }

     */

    @Test
    public void demoCanvas(){
        WebDriver driver = getWebDriver();
        driver.get("https://kitchen.applitools.com/ingredients/canvasa");
        driver.manage().window().maximize();
        WebElement canvas = driver.findElement(By.id("burger_canvas"));

        //Calculate position canvas button
        Dimension canvas_dimensions = canvas.getSize();
        int canvas_center_x = canvas_dimensions.getWidth() / 2;
        int canvas_center_y = canvas_dimensions.getHeight() / 2;
        int button_x = (canvas_center_x / 3) * 2;
        int button_y = (canvas_center_y / 3) * 2;

        //Click button on the canvas
        new Actions(driver)
                .moveToElement(canvas, button_x, button_y)
                .perform();

        UIUtil.sleep(5000);

    }



    @Test(enabled = false)
    public void test() {
        WebDriver driver = getWebDriver();
        driver.get("https://qa.anywhereauctions.com.au/login-app/login?user=agent");
        driver.manage().window().maximize();
        driver.findElement(By.xpath("//input[@type='email']")).sendKeys("agencyqa@mailinator.com");
        driver.findElement(By.xpath("//input[@type='password']")).sendKeys("Test@123");
        driver.findElement(By.xpath("//button[@type='submit']")).click();
        RetryUtil.retry(() -> driver.findElement(By.cssSelector("navigation-menu")).isDisplayed(), 15);
        driver.findElement(By.xpath("//div[text()='Create Property Listing']")).click();
        UIUtil.waitUntilInvisibilityOf(driver, driver.findElement(By.cssSelector("div.loader-container")), 30);

        driver.findElement(By.cssSelector("input[formcontrolname='street_number']")).sendKeys("Auto1");
        //date selection
        new DatePickerImpl(driver.findElement(By.cssSelector("input[formcontrolname='date']"))).select(5);
        //time selection
        new Select(driver.findElement(By.cssSelector("select[formcontrolname= 'hour']"))).selectByVisibleText("07");
        new Select(driver.findElement(By.cssSelector("select[formcontrolname= 'minute']"))).selectByVisibleText("30");
        new Select(driver.findElement(By.cssSelector("select[formcontrolname= 'period']"))).selectByVisibleText("pm");

        driver.findElement(By.cssSelector("input[formcontrolname='street_name']")).sendKeys("Auto11");

        new Select(driver.findElement(By.cssSelector("select[formcontrolname= 'primary_agent_id']"))).selectByVisibleText("agent one");

        new Select(driver.findElement(By.cssSelector("select[formcontrolname= 'state_id']"))).selectByVisibleText("Victoria");

        driver.findElement(By.cssSelector("input[placeholder='Enter Suburb']")).sendKeys("Sugarloaf");
        driver.findElement(By.cssSelector("md-option")).click();

        driver.findElement(By.cssSelector("input[formcontrolname='listing_price']")).sendKeys("12000");

        //document upload
        WebElement legalDocuments = driver.findElements(By.cssSelector("div.mdl-cell.mdl-cell--6-col.mdl-cell--8-col-tablet")).stream()
                .filter(element -> element.getText().contains("UPLOAD CONTRACT OF SALE FOR E-SIGNING")).findFirst().get();
        WebElement browse = legalDocuments.findElement(By.cssSelector("input#contract-document"));
        browse.sendKeys("/home/qburst/Downloads/Contract document.pdf");
        UIUtil.waitUntilInvisibilityOf(driver, driver.findElement(By.cssSelector("div.loader-container")), 30);
        RetryUtil.retry(()-> driver.findElement(By.cssSelector("div.doc-thumb img")).isDisplayed(), 15);

        //document tagging
        WebElement documentTag = driver.findElement(By.cssSelector("button.tag-btn"));
        UIUtil.scrollByElement(driver, documentTag);
        documentTag.click();
        driver.findElement(By.cssSelector("common-popup button.confirm")).click();


        RetryUtil.retry(() -> driver.findElement(By.cssSelector("canvas.upper-canvas")).isDisplayed(), 15);
        driver.findElement(By.cssSelector("div.tag-group")).click();
        addTag(driver, Tag.SIGNE_NAME);
        addTag(driver, Tag.SIGN);

        //driver.findElements(By.cssSelector("div.property-btn button")).get(2).click();

        UIUtil.sleep(5000);
    }

    private void addTag(WebDriver driver, Tag tagItem) {
        Optional<WebElement> item = driver.findElements(By.cssSelector("div.tagging-div")).stream()
                .filter(tag-> tag.getText().equals(tagItem.tagName)).findFirst();
        if (item.isPresent()) {
            item.get().click();
        } else {
            //logger
            throw new NoSuchElementException("Tag item " + tagItem.tagName + " is not present on the menu");
        }
        WebElement canvas = driver.findElement(By.cssSelector("canvas.upper-canvas"));
        new Actions(driver).moveToElement(canvas, tagItem.xOffset, tagItem.yOffset).click().perform();
    }

    @Test
    public void sauceLabLogin() {
        LoginPage loginPage = new LoginPage(getWebDriver());
        loginPage.launchApplication();
        loginPage.login("standard_user", "secret_sauce");
        ProductsPage productsPage = new ProductsPage(getWebDriver());
        Assert.assertTrue(productsPage.isCartIconPresent(), "Cart icon is not displayed on Products page");
    }

}
