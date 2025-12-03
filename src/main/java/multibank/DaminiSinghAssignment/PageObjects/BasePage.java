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
import org.openqa.selenium.support.ui.FluentWait;
import org.openqa.selenium.support.ui.Wait;

public abstract class BasePage {

    protected final WebDriver driver;
    protected Actions actions;
    private static final Duration DEFAULT_TIMEOUT = Duration.ofSeconds(30);
    private static final Duration DEFAULT_POLLING = Duration.ofMillis(500);

    public BasePage(WebDriver driver) {
        this.driver = driver;
        this.actions = new Actions(driver);
        PageFactory.initElements(driver, this);
    }

    protected Wait<WebDriver> getFluentWait() {
        return new FluentWait<>(driver)
                .withTimeout(DEFAULT_TIMEOUT)
                .pollingEvery(DEFAULT_POLLING)
                .ignoring(NoSuchElementException.class)
                .ignoring(StaleElementReferenceException.class);
    }

    // ===== By-based helpers =====

    protected WebElement waitForVisibility(By locator) {
        return getFluentWait().until(driver -> {
            WebElement el = driver.findElement(locator);
            return el.isDisplayed() ? el : null;
        });
    }

    protected void clickWhenReady(By locator) {
        WebElement element = getFluentWait().until(driver -> {
            WebElement el = driver.findElement(locator);
            return (el.isDisplayed() && el.isEnabled()) ? el : null;
        });
        element.click();
    }

    protected String getTextWhenVisible(By locator) {
        return waitForVisibility(locator).getText().trim();
    }

    // ===== WebElement-based helpers (for @FindBy fields) =====

    protected void clickWhenReady(WebElement element) {
        getFluentWait().until(driver ->
                element.isDisplayed() && element.isEnabled()
        );
        element.click();
    }

//    protected String getTextWhenVisible(WebElement element) {
//        getFluentWait().until(driver -> element.isDisplayed());
//        return element.getText().trim();
//    }
    
    protected void scrollIntoView(WebElement element) {
        ((JavascriptExecutor) driver)
                .executeScript("arguments[0].scrollIntoView({block: 'center', inline: 'nearest'});", element);
    }
    
    protected boolean areAllDisplayedWithScroll(List<WebElement> elements) {
        for (WebElement el : elements) {
            scrollIntoView(el);
            // optional: tiny wait if needed via fluent wait
            if (!el.isDisplayed()) {
                return false;
            }
        }
        return true;
    }
    
 // NEW overload for WebElement – no FluentWait, just direct text
    protected String getTextWhenVisible(WebElement element) {
        try {
            scrollIntoView(element);     // optional, if you added this earlier
            return element.getText().trim();
            // or: return element.getDomProperty("innerText").trim();
        } catch (Exception e) {
            return "";
        }
    }
    
    protected boolean hasSvgInside(WebElement cell) {
        try {
            return !cell.findElements(By.xpath(".//*[name()='path' or name()='g']")).isEmpty();
        } catch (Exception e) {
            return false;
        }
    }
}
