package multibankDSTest;

import java.time.Duration;

import org.openqa.selenium.WebDriver;
import org.testng.annotations.*;

import multibank.DaminiSinghAssignment.Core.DriverFactory;
import multibank.DaminiSinghAssignment.Core.DriverManager;

/**
 * BaseTest:
 * ----------
 * Parent class for all TestNG test classes.
 *
 * Responsibilities:
 *  - Manage WebDriver lifecycle (create → use → quit)
 *  - Provide a clean, isolated WebDriver instance per test method using ThreadLocal
 *  - Handle cross-browser execution through @Parameters
 *  - Navigate to the base URL before each test
 *
 * Pattern:
 *  - All test classes extend BaseTest
 *  - Pages never instantiate their own WebDriver; they use DriverManager.getDriver()
 */
public abstract class BaseTest {

    // Base URL for the AUT (Application Under Test)
    protected String baseUrl = "https://trade.multibank.io/";

    /**
     * @BeforeMethod
     * Runs BEFORE every @Test method.
     *
     * Responsibilities:
     *  - Create a new browser instance (Chrome by default)
     *  - Store the driver in ThreadLocal via DriverManager so tests do not share drivers
     *  - Configure implicit waits + maximize window
     *  - Navigate to base URL
     *
     * @param browser  Browser name passed from TestNG XML or defaults to "chrome"
     */
    @Parameters("browser")
    @BeforeMethod(alwaysRun = true)
    public void setUp(@Optional("chrome") String browser) {

        // Create a new isolated driver instance for THIS thread (for parallel execution compatibility)
        WebDriver driver = DriverFactory.createInstance(browser);

        // Store driver in ThreadLocal container
        DriverManager.setDriver(driver);

        // Basic browser configuration
        DriverManager.getDriver().manage().timeouts()
                .implicitlyWait(Duration.ofSeconds(10));  // fallback for simple waits
        DriverManager.getDriver().manage().window().maximize();

        // Navigate to the application
        DriverManager.getDriver().get(baseUrl);
    }

    /**
     * @AfterMethod
     * Runs AFTER every @Test method.
     *
     * Responsibilities:
     *  - Quit WebDriver cleanly
     *  - Remove WebDriver reference from ThreadLocal to prevent memory leaks
     */
    @AfterMethod(alwaysRun = true)
    public void tearDown() {
        WebDriver driver = DriverManager.getDriver();

        if (driver != null) {
            driver.quit();   // Close browser + WebDriver session
        }

        // Remove driver object from ThreadLocal storage
        DriverManager.unload();
    }
}
