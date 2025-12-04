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

/**
 * Test class for homepage-level scenarios:
 *
 * Covers:
 *  - RETRY-001: Demonstration of TestNG retry logic on flaky-style failures
 *  - NAV-001: Top navigation header visibility + nav items
 *  - NAV-002: Hover behavior + href mapping
 *  - FOOT-001: Marketing banner visibility and text
 *  - APP-001: Apple App Store download link navigation
 *  - APP-002: Google Play download link navigation
 *
 * Uses:
 *  - HomePage POM for all interactions
 *  - BaseTest for WebDriver lifecycle and setup
 */
public class HomePageTest extends BaseTest {

    private HomePage homePage;

    /**
     * Common setup executed before each test.
     *
     * BaseTest @BeforeMethod already:
     *  - Creates WebDriver
     *  - Maximizes window
     *  - Sets timeouts / implicit waits
     *  - Navigates to base URL
     *
     * Here we just bind the driver to the HomePage POM.
     */
    @BeforeMethod(alwaysRun = true)
    public void initPage() {
        WebDriver driver = DriverManager.getDriver();
        homePage = new HomePage(driver);
    }

    // ============================================================
    // RETRY-001 – Demo of flaky-style retry using RetryAnalyzer
    // ============================================================
    /**
     * RETRY-001
     *
     * Purpose:
     *  - Demonstrate TestNG retry mechanism for transient failures.
     *
     * Behavior:
     *  - First run: explicitly fails via FlakySimulator
     *  - Retry run: passes, asserting logo visibility
     *
     * This is not part of functional requirements but shows robustness
     * for unstable environments (network issues, timing, etc.).
     */
    @Test(
        retryAnalyzer = RetryAnalyzer.class,
        description = "RETRY-001 demo: flaky-style test with retry"
    )
    public void RETRY_001_demoRetryOnTransientFailure() {
        homePage = new HomePage(DriverManager.getDriver());

        // Simulate transient/flaky behaviour
        if (FlakySimulator.isFirstRun("RETRY_001_demoRetryOnTransientFailure")) {
            Assert.fail("Simulated transient wait/timing failure on first run.");
        }

        // On retry, this should pass
        Assert.assertTrue(homePage.isLogoDisplayed(), "Logo should be visible after retry.");
    }

    // ============================================================
    // NAV-001 – TopNavigation: Header visible; all required nav
    // ============================================================
    /**
     * NAV-001
     *
     * Requirement:
     *  - Home page loaded.
     *  - Wait for header to be visible.
     *  - Collect displayed nav items.
     *  - Validate that all required nav options are present.
     *
     * Implementation:
     *  - Validate non-empty page title
     *  - Assert logo visibility
     *  - Read navigation texts using HomePage POM
     *  - Compare against expected nav list
     */
    @Test
    public void NAV_001_TopNavigation_HeaderAndNavVisible() {
        String title = homePage.getPageTitle();
        Assert.assertTrue(title != null && !title.isEmpty(), "Page title is empty!");

        Assert.assertTrue(homePage.isLogoDisplayed(), "MultiBank logo is not visible!");

        List<String> actualNav = homePage.getNavigationItemTexts();
        Assert.assertFalse(actualNav.isEmpty(), "Navigation items list is empty!");

        List<String> expectedNav = Arrays.asList(
                "Dashboard",
                "Markets",
                "Trade",
                "Features",
                "About Us",
                "Support"
        );

        Assert.assertTrue(
                homePage.areAllExpectedNavItemsPresent(expectedNav),
                "Not all expected top navigation items are present!"
        );
    }

