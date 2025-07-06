package hybridAutomation.Pages.anywhereAction;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;

public class EditProperty extends Property {
    private WebDriver driver;
    public EditProperty(WebDriver driver) {
        super(driver);
    }

    @FindBy(css = "button.delete-btn")
    private WebElement deleteProperty;

    public void deleteProperty() {
        deleteProperty.click();
        confirmModalDialog();
        acceptModalDialog();
    }
}
