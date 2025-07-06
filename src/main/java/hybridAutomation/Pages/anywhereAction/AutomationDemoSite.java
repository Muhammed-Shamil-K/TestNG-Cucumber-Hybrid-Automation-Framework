package hybridAutomation.Pages.anywhereAction;

import hybridAutomation.Core.Scan;
import hybridAutomation.Core.TestImage;
import hybridAutomation.Core.TestPageFactory;
import hybridAutomation.elements.TestElement;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.FindBy;

public class AutomationDemoSite {
    private final WebDriver driver;

    public AutomationDemoSite(WebDriver driver) {
        this.driver = driver;
        TestPageFactory.initElements(driver, this);
    }

    @FindBy(css = "ul.navbar-nav li")
    private TestElement homeTab;

    @Scan(image = "/home/qburst/Documents/Training/Frameworks/test-automation/src/main/resources/Images/FileUpload.png")
    private TestImage image;

    public void openHomePage() {
        homeTab.click();
    }

    public void clickImage() {
        image.clickImage();
    }
}
