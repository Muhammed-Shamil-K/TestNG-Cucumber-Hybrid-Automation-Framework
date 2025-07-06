package hybridAutomation.elements.Impl;

import hybridAutomation.Utilities.UIUtil;
import hybridAutomation.elements.TestElement;
import org.openqa.selenium.*;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.interactions.Coordinates;
import org.openqa.selenium.interactions.Locatable;
import org.openqa.selenium.remote.RemoteWebElement;

import java.util.List;

public class TestElementImpl implements TestElement {

    private final WebElement element;
    private final WebDriver driver;
    private final JavascriptExecutor executor;
    private final Actions actions;

    public TestElementImpl(WebElement element) {
        this.element = element;
        this.driver = ((RemoteWebElement) element).getWrappedDriver();
        executor = (JavascriptExecutor) driver;
        actions = new Actions(driver);
    }

    @Override
    public void click() {
        UIUtil.locateElement(driver, element);
        UIUtil.waitUntilElementIsClickable(driver, element, 5);
        element.click();
    }

    @Override
    public boolean click2() {
        UIUtil.locateElement(driver, element);
        UIUtil.waitUntilElementIsClickable(driver, element, 5);
        actions.moveToElement(element).moveByOffset(0, -1).click().build().perform();
        return true;
    }

    @Override
    public boolean click3() {
        UIUtil.locateElement(driver, element);
        UIUtil.waitUntilElementIsClickable(driver, element, 5);
        executor.executeScript("arguments[0].click()", element);
        return true;
    }

    @Override
    public void submit() {
        element.submit();
    }

    @Override
    public void sendKeys(CharSequence... keysToSend) {
        UIUtil.locateElement(driver, element);
        element.clear();
        element.sendKeys(keysToSend);
    }

    @Override
    public void clear() {
        element.clear();
    }

    @Override
    public String getTagName() {
        return element.getTagName();
    }

    @Override
    public String getAttribute(String name) {
        return element.getAttribute(name);
    }

    @Override
    public boolean isSelected() {
        return element.isSelected();
    }

    //we may need to check disabled attribute also here (correction may  be required)
    @Override
    public boolean isEnabled() {
        return element.isEnabled();
    }

    @Override
    public String getText() {
        return element.getText();
    }

    @Override
    public List<WebElement> findElements(By by) {
        return element.findElements(by);
    }

    @Override
    public WebElement findElement(By by) {
        return element.findElement(by);
    }

    // added DOM visibility check here (may not be required amd can be removed later if not needed)
    @Override
    public boolean isDisplayed() {
        executor.executeScript("arguments[0].scrollIntoView()", element);
        boolean flag = Integer.parseInt(element.getAttribute("offsetHeight")) > 0 &&
                Integer.parseInt(element.getAttribute("offsetWidth")) >0;
        //add any logging
        System.out.println("Element " + element.toString() + " displayed ? -----> " + flag);
        return flag;
    }

    @Override
    public Point getLocation() {
        return element.getLocation();
    }

    @Override
    public Dimension getSize() {
        return element.getSize();
    }

    @Override
    public Rectangle getRect() {
        return element.getRect();
    }

    @Override
    public String getCssValue(String propertyName) {
        return element.getCssValue(propertyName);
    }

    @Override
    public <X> X getScreenshotAs(OutputType<X> target) throws WebDriverException {
        return element.getScreenshotAs(target);
    }

    @Override
    public WebElement getWrappedElement() {
        return element;
    }

    @Override
    public Coordinates getCoordinates() {
        return ((Locatable) element).getCoordinates();
    }
}
