package multibank.DaminiSinghAssignment.Core;

import org.openqa.selenium.WebDriver;

/**
 * DriverManager
 * ----------------------
 * Thread-safe WebDriver container using ThreadLocal.
 *
 * Purpose:
 *  - Ensures each test thread running in parallel receives a separate,
 *    isolated WebDriver instance.
 *
 * Why ThreadLocal?
 *  - TestNG parallel runs (methods/tests/classes) require independent WebDriver
 *    sessions to avoid cross-thread interference.
 *  - ThreadLocal ensures:
 *        Thread A → gets its own driver
 *        Thread B → gets its own driver
 *        No overlap, no race conditions.
 *
 * Lifecycle:
 *  - BaseTest @BeforeMethod → calls setDriver()
 *  - Tests use DriverManager.getDriver()
 *  - BaseTest @AfterMethod → quit() and unload() to prevent memory leaks
 */
public class DriverManager {

    // Holds WebDriver for the current thread only
    private static final ThreadLocal<WebDriver> driver = new ThreadLocal<>();

    /**
     * Returns the WebDriver instance assigned to the current thread.
     *
     * @return WebDriver for this thread
     */
    public static WebDriver getDriver() {
        return driver.get();
    }

    /**
     * Stores a new WebDriver instance inside the ThreadLocal container.
     *
     * @param driverInstance WebDriver created by DriverFactory
     */
    public static void setDriver(WebDriver driverInstance) {
        driver.set(driverInstance);
    }

    /**
     * Removes WebDriver reference from ThreadLocal to avoid:
     *  - memory leaks
     *  - stale driver references
     */
    public static void unload() {
        driver.remove();
    }
}
