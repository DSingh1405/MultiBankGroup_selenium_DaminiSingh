package multibank.DaminiSinghAssignment.PageObjects;

import java.util.ArrayList;
import java.util.List;

import org.openqa.selenium.By;
import org.openqa.selenium.ElementClickInterceptedException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;

/**
 * HomePage POM for MultiBank.io
 *
 * Responsibilities:
 *  - Models the top navigation bar (NAV-001, NAV-002)
 *  - Models the marketing banner carousel (FOOT-001 equivalent on homepage)
 *  - Models the app download section (APP-001, APP-002)
 *
 * Exposes:
 *  - Methods to read navigation items and hover/open submenus
 *  - Methods to inspect marketing banners
 *  - Methods to click store download buttons and handle edge cases
 */
public class HomePage extends BasePage {

    public HomePage(WebDriver driver) {
        super(driver);
    }

    // ============================================
    // ====== Top Navigation (NAV-001 / NAV-002)
    // ============================================

    /**
     * All top-level navigation items in the header.
     * Example: Dashboard, Markets, Trade, Wallet, About Us, etc.
     *
     */
    @FindBy(xpath = "//div[contains(@class,'style_menu-container')]//*[self::a or self::span][contains(@class,'style_menu-item')]")
    private List<WebElement> navigationItemList;

    /**
     * Sub-menu items shown in popover after hovering a main nav item.
     * Example: under "Markets" → Spot, Instant Buy, Panic Sell, etc.
     *
     */
    @FindBy(xpath = "//div[contains(@class,'popover')]//div[contains(@class,'style_text')]")
    private List<WebElement> navigationSubItemList;

    /**
     * MultiBank logo in the header.
     */
    @FindBy(xpath = "//img[contains(@class, 'style_logo')]")
    private WebElement mbLogo;

    // ==================================================
    // ====== Marketing Banner / Carousel (FOOT-001)
    // ==================================================

    /**
     * Root container for the banner carousel on the homepage.
     */
    @FindBy(css = ".slick-list")
    private WebElement marketBanner;

    /**
     * All text spans inside the currently active banner slide.
     */
    @FindBy(css = ".slick-list div[class*='slick-active'] span")
    private List<WebElement> activeBannerSpans;

    /**
     * Next arrow button to move the banner carousel.
     */
    @FindBy(xpath = "//button[contains(@class,'style_next')]")
    private WebElement bannerNextArrow;

    // ============================================
    // ====== App Store Buttons (APP-001 / APP-002)
    // ============================================

    /**
     * Button for Apple App Store download.
     * Located inside the "download app" section.
     *
     */
    @FindBy(xpath = "//div[contains(@class,'app-button')][.//img[@alt='app-store']]")
    private WebElement appleAppStoreButton;

    /**
     * Button for Google Play download.
     *
     */
    @FindBy(xpath = "//div[contains(@class,'app-button')][.//img[@alt='google-play']]")
    private WebElement googlePlayStoreButton;

    // ==========================
    // ====== Page helpers
    // ==========================

    /** Returns the browser page title for quick smoke checks. */
    public String getPageTitle() {
        return driver.getTitle();
    }

    // ==========================
    // ====== Navigation APIs
    // ==========================

    /**
     * Returns the visible texts of all top-level header navigation items.
     *
     * Supports NAV-001:
     *   - Collect displayed nav options
     *   - Compare list against external/expected data
     */
    public List<String> getNavigationItemTexts() {
        List<String> navMainItems = new ArrayList<>();
        for (WebElement element : navigationItemList) {
            navMainItems.add(element.getText().trim());
        }
        return navMainItems;
    }

    /**
     * Returns visible texts of all sub-menu items currently rendered in the popover.
     *
     * Supports NAV-002:
     *   - After hover, verify expected sub-menu items are present
     */
    public List<String> getNavigationSubItemTexts() {
        List<String> navMainSubItems = new ArrayList<>();
        for (WebElement element : navigationSubItemList) {
            navMainSubItems.add(element.getText().trim());
        }
        return navMainSubItems;
    }

    /**
     * Clicks a top navigation item by its visible text.
     *
     * Example:
     *   clickNavigationItem("Markets");
     *   clickNavigationItem("About Us");
     *
     * Used for navigation-based tests or deep-link flows.
     */
    public void clickNavigationItem(String name) {
        for (WebElement element : navigationItemList) {
            if (element.getText().trim().equalsIgnoreCase(name)) {
                element.click();
                return;
            }
        }
        throw new RuntimeException("Navigation item not found: " + name);
    }

    /**
     * Hovers on any top navigation item by visible text.
     *
     * Supports NAV-002:
     *   - For each expected nav item:
     *       - hoverNavigationItem(name)
     *       - assert sub-menu visibility and links
     */
    public void hoverNavigationItem(String name) {
        for (WebElement element : navigationItemList) {
            if (element.getText().trim().equalsIgnoreCase(name)) {
                actions.moveToElement(element).perform();
                return;
            }
        }
        throw new RuntimeException("Navigation item not found to hover: " + name);
    }

    /**
     * Returns the href attribute for a given navigation item.
     *
     * Supports NAV-002:
     *   - For each item, verify it is mapped to the correct target route
     *     without necessarily clicking through.
     */
    public String getNavigationItemHref(String name) {
        for (WebElement element : navigationItemList) {
            if (element.getText().trim().equalsIgnoreCase(name)) {
                return element.getAttribute("href");
            }
        }
        throw new RuntimeException("Navigation item not found: " + name);
    }

