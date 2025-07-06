package hybridAutomation.Core;

import com.aventstack.extentreports.MediaEntityBuilder;
import com.aventstack.extentreports.markuputils.ExtentColor;
import com.aventstack.extentreports.markuputils.MarkupHelper;
import hybridAutomation.testng.WebTest;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.WebDriver;
import org.testng.*;
import org.testng.annotations.ITestAnnotation;
import hybridAutomation.reporting.ExtentReportManager;
import hybridAutomation.reporting.TestLog;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.lang.reflect.Constructor;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

public class TestListener implements IAnnotationTransformer, ITestListener, ISuiteListener {

    private final List<String> testCaseIds;

    public TestListener() {
        String testCaseIdProperty = System.getProperty("testCaseId");
        if (testCaseIdProperty != null && !testCaseIdProperty.isEmpty()) {
            TestLog.log().info("TestCaseIdInterceptor initialized with testCaseIds: {}", testCaseIdProperty);
            this.testCaseIds = Arrays.asList(testCaseIdProperty.split(","));
        } else {
            this.testCaseIds = null;
        }
    }

    @Override
    public void transform(ITestAnnotation annotation, Class testClass, Constructor testConstructor, Method testMethod) {
        if (testMethod != null && testCaseIds != null) {
            TestProperties tcIdAnnotation = testMethod.getAnnotation(TestProperties.class);
            String[] testCaseId = (tcIdAnnotation != null) ? tcIdAnnotation.id() : null;
            if (testCaseId == null || !containsAny(testCaseIds, testCaseId)) {
                annotation.setEnabled(false);
            }else {
                TestLog.log().info("Found test method: {} with TestCaseId: {}", testMethod.getName(), testCaseId);
            }
        }
    }

    private boolean containsAny(List<String> list, String[] array) {
        for (String element : array) {
            if (list.contains(element)) {
                return true;
            }
        }
        return false;
    }

    @Override
    public void onStart(ISuite suite) {
        TestLog.log().info("Starting execution of Suite: {}", suite.getName());
    }

    private Optional<WebDriver> getDriver(ITestResult result) {
        if (result.getInstance() instanceof WebTest) {
            return Optional.of(WebTest.getWebDriver());
        } else {
            return Optional.empty();
        }
    }

    @Override
    public void onTestFailure(ITestResult result) {
        if (!result.getName().equals("runScenario")) {
            Optional<WebDriver> driver = getDriver(result);
            if (driver.isPresent()) {
                String encoded = getScreenshotAsBase64String(driver.get());
                ExtentReportManager.getTest().fail("Test Failed", MediaEntityBuilder.createScreenCaptureFromBase64String(encoded).build());
            }else {
                ExtentReportManager.getTest().fail(MarkupHelper.createLabel("Test Failed", ExtentColor.RED));
            }
            TestLog.log().error("End of Test: {} : Failed", result.getName());
            if (result.getThrowable() != null) {
                StringWriter sw = new StringWriter();
                PrintWriter pw = new PrintWriter(sw);
                result.getThrowable().printStackTrace(pw);
                ExtentReportManager.getTest().fail(result.getThrowable());
                TestLog.log().error(result.getThrowable());
            }
        }
    }

    /**
     * Get screenshot as Base64 String
     *
     * @param driver: Web Driver Object
     * @return Screenshot String - BASE64
     */
    private static String getScreenshotAsBase64String(WebDriver driver) {
        return (String) ((TakesScreenshot) driver).getScreenshotAs(OutputType.BASE64);
    }

    @Override
    public void onFinish(ISuite suite) {
        TestLog.log().info("End of Suite: {}", suite.getName());
    }

    @Override
    public void onTestStart(ITestResult result) {
        if (!result.getName().equals("runScenario")) {
            TestLog.log().info("Starting execution of Test: {}", result.getName());
            ExtentReportManager.startTest(result.getName(), result.getMethod().getDescription())
                    .assignCategory(result.getTestContext().getName());
        }

    }

    @Override
    public void onTestSuccess(ITestResult result) {
        if (!result.getName().equals("runScenario")) {
            ExtentReportManager.getTest().pass(MarkupHelper.createLabel("Test Passed", ExtentColor.GREEN));
            TestLog.log().info("End of Test: {} : Passed", result.getName());
        }

    }

    @Override
    public void onTestSkipped(ITestResult result) {
        if (!result.getName().equals("runScenario")) {
            ExtentReportManager.getTest().skip(MarkupHelper.createLabel("Test Skipped", ExtentColor.YELLOW));
            TestLog.log().error("End of Test: {} : Skipped", result.getName());
        }
    }

    @Override
    public void onTestFailedButWithinSuccessPercentage(ITestResult result) {
        if (!result.getName().equals("runScenario")) {
            TestLog.log().error("End of Test: {} : Passed Conditionally", result.getName());
            this.onTestFailure(result);
        }
    }

    @Override
    public void onTestFailedWithTimeout(ITestResult result) {
        if (!result.getName().equals("runScenario")) {
            TestLog.log().error("End of Test: {} : Failed due to Timeout", result.getName());
            this.onTestFailure(result);
        }
    }

    @Override
    public void onStart(ITestContext context) {
        TestLog.log().info("Starting execution of Test Set: {}", context.getName());
    }

    @Override
    public void onFinish(ITestContext context) {
        TestLog.log().info("End of Test Set: {}", context.getName());
        ExtentReportManager.getReporter().flush();
    }
}
