package hybridAutomation.Utilities;

import org.openqa.selenium.*;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.Assert;

import java.time.Duration;
import java.util.HashMap;
import java.util.Map;

public class UIUtil {

    public static void sleep(long timeout) {
        try {
            Thread.sleep(timeout);
        } catch (InterruptedException e) {
            e.getSuppressed();
        }
    }

    public static void refresh(WebDriver driver) {
        driver.navigate().refresh();
    }

    public static String getActiveText(WebDriver driver) {
        return driver.switchTo().activeElement().getText();
    }

    public static void clickKeyOnActiveElement(WebDriver driver, Keys... keys) {
        driver.switchTo().activeElement().sendKeys(Keys.chord(keys));
    }

    public static boolean switchToWindow(WebDriver driver, String title) {
        Map<String, String> winMap = new HashMap<>();
        return RetryUtil.retry(()-> {
            for(String winHandle: driver.getWindowHandles()) {
                driver.switchTo().window(winHandle);
                String winTitle = driver.getTitle();
                winMap.put(winTitle, winHandle);
            }
            Assert.assertTrue(winMap.containsKey(title), "Window with title "+ title + "doesn't exist!!!...");
            driver.switchTo().window(winMap.get(title));
            try {
                driver.manage().window().maximize();
            } catch (WebDriverException e) {
                e.getStackTrace();
            }
            return true;
        }, 10);
    }

    public static void waitUntilDisplayed(WebElement element, int seconds) {
        RetryUtil.retry(()-> {
            Assert.assertTrue(element.isDisplayed(), "The WebElement " + element.toString() +
                    "is not not displayed!!...");
            return element;
        },seconds);
    }

    public static void waitUntilVisibilityOf(WebDriver driver, WebElement element) {
        WebDriverWait w = new WebDriverWait(driver, Duration.ofSeconds(5));
        try {
            w.until(ExpectedConditions.visibilityOf(element));
        } catch (TimeoutException e) {
            //log something
            throw e;
        }
    }

    public static void waitUntilInvisibilityOf(WebDriver driver, WebElement element, int seconds) {
        WebDriverWait w = new WebDriverWait(driver, Duration.ofSeconds(seconds));
        try {
            w.until(ExpectedConditions.invisibilityOf(element));
        } catch (TimeoutException e) {
            //log something
            throw e;
        }
    }

    public static void waitForAttributeToBe(WebDriver driver, WebElement element,String attribute, String value,
                                            int seconds) {
        WebDriverWait w = new WebDriverWait(driver, Duration.ofSeconds(seconds));
        try {
            w.until(ExpectedConditions.attributeToBe(element, attribute, value));
        } catch (TimeoutException e) {
            //log something
            throw e;
        }
    }

    public static void waitForEmptyAttribute(WebDriver driver, WebElement element,String attribute, int seconds) {
        WebDriverWait w = new WebDriverWait(driver, Duration.ofSeconds(seconds));
        try {
            w.until(ExpectedConditions.not(ExpectedConditions.attributeToBeNotEmpty(element, attribute)));
        } catch (TimeoutException e) {
            //log something
            throw e;
        }
    }

    public static void waitUntilElementIsClickable(WebDriver driver, WebElement element, int seconds) {
        WebDriverWait w = new WebDriverWait(driver, Duration.ofSeconds(seconds));
        try {
            w.until(ExpectedConditions.elementToBeClickable(element));
        } catch (TimeoutException e) {
            //log something
            throw e;
        }
    }

    public static void waitUntilTextToBePresentInElement(WebDriver driver, WebElement element, String text, int seconds) {
        WebDriverWait w = new WebDriverWait(driver, Duration.ofSeconds(seconds));
        try {
            w.until(ExpectedConditions.textToBePresentInElement(element, text));
        } catch (TimeoutException e) {
            //log something
            throw e;
        }
    }

    public static void clearInput(WebDriver driver, WebElement element) {
        element.click();
        element.sendKeys(Keys.chord(Keys.CONTROL, "A"), Keys.chord(Keys.BACK_SPACE));
    }

    public static void rightClick(WebDriver driver, WebElement element) {
        scrollByElement(driver, element);
        actionClass(driver).contextClick(element).build().perform();
    }

    private static Actions actionClass(WebDriver driver) {
        Actions actions = new Actions(driver);
        return actions;
    }

    public static void dragAndDropMethod(WebDriver driver, WebElement source, WebElement target) {
        waitUntilDisplayed(source, 10);
        scrollByElement(driver, source);
        Actions actions = actionClass(driver);
        actions.clickAndHold(source).build().perform();
        actions.moveToElement(target).build().perform();
        actions.moveByOffset(-1, -1).build().perform();
        actions.release().build().perform();
    }

    public static void dragAndDrop(WebDriver driver, WebElement source, WebElement target) {
        String script = "function createEvent(typeOfEvent) {\n" +"var event =document.createEvent(\"CustomEvent\");\n" +
                "event.initCustomEvent(typeOfEvent,true, true, null);\n" +"event.dataTransfer = {\n" +"data: {},\n" +
                "setData: function (key, value) {\n" +"this.data[key] = value;\n" +"},\n" +"getData: function (key) " +
                "{\nreturn this.data[key];\n" +"}\n" +"};\n" +"return event;\n" +"}\n" +"\nfunction dispatchEvent" +
                "(element, event,transferData) {\n" +"if (transferData !== undefined) {\nevent.dataTransfer = " +
                "transferData;\n" +"}\n" +"if (element.dispatchEvent) {\n" + "element.dispatchEvent(event);\n" +
                "} else if (element.fireEvent) {\n" + "element.fireEvent(\"on\" + event.type, event);\n" +"}\n" +"}\n" +
                "\n" + "function simulateHTML5DragAndDrop(element, destination) {\n" +"var dragStartEvent =createEvent"+
                "('dragstart');\n" +"dispatchEvent(element, dragStartEvent);\n"+"var dropEvent = createEvent('drop');\n"
                +"dispatchEvent(destination, dropEvent,dragStartEvent.dataTransfer);\n" +"var dragEndEvent = " +
                "createEvent('dragend');\n" +"dispatchEvent(element, dragEndEvent,dropEvent.dataTransfer);\n" +"}\n" +
                "\n" +"var source = arguments[0];\n" +"var destination = arguments[1];\n" +"simulateHTML5DragAndDrop" +
                "(source,destination);";
        ((JavascriptExecutor)driver).executeScript(script , source, target);
        sleep(2000);
    }

    //closes current window
    public static void closeWindow(WebDriver driver) {
        JavascriptExecutor executor = (JavascriptExecutor) driver;
        executor.executeScript("window.close()");
    }

    public static void scrollByElement(WebDriver driver, WebElement element) {
        JavascriptExecutor executor = (JavascriptExecutor) driver;
        executor.executeScript("arguments[0].scrollIntoView();", element);
    }

    public static void locateElement(WebDriver driver, WebElement element) {
        ((JavascriptExecutor) driver).executeScript("arguments[0].scrollIntoView();", element);
        waitUntilElementIsClickable(driver, element, 5);
    }
}
