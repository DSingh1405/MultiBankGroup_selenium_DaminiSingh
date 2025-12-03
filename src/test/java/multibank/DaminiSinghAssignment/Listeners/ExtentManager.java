package multibank.DaminiSinghAssignment.Listeners;

import com.aventstack.extentreports.ExtentReports;
import com.aventstack.extentreports.reporter.ExtentSparkReporter;

public class ExtentManager {

    private static ExtentReports extent;

    public static synchronized ExtentReports getInstance() {
        if (extent == null) {
            createInstance();
        }
        return extent;
    }

    private static void createInstance() {
        String reportPath = System.getProperty("user.dir") +
                "/target/extent-report/ExtentReport.html";

        ExtentSparkReporter spark = new ExtentSparkReporter(reportPath);
        spark.config().setReportName("Multibank UI Automation Report");
        spark.config().setDocumentTitle("Multibank Test Execution");

        extent = new ExtentReports();
        extent.attachReporter(spark);

        // Optional: add system info
        extent.setSystemInfo("Project", "Multibank Trading");
        extent.setSystemInfo("Tester", "Damini Singh");
    }
}