    // ==============================
    // ====== Banner / Marketing
    // ==============================

    /**
     * Returns all visible text from the currently active banner slide.
     *
     * Supports FOOT-001:
     *   - Scroll to banner (if needed, in test)
     *   - Collect titles/alt-texts (via span text)
     *   - Compare against expected marketing copy
     */
    public List<String> getActiveBannerTexts() {
        List<String> texts = new ArrayList<>();
        for (WebElement span : activeBannerSpans) {
            if (span.isDisplayed()) {   // defensive check for moving carousel slides
                texts.add(span.getText().trim());
            }
        }
        return texts;
    }

    /** Exposes the banner root for any additional low-level checks if required. */
    public WebElement getMarketBanner() {
        return marketBanner;
    }

    /**
     * Clicks the "next" arrow of the banner carousel.
     *
     */
    public void clickNextBanner() {
        bannerNextArrow.click();
    }

    // ==============================
    // ====== Sub-menu interaction
    // ==============================

    /**
     * Clicks a sub-menu item (popover entry) by partial text match.
     *
     * Example:
     *   clickSubMenuItem("Spot");
     *   clickSubMenuItem("Why Multibank?");
     *
     * Used by openMenuItem(...) for composite flows.
     */
    public void clickSubMenuItem(String name) {
        for (WebElement element : navigationSubItemList) {
            String fullText = element.getText().trim();
            String firstLine = fullText.split("\n")[0];
            System.out.println(firstLine);

            if (element.getText().trim().contains(name)) {
                element.click();
                return;
            }
        }
        throw new RuntimeException("Sub-menu item not found: " + name);
    }

    // ==============================
    // ====== App store buttons
    // ==============================

    /**
     * Clicks the Apple App Store button within the download section.
     *
     * APP-001 uses this:
     *   - Call clickAppleAppStore()
     *   - Switch to new tab in the test
     *   - Assert App Store product page URL + title match expected pattern
     *
     * Contains:
     *   - scrollIntoView and clickWhenReady (from BasePage)
     *   - Fallback to direct click if header overlaps element
     */
    public void clickAppleAppStore() {
        try {
            scrollIntoView(appleAppStoreButton);
            clickWhenReady(appleAppStoreButton);
        } catch (ElementClickInterceptedException e) {
            // Fallback to normal click when some overlay/header intercepts
            appleAppStoreButton.click();
        }
    }

    /**
     * Clicks the Google Play Store button within the download section.
     *
     * APP-002 uses this:
     *   - Call clickGooglePlayStore()
     *   - Switch to new tab in the test
     *   - Assert Google Play product page URL + title match expected pattern
     */
    public void clickGooglePlayStore() {
        try {
            scrollIntoView(googlePlayStoreButton);
            clickWhenReady(googlePlayStoreButton);
        } catch (ElementClickInterceptedException e) {
            // Fallback to normal click when some overlay/header intercepts
            googlePlayStoreButton.click();
        }
    }

    // ==============================
    // ====== Composite navigation
    // ==============================

    /**
     * Composite helper:
     *   - Hover main menu item (e.g., "About Us")
     *   - Wait for popover visibility
     *   - Click sub-menu item (e.g., "Why Multibank?")
     *
     * This is used heavily in page navigation tests like:
     *   - ABOUT-001 / ABOUT-002
     *   - And can also support other flows needing nested nav access.
     */
    public void openMenuItem(String menuName, String subMenuName) {
        hoverNavigationItem(menuName);
        waitForVisibility(By.xpath("//div[contains(@class,'popover')]//div[contains(@class,'style_text')]"));
        clickSubMenuItem(subMenuName);
    }

    // =========================================
    // ====== Verification convenience methods
    // =========================================

    /** Simple check that the header logo is displayed. */
    public boolean isLogoDisplayed() {
        try {
            return mbLogo.isDisplayed();
        } catch (Exception e) {
            return false;
        }
    }

    /**
     * Verifies that all expected navigation items are present in the top menu.
     *
     * Example:
     *   areAllExpectedNavItemsPresent(Arrays.asList("Dashboard", "Markets", "Trade", "Wallet"));
     *
     * Supports NAV-001 list comparison against spec.
     */
    public boolean areAllExpectedNavItemsPresent(List<String> expected) {
        List<String> actual = getNavigationItemTexts();
        for (String exp : expected) {
            boolean found = actual.stream().anyMatch(a -> a.equalsIgnoreCase(exp));
            if (!found) {
                return false;
            }
        }
        return true;
    }

    /**
     * Checks if a given sub-menu option (e.g. "Spot") is present after hover.
     *
     * Often used with:
     *   hoverNavigationItem("Markets");
     *   isSubMenuOptionPresent("Spot");
     */
    public boolean isSubMenuOptionPresent(String expected) {
        List<String> actual = getNavigationSubItemTexts();
        return actual.stream().anyMatch(a -> a.equalsIgnoreCase(expected));
    }

    /**
     * Checks if the marketing banner carousel root is displayed.
     *
     * Supports FOOT-001 as an initial visibility assertion.
     */
    public boolean isMarketBannerVisible() {
        try {
            return marketBanner.isDisplayed();
        } catch (Exception e) {
            return false;
        }
    }
}
