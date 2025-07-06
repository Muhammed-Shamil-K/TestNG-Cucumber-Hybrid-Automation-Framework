package hybridAutomation.reporting;

import com.aventstack.extentreports.ExtentReports;
import com.aventstack.extentreports.ExtentTest;
import com.aventstack.extentreports.Status;
import com.aventstack.extentreports.reporter.ExtentSparkReporter;
import com.aventstack.extentreports.reporter.configuration.Theme;

import java.util.HashMap;
import java.util.Map;

public class ExtentReportManager {
    public static ExtentReports extentReports;
    static Map<Integer, ExtentTest> extentTestMap = new HashMap<>();

    public static ExtentReports getReporter() {
        String fileName = System.getProperty("user.dir")+"/target/Reports/TestNG/extent-report.html";
        if (extentReports == null) {
            ExtentSparkReporter extentSparkReporter = new ExtentSparkReporter(fileName);
            extentSparkReporter.config().setReportName("Automation Report");
            extentSparkReporter.config().setDocumentTitle("Hybrid Test Automation Framework Report");
            extentSparkReporter.config().setTheme(Theme.DARK);
            extentSparkReporter.config().setEncoding("utf-8");
            extentReports = new ExtentReports();
            extentReports.attachReporter(extentSparkReporter);
        }
        return extentReports;
    }

    public static ExtentTest getTest() {
        return extentTestMap.get((int) (Thread.currentThread().getId()));
    }

    public static synchronized  ExtentTest startTest(String testName, String desc) {
        ExtentTest extentTest = getReporter().createTest(testName, desc);
        extentTestMap.put((int) Thread.currentThread().getId(), extentTest);
        return extentTest;

    }

    public static  void log(String msg) {
        log(Status.INFO,msg);
    }

    public static  void log(Status status, String msg) {
        TestLog.log().debug(msg);
        if (ExtentReportManager.getTest() != null)
            ExtentReportManager.getTest().log(status, msg);
        else
            TestLog.log().warn("SKIPPED: Adding log to Extend Report:'{}' . Extend report is not created by the framework.",msg);
    }
}
