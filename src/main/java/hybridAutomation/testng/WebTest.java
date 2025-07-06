package hybridAutomation.testng;

import hybridAutomation.reporting.TestLog;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.logging.LogType;
import org.openqa.selenium.logging.LoggingPreferences;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;

import java.util.logging.Level;

public class WebTest {

    private static ThreadLocal<WebDriver> driver = new ThreadLocal<>();
    private static WebTest instance;

    @BeforeMethod(alwaysRun = true)
    public void beforeMethod() {
        TestLog.log().info("Opening chrome browser");
        LoggingPreferences logPrefs = new LoggingPreferences();
        logPrefs.enable(LogType.PERFORMANCE, Level.ALL);
        ChromeOptions chromeOptions = new ChromeOptions();
        chromeOptions.addArguments("--start-maximized");
        chromeOptions.addArguments("--no-sandbox");
        chromeOptions.addArguments("--disable-dev-shm-usage");
        chromeOptions.addArguments("--remote-allow-origins=*");
        chromeOptions.setAcceptInsecureCerts(true);
        chromeOptions.setCapability("goog:loggingPrefs", logPrefs);
        chromeOptions.addArguments("--enable-logging=stderr --v=2");
        driver.set(new ChromeDriver(chromeOptions));

    }

    @AfterMethod(alwaysRun = true)
    public void afterMethod() {
        driver.get().quit();
    }

    public static WebTest getWebTest() {
        if (instance == null) {
            instance = new WebTest();
        }
        return instance;
    }

    public static WebDriver getWebDriver() {
        return getWebTest().driver.get();
    }
}