    // ============================================================
    // NAV-002 – TopNavigation: Hover + link mapping (href)
    // ============================================================
    /**
     * NAV-002
     *
     * Requirement:
     *  - Home page loaded.
     *  - Iterate expected nav items.
     *  - For each:
     *      - Hover to verify visibility/interactability.
     *      - Check link target/href (without always leaving the page).
     *
     * Implementation:
     *  - For a subset of nav items:
     *      - Hover on each
     *      - Retrieve href attribute
     *      - Assert href is non-null and non-empty
     */
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
    /**
     * FOOT-001
     *
     * Requirement:
     *  - Home page loaded.
     *  - Scroll to bottom, wait for banners to be visible.
     *  - Collect banner titles/alt texts.
     *  - Validate all marketing banners are present and visible.
     *
     * Implementation:
     *  - Scroll into view of the market banner section
     *  - Assert banner container is visible
     *  - Read active banner texts via HomePage POM
     */
    @Test
    public void FOOT_001_MarketBanner_VisibleAndHasTexts() throws InterruptedException {
        JavascriptExecutor js = (JavascriptExecutor) DriverManager.getDriver();
        js.executeScript("arguments[0].scrollIntoView(true);", homePage.getMarketBanner());
        Thread.sleep(800);  // small pause to let animation complete

        Assert.assertTrue(homePage.isMarketBannerVisible(), "Market banner container is not visible!");

        List<String> bannerTexts = homePage.getActiveBannerTexts();
        Assert.assertFalse(bannerTexts.isEmpty(), "No texts found in active banner!");

        System.out.println("Active Banner Texts: " + bannerTexts);
    }

    // ============================================================
    // APP-001 – Apple App Store link
    // ============================================================
    /**
     * APP-001
     *
     * Requirement:
     *  - Home page loaded.
     *  - Under download section, click 'Download app' → 'App Store'.
     *  - Switch to new tab.
     *  - Capture URL and page title.
     *  - Ensure it navigates to correct Apple App Store product page (URL pattern + title).
     *
     * Implementation:
     *  - Click App Store button via HomePage POM
     *  - Switch to newly opened tab
     *  - Assert URL contains 'apple.com'
     *  - Assert title is non-empty
     *  - Close tab and return to original window
     */
    @Test
    public void APP_001_AppleAppStoreLink_NavigatesToCorrectPage() throws InterruptedException {
        WebDriver driver = DriverManager.getDriver();
        String originalWindow = driver.getWindowHandle();

        // Click on the App Store button in download section
        homePage.clickAppleAppStore();

        // Switch context to newly opened tab/window
        switchToNewTab(originalWindow);

        String currentUrl = driver.getCurrentUrl();
        String title = driver.getTitle();

        Assert.assertTrue(currentUrl.contains("apple.com"),
                "Apple Store URL does not look correct: " + currentUrl);
        Assert.assertFalse(title.isEmpty(), "Apple Store page title is empty!");

        // Clean up: close new tab and return to original
        driver.close();
        driver.switchTo().window(originalWindow);
    }

    // ============================================================
    // APP-002 – Google Play link
    // ============================================================
    /**
     * APP-002
     *
     * Requirement:
     *  - Home page loaded.
     *  - Under download section, click 'Download MultiBank.io app' → 'Google Play'.
     *  - Switch to new tab.
     *  - Capture URL and page title.
     *  - Ensure it navigates to correct Google Play product page (URL pattern + title).
     *
     * Implementation:
     *  - Click Google Play button via HomePage POM
     *  - Switch to newly opened tab
     *  - Assert URL contains 'play.google.com'
     *  - Assert title is non-empty
     *  - Close tab and return to original window
     */
    @Test
    public void APP_002_GooglePlayLink_NavigatesToCorrectPage() throws InterruptedException {
        WebDriver driver = DriverManager.getDriver();
        String originalWindow = driver.getWindowHandle();

        // Click on the Google Play button in download section
        homePage.clickGooglePlayStore();

        // Switch context to newly opened tab/window
        switchToNewTab(originalWindow);

        String currentUrl = driver.getCurrentUrl();
        String title = driver.getTitle();

        Assert.assertTrue(currentUrl.contains("play.google.com"),
                "Google Play URL does not look correct: " + currentUrl);
        Assert.assertFalse(title.isEmpty(), "Google Play page title is empty!");

        // Clean up: close new tab and return to original
        driver.close();
        driver.switchTo().window(originalWindow);
    }

    // ============================================================
    // Helper: switch to newly opened tab/window
    // ============================================================
    /**
     * Helper to switch WebDriver context to a newly opened tab/window.
     *
     * Logic:
     *  - Capture all window handles
     *  - Switch to the first handle that is not the original
     *  - If none found, fail the test explicitly
     */
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
