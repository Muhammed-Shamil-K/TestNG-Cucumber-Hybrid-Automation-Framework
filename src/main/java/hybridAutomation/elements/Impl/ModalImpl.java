package hybridAutomation.elements.Impl;

import hybridAutomation.Utilities.UIUtil;
import hybridAutomation.elements.Modal;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.remote.RemoteWebElement;

import java.util.NoSuchElementException;
import java.util.Optional;

public class ModalImpl implements Modal {

    private WebElement element;
    private WebDriver driver;

    public ModalImpl(WebElement element) {
        this.driver = ((RemoteWebElement) element).getWrappedDriver();
        this.element = element;
    }
    @Override
    public String getTitle() {
        waitUntilModalDisplayed();
        return element.findElement(By.cssSelector("div.header")).getText();
    }

    @Override
    public String getMessage() {
        waitUntilModalDisplayed();
        return element.findElement(By.cssSelector("p")).getText();
    }
    @Override
    public void clickButton(String buttonText) {
        waitUntilModalDisplayed();
        Optional<WebElement> matchedButton = element.findElements(By.cssSelector("button")).stream()
                .filter(item-> item.getText().equals(buttonText)).findFirst();
        if (matchedButton.isPresent()) {
            matchedButton.get().click();
        } else {
            throw new NoSuchElementException("Button with text " + buttonText + " is not present in modal ..");
        }
    }

    private void waitUntilModalDisplayed() {
        UIUtil.waitUntilDisplayed(element, 10);
    }
}
