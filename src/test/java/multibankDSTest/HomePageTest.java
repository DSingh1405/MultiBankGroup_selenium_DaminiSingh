package multibankDSTest;

import java.util.Arrays;
import java.util.List;
import java.util.Set;

import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.testng.Assert;
import org.testng.annotations.*;

import multibank.DaminiSinghAssignment.PageObjects.HomePage;
import multibank.DaminiSinghAssignment.Utilities.FlakySimulator;
import multibank.DaminiSinghAssignment.Core.DriverManager;
import multibank.DaminiSinghAssignment.Listeners.RetryAnalyzer;

public class HomePageTest extends BaseTest {

    private HomePage homePage;

    @BeforeMethod(alwaysRun = true)
    public void initPage() {
        // BaseTest @BeforeMethod already:
        // - creates driver
        // - maximizes
        // - sets implicit wait
        // - navigates to baseUrl
        WebDriver driver = DriverManager.getDriver();
        homePage = new HomePage(driver);
    }

    @Test(
    	    retryAnalyzer = RetryAnalyzer.class,
    	    description = "RETRY-001 demo: flaky-style test with retry"
    	)
    	public void RETRY_001_demoRetryOnTransientFailure() {
    	    homePage = new HomePage(DriverManager.getDriver());

    	    // Simulate transient / flaky behaviour:
    	    // First attempt: fail
    	    // Second attempt: pass
    	    if (FlakySimulator.isFirstRun("RETRY_001_demoRetryOnTransientFailure")) {
    	        Assert.fail("Simulated transient wait/timing failure on first run.");
    	    }

    	    // Normal assertions that will pass on retry
    	    Assert.assertTrue(homePage.isLogoDisplayed(), "Logo should be visible after retry.");
    	}
    
    // ============================================================
    // NAV-001 – TopNavigation: Header visible; all required nav
    // ============================================================
    @Test
    public void NAV_001_TopNavigation_HeaderAndNavVisible() {
        String title = homePage.getPageTitle();
        Assert.assertTrue(title != null && !title.isEmpty(), "Page title is empty!");

        Assert.assertTrue(homePage.isLogoDisplayed(), "MultiBank logo is not visible!");

        List<String> actualNav = homePage.getNavigationItemTexts();
        Assert.assertFalse(actualNav.isEmpty(), "Navigation items list is empty!");

        List<String> expectedNav = Arrays.asList("Dashboard", "Markets", "Trade", "Features", "About Us", "Support");
        Assert.assertTrue(
                homePage.areAllExpectedNavItemsPresent(expectedNav),
                "Not all expected top navigation items are present!"
        );
    }

    // ============================================================
    // NAV-002 – TopNavigation: Hover + link mapping (href)
    // ============================================================
    @Test
    public void NAV_002_TopNavigation_HoverAndHrefMapping() {
        List<String> expectedNav = Arrays.asList("Trade", "Features", "About Us", "Support");

        for (String menu : expectedNav) {
            homePage.hoverNavigationItem(menu);

            String href = homePage.getNavigationItemHref(menu);
            Assert.assertNotNull(href, "Href is null for menu: " + menu);
            Assert.assertFalse(href.trim().isEmpty(), "Href is empty for menu: " + menu);
        }
    }

    // ============================================================
    // FOOT-001 – Footer/Marketing Banners (using market banner)
    // ============================================================
    @Test
    public void FOOT_001_MarketBanner_VisibleAndHasTexts() throws InterruptedException {
        JavascriptExecutor js = (JavascriptExecutor) DriverManager.getDriver();
        js.executeScript("arguments[0].scrollIntoView(true);", homePage.getMarketBanner());
        Thread.sleep(800);

        Assert.assertTrue(homePage.isMarketBannerVisible(), "Market banner container is not visible!");

        List<String> bannerTexts = homePage.getActiveBannerTexts();
        Assert.assertFalse(bannerTexts.isEmpty(), "No texts found in active banner!");

        System.out.println("Active Banner Texts: " + bannerTexts);
    }

    // ============================================================
    // APP-001 – Apple App Store link
    // ============================================================
    @Test
    public void APP_001_AppleAppStoreLink_NavigatesToCorrectPage() throws InterruptedException {
        WebDriver driver = DriverManager.getDriver();
        String originalWindow = driver.getWindowHandle();

        JavascriptExecutor js = (JavascriptExecutor) driver;
        js.executeScript("window.scrollTo(0, document.body.scrollHeight);");
        Thread.sleep(800);

        homePage.clickAppleAppStore();

        switchToNewTab(originalWindow);

        String currentUrl = driver.getCurrentUrl();
        String title = driver.getTitle();

        Assert.assertTrue(currentUrl.contains("apple.com"),
                "Apple Store URL does not look correct: " + currentUrl);
        Assert.assertFalse(title.isEmpty(), "Apple Store page title is empty!");

        driver.close();
        driver.switchTo().window(originalWindow);
    }

    // ============================================================
    // APP-002 – Google Play link
    // ============================================================
    @Test
    public void APP_002_GooglePlayLink_NavigatesToCorrectPage() throws InterruptedException {
        WebDriver driver = DriverManager.getDriver();
        String originalWindow = driver.getWindowHandle();

        JavascriptExecutor js = (JavascriptExecutor) driver;
        js.executeScript("window.scrollTo(0, document.body.scrollHeight);");
        Thread.sleep(800);

        homePage.clickGooglePlayStore();

        switchToNewTab(originalWindow);

        String currentUrl = driver.getCurrentUrl();
        String title = driver.getTitle();

        Assert.assertTrue(currentUrl.contains("play.google.com"),
                "Google Play URL does not look correct: " + currentUrl);
        Assert.assertFalse(title.isEmpty(), "Google Play page title is empty!");

        driver.close();
        driver.switchTo().window(originalWindow);
    }

    // ===== Helper: switch to newly opened tab =====
    private void switchToNewTab(String originalWindow) {
        WebDriver driver = DriverManager.getDriver();
        Set<String> allWindows = driver.getWindowHandles();
        for (String window : allWindows) {
            if (!window.equals(originalWindow)) {
                driver.switchTo().window(window);
                return;
            }
        }
        Assert.fail("No new tab/window was opened!");
    }
}