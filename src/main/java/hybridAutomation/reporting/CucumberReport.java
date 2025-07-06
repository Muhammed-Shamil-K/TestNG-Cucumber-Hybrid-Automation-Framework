package hybridAutomation.reporting;

import com.aventstack.extentreports.service.ExtentService;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

public class CucumberReport {

    public static void setUp() {
        TestLog.log().info("project report configuration started");
        DateFormat dateFormat = new SimpleDateFormat("dd-MMM-yyyy_HH-mm-ss");
        Date date = new Date();
        String filePathdate = dateFormat.format(date).toString();

        // spark report
        System.setProperty("extent.reporter.spark.start", "true");
        System.setProperty("extent.reporter.spark.out", "target/Reports/cucumber/" + filePathdate + "_AutomationReport.html");
        System.setProperty("extent.reporter.spark.config", "src/main/resources/spark-config.xml");

        // html report
        System.setProperty("extent.reporter.html.start", "true");
        System.setProperty("extent.reporter.html.config", "src/main/resources/html-config.xml");
        System.setProperty("extent.reporter.html.out", "target/Reports/cucumber/Html/"+ filePathdate + "_AutomationHtmlReport.html");

        // pdf report
        System.setProperty("extent.reporter.pdf.start", "true");
        System.setProperty("extent.reporter.pdf.out", "target/Reports/cucumber/PdfReport/"+ filePathdate + "_AutomationPdfReport.pdf");

    }

    public static void writeReport() {

        TestLog.log().info("writing execution info");
        ExtentService.getInstance().setSystemInfo("User Name", System.getProperty("user.name"));
        ExtentService.getInstance().setSystemInfo("Time Zone", System.getProperty("user.timezone"));
        ExtentService.getInstance().setSystemInfo("Machine", System.getProperty("os.name"));
        ExtentService.getInstance().setSystemInfo("Java Version", System.getProperty("java.version"));

    }
}
