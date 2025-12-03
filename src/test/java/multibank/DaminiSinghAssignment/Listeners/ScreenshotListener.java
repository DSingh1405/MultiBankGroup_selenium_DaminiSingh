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

public class ScreenshotListener implements ITestListener {

    @Override
    public void onTestFailure(ITestResult result) {
        WebDriver driver = DriverManager.getDriver();
        if (driver == null) return;

        try {
            String browser = result.getTestContext().getCurrentXmlTest()
                                   .getParameter("browser");
            String testName = result.getMethod().getMethodName();
            long threadId = Thread.currentThread().getId();

            // ----- Retry awareness -----
            String attemptLabel = "FINAL_FAIL";
            int attemptNumber = 0;

            Object retryObj = result.getMethod().getRetryAnalyzer(result);
            if (retryObj instanceof RetryAnalyzer) {
                RetryAnalyzer ra = (RetryAnalyzer) retryObj;
                attemptNumber = ra.getAttempt();

                // if attempt < maxRetry => this failure will be retried
                if (attemptNumber < ra.getMaxRetry()) {
                    attemptLabel = "RETRY_ATTEMPT_" + attemptNumber;
                } else {
                    attemptLabel = "FINAL_FAIL";
                }
            }

            File src = ((TakesScreenshot) driver).getScreenshotAs(OutputType.FILE);

            Path dest = Path.of(
                    "artifacts",
                    browser + "_T" + threadId + "_" + testName + "_" + attemptLabel + ".png"
            );

            Files.createDirectories(dest.getParent());
            Files.copy(src.toPath(), dest);

            System.out.println("Screenshot saved for test: " + testName +
                    " | Browser: " + browser +
                    " | Thread: " + threadId +
                    " | AttemptLabel: " + attemptLabel);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
