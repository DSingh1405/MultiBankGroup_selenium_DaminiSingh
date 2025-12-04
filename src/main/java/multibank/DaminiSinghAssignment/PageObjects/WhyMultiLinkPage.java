package multibank.DaminiSinghAssignment.PageObjects;

import java.util.List;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;

/**
 * Page Object Model for the "Why MultiBank" page.
 * 
 * This class exposes:
 *  - Component visibility validation
 *  - Text extraction for all headings, subheadings, and CTAs
 *  - Scroll handling to ensure lazy-loaded elements become visible
 *  - Normalized text outputs for JSON comparison (assignment requirement)
 */
public class WhyMultiLinkPage extends BasePage {

    public WhyMultiLinkPage(WebDriver driver) {
        super(driver);
    }

    /** 
     * Locators for all main sections on the Why MultiBank page.
     * These elements are collected into lists based on repeating UI structure.
     */

    // Main section headings (e.g., “Securely Build…”, “Our Advantages”)
    @FindBy(xpath = "//h2[contains(@class,'text-white heading')]")
    private List<WebElement> MainCardsHeading;

    // Subheadings / paragraph descriptions under the main info cards
    @FindBy(xpath = "//p[contains(@class,'text-secondary')]")
    private List<WebElement> MainCardSubHeading;

    // Advantage section headings (Fiat On/Off Ramp, Heavily Regulated, etc.)
    @FindBy(xpath = "//h3[contains(@class,'text-white heading')]")
    private List<WebElement> OurAdvantagesCardHeading;

    // CTA buttons shown across the page (“Open Live Account”, “Get Started”, etc.)
    @FindBy(xpath = "//button[contains(@class,'button_btn')]")
    private List<WebElement> WhyMultiLinkButtons;

    /**
     * Wait method to ensure the page's hero section is loaded fully.
     * We use a unique section wrapper which always appears first.
     */
    public void waitForPageToLoad() {
        waitForVisibility(
            By.xpath("//section[@class='homePageBannerSectionWrapper']")
        );
    }

    /**
     * Assignment: ABOUT-001 validation
     * Checks that all major visible components are present on the screen.
     * We scroll each element into view before checking visibility
     * because the website uses lazy-loading + fade-in animations.
     */
    public boolean areAllComponentsVisible() {
        waitForPageToLoad();

        try {
            return MainCardsHeading.stream().allMatch(e -> scrollAndWaitVisible(e).isDisplayed())
                && MainCardSubHeading.stream().allMatch(e -> scrollAndWaitVisible(e).isDisplayed())
                && OurAdvantagesCardHeading.stream().allMatch(e -> scrollAndWaitVisible(e).isDisplayed())
                && WhyMultiLinkButtons.stream().allMatch(e -> scrollAndWaitVisible(e).isDisplayed());
        } catch (Exception ex) {
            return false;
        }
    }

    /**
     * Utility: Normalize extracted UI text so it matches JSON expected text.
     * Removes extra whitespace, newlines, and formatting differences.
     */
    private String normalizeText(String text) {
        return text.trim().replaceAll("\\s+", " ");
    }

    /**
     * Extracts normalized text for main headings on the page.
     * Scroll is required because headings load with animations.
     */
    public List<String> getMainCardsHeadingTexts() {
        waitForPageToLoad();

        MainCardsHeading.forEach(e ->
            System.out.println("Raw subheading text: [" + e.getText() + "]")
        );

        return MainCardsHeading.stream()
                .map(e -> {
                    WebElement visible = scrollAndWaitVisible(e);
                    return normalizeText(visible.getText());
                })
                .filter(t -> !t.isBlank())
                .toList();
    }

    /**
     * Extracts the long-form descriptive paragraphs under each main card.
     * These are lazy-loaded, so we scroll each one before extracting text.
     */
    public List<String> getMainCardSubHeadingTexts() {
        waitForPageToLoad();

        MainCardSubHeading.forEach(e ->
            System.out.println("Raw subheading text: [" + e.getText() + "]")
        );

        return MainCardSubHeading.stream()
                .map(e -> {
                    WebElement visible = scrollAndWaitVisible(e);
                    return normalizeText(visible.getText());
                })
                .filter(t -> !t.isBlank())
                .toList();
    }

    /**
     * Extracts headings for the "Our Advantages" cards.
     */
    public List<String> getOurAdvantagesCardHeadingTexts() {
        waitForPageToLoad();

        OurAdvantagesCardHeading.forEach(e ->
            System.out.println("Raw subheading text: [" + e.getText() + "]")
        );

        return OurAdvantagesCardHeading.stream()
                .map(e -> {
                    WebElement visible = scrollAndWaitVisible(e);
                    return normalizeText(visible.getText());
                })
                .filter(t -> !t.isBlank())
                .toList();
    }

    /**
     * Extracts CTA button text (LOGIN, SIGN UP, Get Started, etc.)
     * 
     * Logic considerations:
     *  - Page contains buttons outside the WhyMultibank content (e.g., site header)
     *  - Some buttons (e.g., “Start Now”) appear twice due to DOM duplication
     *  - Scroll is used but WITHOUT hard waits (to avoid timeout on hidden buttons)
     *  - Only visible buttons are considered valid for JSON comparison
     *  - Duplicate text entries are removed via .distinct()
     */
    public List<String> getWhyMultiLinkButtonTexts() {
        waitForPageToLoad();

        List<String> texts = new java.util.ArrayList<>();

        for (WebElement button : WhyMultiLinkButtons) {

            // Bring button into view (required for lazy-loaded elements)
            try {
                ((org.openqa.selenium.JavascriptExecutor) driver)
                        .executeScript("arguments[0].scrollIntoView({block:'center'});", button);
            } catch (Exception ignored) {
                // Scrolling failure is non-blocking
            }

            // Skip elements not actually visible in active UI sections
            if (!button.isDisplayed()) {
                continue;
            }

            // Use innerText to capture nested children used inside buttons
            String raw = button.getAttribute("innerText");
            if (raw == null || raw.isBlank()) {
                raw = button.getText();
            }

            String normalized = normalizeText(raw);
            System.out.println("Button text captured: [" + normalized + "]");

            if (!normalized.isBlank()) {
                texts.add(normalized);
            }
        }

        // Remove duplicated labels (e.g., repeated “Start Now”) before comparison
        return texts.stream()
                .distinct()
                .toList();
    }
}
