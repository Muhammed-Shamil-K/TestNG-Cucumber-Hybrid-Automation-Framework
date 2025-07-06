package hybridAutomation.tests.cucumber.stepDefination;

import com.aventstack.extentreports.MediaEntityBuilder;
import com.aventstack.extentreports.Status;
import com.aventstack.extentreports.cucumber.adapter.ExtentCucumberAdapter;
import hybridAutomation.reporting.CucumberReport;
import hybridAutomation.reporting.TestLog;
import io.cucumber.java.After;
import io.cucumber.java.Before;
import io.cucumber.java.Scenario;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.logging.LogType;
import org.openqa.selenium.logging.LoggingPreferences;
import org.testng.annotations.AfterSuite;
import org.testng.annotations.BeforeSuite;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.URI;
import java.nio.file.Paths;
import java.rmi.RemoteException;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.logging.Level;

public class Hooks {



    private static ThreadLocal<WebDriver> driver = new ThreadLocal<>();

    private static Hooks instance;

    public List<Scenario> scenarioList=new ArrayList<Scenario>();

    public String scenarioName;

    public String featureFileName;

    public static Hooks getHooks() {
        if (instance == null) {
            instance = new Hooks();
        }
        return instance;
    }

    public static WebDriver getWebDriver() {
        return getHooks().driver.get();
    }



    @Before
    public void scenarioCheck(Scenario scenario) {
        String ScenarioName  = scenario.getName();
        scenarioName = ScenarioName;
        URI uri = scenario.getUri();
        if (uri != null) {
            String path = Paths.get(uri).toString();
            featureFileName = path.substring(path.lastIndexOf("/") + 1);
        }
        TestLog.log().info("Automating the scenario ==>  {} in feature : {}", ScenarioName, featureFileName);
        initializeDriverBasedOnPlatform(scenario);
    }

    public void initializeDriverBasedOnPlatform(Scenario scenario){
        Collection<String> tagSet = scenario.getSourceTagNames();
        if (!tagSet.isEmpty()) {
            TestLog.log().info("Scenario is tagged with : {}", tagSet);
        }
        if (tagSet.contains("@web") && driver.get() == null) {
            driver.set(createDriver());
        }
    }

    public WebDriver createDriver() {
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
        return new ChromeDriver(chromeOptions);


    }

    @After()
    public void screenShotForFailed(Scenario scenario) throws IOException {
        //adding scenario in to a list for report processing
        scenarioList.add(scenario);
        checkScenarioStatus(scenario);
        if (driver.get() != null) {
            driver.get().quit();
        }
    }




    private void checkScenarioStatus(Scenario scenario) throws IOException {
        String ScenarioName = scenario.getName();
        if (scenario.getStatus().equals(io.cucumber.java.Status.PASSED)) {
            TestLog.log().info("Successfully automated the scenario ==>  {}", ScenarioName);
        }
        if (scenario.isFailed()) {
            TestLog.log().error("<<<<< Inside the scenario failed method >>>>>");
            TestLog.log().error("Scenario name is {}", ScenarioName);
            if (scenario.getSourceTagNames().contains("@web")) {
                ExtentCucumberAdapter.getCurrentStep().log(Status.FAIL, MediaEntityBuilder.createScreenCaptureFromBase64String(getScreenShot(driver.get(),ScenarioName)).build());
            }else {
                ExtentCucumberAdapter.getCurrentStep().log(Status.FAIL, "Fail");
            }
        }
    }

    public String getScreenShot(WebDriver driver, String screenshotName) throws IOException {
        try {

            String dateName = new SimpleDateFormat("yyyyMMddhhmmss").format(new Date());
            TakesScreenshot ts = (TakesScreenshot) driver;


            //using base64 decoding and encoding for screeen shot as it is getting failed in mobile
            String b64 = ts.getScreenshotAs(OutputType.BASE64).replaceAll("\n","");

            //make a dir if not exist
            String dir = System.getProperty("user.dir") + "/target/Screenshots/";
            File storageDirectory = new File(dir);
            if(!(storageDirectory.exists())){
                storageDirectory.mkdir();
            }


            String destination = dir + screenshotName + dateName + ".png";
            File file = new File(destination);

            try (FileOutputStream fos = new FileOutputStream(file); ) {
                byte[] decoder = Base64.getDecoder().decode(b64);
                fos.write(decoder);

            } catch (Exception e) {
                e.printStackTrace();
            }
            return b64;
        } catch (Exception e) {
            throw new RemoteException("Screenshot is not added"+e);
        }
    }


}
