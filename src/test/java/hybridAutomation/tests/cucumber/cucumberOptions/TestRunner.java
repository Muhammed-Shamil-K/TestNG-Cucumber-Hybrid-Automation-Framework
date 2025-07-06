package hybridAutomation.tests.cucumber.cucumberOptions;


import hybridAutomation.reporting.CucumberReport;
import hybridAutomation.reporting.TestLog;
import io.cucumber.testng.AbstractTestNGCucumberTests;
import io.cucumber.testng.CucumberOptions;
import org.testng.annotations.*;


@CucumberOptions(monochrome = true,
        features = "src/test/java/hybridAutomation/tests/cucumber/features",
        glue = "hybridAutomation/tests/cucumber/stepDefination",
        plugin = {"com.aventstack.extentreports.cucumber.adapter.ExtentCucumberAdapter:"})
public class TestRunner extends AbstractTestNGCucumberTests {



//    @Override
//    @DataProvider(parallel = true)
//    public Object[][] scenarios() {
//        return super.scenarios();
//    }


    @BeforeTest
    public void testInitialisation() {
        TestLog.log().info("Starting Cucumber test suite");
        CucumberReport.setUp();
    }

    @AfterTest
    public void tearDown() {
        TestLog.log().info("Finished Cucumber test suite");
        CucumberReport.writeReport();
    }
}
