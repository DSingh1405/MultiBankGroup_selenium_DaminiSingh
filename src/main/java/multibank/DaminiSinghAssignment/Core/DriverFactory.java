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

public class DriverFactory {

    public static WebDriver createInstance(String browserName) {
        WebDriver driver;

        boolean isHeadless = Boolean.parseBoolean(System.getProperty("headless", "false"));
        boolean isRemote  = Boolean.parseBoolean(System.getProperty("remote",  "false"));

        if (isRemote) {
            driver = createRemoteDriver(browserName, isHeadless);
        } else {
            driver = createLocalDriver(browserName, isHeadless);
        }
        return driver;
    }

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

    private static WebDriver createRemoteDriver(String browserName, boolean isHeadless) {
        // e.g. Selenium Grid / Selenoid URL passed as system property
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
        } catch (MalformedURLException e) {
            throw new RuntimeException("Invalid Grid URL: " + gridUrl, e);
        }
    }
}
