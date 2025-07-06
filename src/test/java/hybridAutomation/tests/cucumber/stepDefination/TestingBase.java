package hybridAutomation.tests.cucumber.stepDefination;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.logging.LogType;
import org.openqa.selenium.logging.LoggingPreferences;
import org.openqa.selenium.remote.DesiredCapabilities;

import java.util.logging.Level;

public class TestingBase {

    private static TestingBase instance;
    private static WebDriver driver;
    private static Thread CLOSE_DRIVER = new Thread() {
        @Override
        public void run() {
            driver.close();
        }

    };

    static {
        Runtime.getRuntime().addShutdownHook(CLOSE_DRIVER);
    }

    private TestingBase() {
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
        driver = new ChromeDriver(chromeOptions);
    }

    public static TestingBase getTestingBase() {
        if (instance == null) {
            instance = new TestingBase();
        }
        return instance;
    }

    public static WebDriver getDriver() {
        return getTestingBase().driver;
    }
}
