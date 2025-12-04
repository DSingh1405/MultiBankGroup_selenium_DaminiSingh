package multibank.DaminiSinghAssignment.Core;

import io.github.bonigarcia.wdm.WebDriverManager;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.edge.EdgeDriver;
import org.openqa.selenium.edge.EdgeOptions;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.firefox.FirefoxOptions;
import org.openqa.selenium.remote.RemoteWebDriver;

import java.net.MalformedURLException;
import java.net.URL;

/**
 * DriverFactory
 * ----------------
 * Central factory responsible for creating all WebDriver instances.
 *
 * Supports:
 *  - Local execution (Chrome, Firefox, Edge)
 *  - Remote execution (Selenium Grid / Selenoid) via RemoteWebDriver
 *  - Headless mode via system property
 *
 * Design:
 *  - Test classes never instantiate WebDriver directly.
 *  - BaseTest → calls DriverFactory → returns WebDriver → stored in DriverManager (ThreadLocal).
 *
 * System Properties Supported:
 *  - browserName → selects browser (chrome|firefox|edge)
 *  - headless=true|false → toggles headless execution
 *  - remote=true|false → runs tests locally or in Selenium Grid/Selenoid
 */
public class DriverFactory {

    /**
     * Creates a WebDriver instance based on:
     *  - browserName (chrome|firefox|edge)
     *  - system properties headless & remote
     *
     * @param browserName browser name passed from BaseTest or TestNG XML.
     * @return Initialized WebDriver
     */
    public static WebDriver createInstance(String browserName) {
        WebDriver driver;

        // Read runtime flags (from Maven command or TestNG XML)
        boolean isHeadless = Boolean.parseBoolean(System.getProperty("headless", "false"));
        boolean isRemote  = Boolean.parseBoolean(System.getProperty("remote",  "false"));

        // Remote vs Local driver selection
        if (isRemote) {
            driver = createRemoteDriver(browserName, isHeadless);
        } else {
            driver = createLocalDriver(browserName, isHeadless);
        }
        return driver;
    }

    /**
     * Creates a local WebDriver instance for Chrome, Firefox or Edge.
     * Uses WebDriverManager for automatic driver binary handling.
     *
     * @param browserName browser to launch
     * @param isHeadless whether headless mode should be enabled
     */
    private static WebDriver createLocalDriver(String browserName, boolean isHeadless) {
        WebDriver driver;

        switch (browserName.toLowerCase()) {

            case "firefox": {
                WebDriverManager.firefoxdriver().setup();
                FirefoxOptions ffOptions = new FirefoxOptions();

                if (isHeadless) {
                    ffOptions.addArguments("--headless", "--width=1920", "--height=1080");
                }
                driver = new FirefoxDriver(ffOptions);
                break;
            }

            case "edge": {
                WebDriverManager.edgedriver().setup();
                EdgeOptions edgeOptions = new EdgeOptions();

                if (isHeadless) {
                    edgeOptions.addArguments("--headless=new", "--window-size=1920,1080");
                }
                driver = new EdgeDriver(edgeOptions);
                break;
            }

            case "chrome":
            default: {
                WebDriverManager.chromedriver().setup();
                ChromeOptions chOptions = new ChromeOptions();

                if (isHeadless) {
                    chOptions.addArguments(
                            "--headless=new",
                            "--no-sandbox",
                            "--disable-dev-shm-usage",
                            "--window-size=1920,1080"
                    );
                }
                driver = new ChromeDriver(chOptions);
                break;
            }
        }

        return driver;
    }

    /**
     * Creates a RemoteWebDriver instance for executing tests in:
     *  - Selenium Grid
     *  - Selenoid
     *  - Dockerized browser clusters
     *
     *
     * @param browserName browser capability to use remotely
     * @param isHeadless enable headless inside container/grid
     */
    private static WebDriver createRemoteDriver(String browserName, boolean isHeadless) {

        // Fallback URL if not supplied
        String gridUrl = System.getProperty("gridUrl", "http://localhost:4444/wd/hub");

        try {

            switch (browserName.toLowerCase()) {

                case "firefox": {
                    FirefoxOptions ff = new FirefoxOptions();
                    if (isHeadless) {
                        ff.addArguments("--headless", "--width=1920", "--height=1080");
                    }
                    return new RemoteWebDriver(new URL(gridUrl), ff);
                }

                case "edge": {
                    EdgeOptions edge = new EdgeOptions();
                    if (isHeadless) {
                        edge.addArguments("--headless=new", "--window-size=1920,1080");
                    }
                    return new RemoteWebDriver(new URL(gridUrl), edge);
                }

                case "chrome":
                default: {
                    ChromeOptions ch = new ChromeOptions();
                    if (isHeadless) {
                        ch.addArguments(
                                "--headless=new",
                                "--no-sandbox",
                                "--disable-dev-shm-usage",
                                "--window-size=1920,1080"
                        );
                    }
                    return new RemoteWebDriver(new URL(gridUrl), ch);
                }
            }
        }
        catch (MalformedURLException e) {
            throw new RuntimeException("Invalid Grid URL: " + gridUrl, e);
        }
    }
}
