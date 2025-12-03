package multibank.DaminiSinghAssignment.Core;

import io.github.bonigarcia.wdm.WebDriverManager;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
/*import org.openqa.selenium.edge.EdgeDriver;
import org.openqa.selenium.edge.EdgeOptions;*/
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.firefox.FirefoxOptions;

public class DriverFactory {

	public static WebDriver createInstance(String browserName) {
		WebDriver driver;

		// Read headless flag from system property (default false)
		boolean isHeadless = Boolean.parseBoolean(System.getProperty("headless", "false"));

		switch (browserName.toLowerCase()) {
		case "firefox": {
			WebDriverManager.firefoxdriver().setup();
			FirefoxOptions ffOptions = new FirefoxOptions();

			if (isHeadless) {
				ffOptions.addArguments("--headless");
				ffOptions.addArguments("--width=1920");
				ffOptions.addArguments("--height=1080");
			}

			driver = new FirefoxDriver(ffOptions);
			break;
		}
		/*
		 * case "edge": { WebDriverManager.edgedriver().setup(); EdgeOptions edgeOptions
		 * = new EdgeOptions(); if (isHeadless) { edgeOptions.addArguments(
		 * "--headless=new", "--window-size=1920,1080" ); } driver = new
		 * EdgeDriver(edgeOptions); break; }
		 */
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
}
