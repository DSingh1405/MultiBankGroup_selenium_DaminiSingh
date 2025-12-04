package multibank.DaminiSinghAssignment.Listeners;

import com.aventstack.extentreports.ExtentReports;
import com.aventstack.extentreports.reporter.ExtentSparkReporter;

/**
 * ExtentManager
 * ------------------------------------------------------------
 * Centralized factory class for managing a single ExtentReports
 * instance across the test execution lifecycle.
 *
 * Why we need this:
 *  - ExtentReports must follow a singleton pattern.
 *  - Multiple instances cause report corruption or overwriting.
 *  - Thread-safe access ensures parallel TestNG runs remain stable.
 *
 * Responsibilities:
 *  - Create and configure ExtentReports object.
 *  - Attach Spark HTML reporter.
 *  - Provide global access via getInstance().
 */
public class ExtentManager {

    // Singleton instance of ExtentReports
    private static ExtentReports extent;

    /**
     * Provides a globally accessible, thread-safe ExtentReports instance.
     * If an instance does not exist, it initializes one.
     *
     * @return ExtentReports – the shared reporting instance for the suite
     */
    public static synchronized ExtentReports getInstance() {
        if (extent == null) {
            createInstance();
        }
        return extent;
    }

    /**
     * Creates the ExtentReport instance and configures the HTML reporter.
     *
     * Report output:
     *   <project_root>/target/extent-report/ExtentReport.html
     *
     * Configures:
     *   - Report name (displayed inside the HTML)
     *   - Document title (browser tab title)
     *   - System info metadata
     */
    private static void createInstance() {

        // Define output path for the Extent Report
        String reportPath = System.getProperty("user.dir") +
                "/target/extent-report/ExtentReport.html";

        // HTML reporter responsible for Spark-style Extent report UI
        ExtentSparkReporter spark = new ExtentSparkReporter(reportPath);
        spark.config().setReportName("Multibank UI Automation Report");
        spark.config().setDocumentTitle("Multibank Test Execution");

        // Main ExtentReports object
        extent = new ExtentReports();
        extent.attachReporter(spark);

        // Useful metadata included in the report
        extent.setSystemInfo("Project", "Multibank Trading");
        extent.setSystemInfo("Tester", "Damini Singh");
    }
}
