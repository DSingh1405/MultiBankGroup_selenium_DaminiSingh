package multibank.DaminiSinghAssignment.Listeners;

import multibank.DaminiSinghAssignment.Core.DriverManager;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.WebDriver;
import org.testng.ITestListener;
import org.testng.ITestResult;

import java.io.File;
import java.nio.file.Files;
import java.nio.file.Path;

/**
 * ScreenshotListener:
 * -------------------
 * This TestNG listener captures a screenshot every time a test fails.
 *
 * Key Features:
 *  - Supports parallel execution (uses thread ID in filename)
 *  - Integrates with RetryAnalyzer to tag screenshots as:
 *        RETRY_ATTEMPT_X   → failure occurred but test will retry
 *        FINAL_FAIL        → failure after exhausting retries
 *  - Stores screenshots under /artifacts/ folder for reporting
 *
 * Execution flow:
 *  1. Test fails → onTestFailure() triggered.
 *  2. Retrieves driver from DriverManager (ThreadLocal-safe).
 *  3. Determines retry attempt number (if retry enabled).
 *  4. Takes screenshot using TakesScreenshot.
 *  5. Saves screenshot with structured file naming.
 */
public class ScreenshotListener implements ITestListener {

    @Override
    public void onTestFailure(ITestResult result) {

        // Retrieve driver for this test thread
        WebDriver driver = DriverManager.getDriver();
        if (driver == null) return; // No driver → nothing to capture

        try {
            // Metadata for naming screenshot file
            String browser = result.getTestContext()
                                   .getCurrentXmlTest()
                                   .getParameter("browser");

            String testName = result.getMethod().getMethodName();
            long threadId  = Thread.currentThread().getId();

            // ------------------------------
            // Determine retry status
            // ------------------------------
            String attemptLabel = "FINAL_FAIL"; // default
            int attemptNumber   = 0;

            Object retryObj = result.getMethod().getRetryAnalyzer(result);

            if (retryObj instanceof RetryAnalyzer) {
                RetryAnalyzer ra = (RetryAnalyzer) retryObj;
                attemptNumber = ra.getAttempt();

                // If current failure still has retries left
                if (attemptNumber < ra.getMaxRetry()) {
                    attemptLabel = "RETRY_ATTEMPT_" + attemptNumber;
                }
            }

            // ------------------------------
            // Capture screenshot
            // ------------------------------
            File src = ((TakesScreenshot) driver).getScreenshotAs(OutputType.FILE);

            // File naming convention ensures uniqueness per browser/thread/test/attempt
            Path dest = Path.of(
                    "artifacts",
                    browser + "_T" + threadId + "_" + testName + "_" + attemptLabel + ".png"
            );

            Files.createDirectories(dest.getParent()); // ensure folder exists
            Files.copy(src.toPath(), dest);            // save screenshot

            System.out.println(
                    "Screenshot saved for test: " + testName +
                    " | Browser: " + browser +
                    " | Thread: " + threadId +
                    " | AttemptLabel: " + attemptLabel
            );

        } catch (Exception e) {
            // Avoid breaking execution flow; simply log screenshot capture failure
            e.printStackTrace();
        }
    }
}
