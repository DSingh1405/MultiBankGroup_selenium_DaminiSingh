package multibank.DaminiSinghAssignment.PageObjects;

import java.time.Duration;
import java.util.List;

import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.StaleElementReferenceException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.PageFactory;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.FluentWait;
import org.openqa.selenium.support.ui.Wait;
import org.openqa.selenium.support.ui.WebDriverWait;

/**
 * BasePage:
 * -----------
 * This class provides shared Selenium helpers for all Page Object classes.
 *
 * Responsibilities:
 *  - Initialize WebElements using PageFactory
 *  - Provide reusable waits (FluentWait)
 *  - Provide safe interaction helpers (click, visibility waits)
 *  - Provide scroll helpers for lazy-loaded or below-fold elements
 *  - Provide utility methods for table, SVG and dynamic content handling
 *
 * Pattern:
 *  - All page classes extend BasePage
 *  - Tests never instantiate WebDriver directly; they interact only via POM
 */
public abstract class BasePage {

    protected final WebDriver driver;
    protected Actions actions;

    // Default timeouts for FluentWait
    private static final Duration DEFAULT_TIMEOUT = Duration.ofSeconds(30);
    private static final Duration DEFAULT_POLLING = Duration.ofMillis(500);

    /**
     * Constructor:
     * - Stores driver reference
     * - Creates Actions instance for hover, drag, advanced interactions
     * - Initializes @FindBy WebElements via PageFactory
     */
    public BasePage(WebDriver driver) {
        this.driver = driver;
        this.actions = new Actions(driver);
        PageFactory.initElements(driver, this);
    }

    /**
     * Central FluentWait instance used across BasePage:
     * - 30-second timeout
     * - 500ms polling
     * - Ignores common transient exceptions (NoSuchElement, StaleElement)
     */
    protected Wait<WebDriver> getFluentWait() {
        return new FluentWait<>(driver)
                .withTimeout(DEFAULT_TIMEOUT)
                .pollingEvery(DEFAULT_POLLING)
                .ignoring(NoSuchElementException.class)
                .ignoring(StaleElementReferenceException.class);
    }

    // =========================================================================
    //  WAIT HELPERS (By-locator based)
    // =========================================================================

    /**
     * Waits for an element (By locator) to become visible in DOM.
     * Returns the WebElement once visible.
     */
    protected WebElement waitForVisibility(By locator) {
        return getFluentWait().until(driver -> {
            WebElement el = driver.findElement(locator);
            return el.isDisplayed() ? el : null;
        });
    }

    /**
     * Waits for element to become visible + enabled, then clicks.
     */
    protected void clickWhenReady(By locator) {
        WebElement element = getFluentWait().until(driver -> {
            WebElement el = driver.findElement(locator);
            return (el.isDisplayed() && el.isEnabled()) ? el : null;
        });
        element.click();
    }

    /**
     * Returns text of a visible element (By locator).
     */
    protected String getTextWhenVisible(By locator) {
        return waitForVisibility(locator).getText().trim();
    }

    // =========================================================================
    //  WAIT HELPERS (WebElement based – used for @FindBy elements)
    // =========================================================================

    /**
     * Click on a WebElement after waiting until it becomes visible + enabled.
     * Useful for @FindBy fields which may not be instantly ready.
     */
    protected void clickWhenReady(WebElement element) {
        getFluentWait().until(driver ->
                element.isDisplayed() && element.isEnabled()
        );
        element.click();
    }

    /**
     * Scrolls the element into viewport (center alignment).
     * Helps interact with elements dynamically rendered or below the fold.
     */
    protected void scrollIntoView(WebElement element) {
        ((JavascriptExecutor) driver)
                .executeScript("arguments[0].scrollIntoView({block: 'center', inline: 'nearest'});", element);
    }

    /**
     * Scrolls the element into view and waits for its visibility using WebDriverWait.
     * Used when page renders components only after scrolling.
     */
    protected WebElement scrollAndWaitVisible(WebElement element) {
        scrollIntoView(element);
        return new WebDriverWait(driver, DEFAULT_TIMEOUT)
                .until(ExpectedConditions.visibilityOf(element));
    }

    /**
     * Scrolls through each element in a list and checks visibility.
     * Useful for validating UI sections that load only after scroll.
     */
    protected boolean areAllDisplayedWithScroll(List<WebElement> elements) {
        for (WebElement el : elements) {
            scrollIntoView(el);
            if (!el.isDisplayed()) {
                return false;
            }
        }
        return true;
    }

    /**
     * Returns trimmed text of a @FindBy WebElement.
     * Safe wrapper that:
     *  - scrolls into view
     *  - handles exceptions gracefully
     */
    protected String getTextWhenVisible(WebElement element) {
        try {
            scrollIntoView(element);   // helps for lazy-loaded DOM segments
            return element.getText().trim();
        } catch (Exception e) {
            return "";
        }
    }

    /**
     * Checks if an element contains inline SVG graphics.
     * Used for charts, sparkline graphs, and “Last 7 Days” miniature charts.
     *
     * Looks for <path> or <g> inside the cell.
     */
    protected boolean hasSvgInside(WebElement cell) {
        try {
            return !cell.findElements(By.xpath(".//*[name()='path' or name()='g']")).isEmpty();
        } catch (Exception e) {
            return false;
        }
    }
}
