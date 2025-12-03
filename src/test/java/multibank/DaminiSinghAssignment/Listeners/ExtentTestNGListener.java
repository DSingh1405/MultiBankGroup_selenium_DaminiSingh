package multibank.DaminiSinghAssignment.Listeners;

import com.aventstack.extentreports.ExtentReports;
import com.aventstack.extentreports.ExtentTest;
import com.aventstack.extentreports.Status;
import org.testng.*;

public class ExtentTestNGListener implements ITestListener, ISuiteListener {

    private static ExtentReports extent = ExtentManager.getInstance();
    private static ThreadLocal<ExtentTest> testThread = new ThreadLocal<>();

    @Override
    public void onStart(ISuite suite) {
        // suite start
    }

    @Override
    public void onFinish(ISuite suite) {
        extent.flush();
    }

    @Override
    public void onTestStart(ITestResult result) {
        String testName = result.getMethod().getMethodName();
        ExtentTest test = extent.createTest(testName)
                .assignCategory(result.getTestContext().getCurrentXmlTest().getName());
        testThread.set(test);
    }

    @Override
    public void onTestSuccess(ITestResult result) {
        testThread.get().log(Status.PASS, "Test passed");
    }

    @Override
    public void onTestFailure(ITestResult result) {
        ExtentTest t = testThread.get();
        t.log(Status.FAIL, "Test failed: " + result.getThrowable());

        // Optional: link screenshot saved by ScreenshotListener
        String browser = result.getTestContext().getCurrentXmlTest().getParameter("browser");
        long threadId = Thread.currentThread().getId();
        String testName = result.getMethod().getMethodName();

        String screenshotPath = "artifacts/" + browser + "_T" + threadId + "_" + testName + "_FINAL_FAIL.png";
        t.addScreenCaptureFromPath(screenshotPath);
    }

    @Override
    public void onTestSkipped(ITestResult result) {
        testThread.get().log(Status.SKIP, "Test skipped");
    }

    @Override
    public void onTestFailedButWithinSuccessPercentage(ITestResult result) {
        // not used
    }

    @Override
    public void onTestFailedWithTimeout(ITestResult result) {
        onTestFailure(result);
    }

    @Override
    public void onStart(ITestContext context) {
        // not used
    }

    @Override
    public void onFinish(ITestContext context) {
        // not used
    }
}
