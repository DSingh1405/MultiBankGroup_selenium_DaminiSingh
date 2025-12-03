package multibankDSTest;

import java.time.Duration;

import org.openqa.selenium.WebDriver;
import org.testng.annotations.*;

import multibank.DaminiSinghAssignment.Core.DriverFactory;
import multibank.DaminiSinghAssignment.Core.DriverManager;

public abstract class BaseTest {

    protected String baseUrl = "https://trade.multibank.io/";

    @Parameters("browser")
    @BeforeMethod(alwaysRun = true)
    public void setUp(@Optional("chrome") String browser) {

        // Create a new isolated driver instance for THIS thread
        WebDriver driver = DriverFactory.createInstance(browser);

        // Store in ThreadLocal
        DriverManager.setDriver(driver);

        // Basic setup
        DriverManager.getDriver().manage().timeouts()
                .implicitlyWait(Duration.ofSeconds(10));
        DriverManager.getDriver().manage().window().maximize();

        // Navigate
        DriverManager.getDriver().get(baseUrl);
    }

    @AfterMethod(alwaysRun = true)
    public void tearDown() {
        WebDriver driver = DriverManager.getDriver();

        if (driver != null) {
            driver.quit();
        }

        // Remove the driver from ThreadLocal to avoid memory leaks
        DriverManager.unload();
    }
}