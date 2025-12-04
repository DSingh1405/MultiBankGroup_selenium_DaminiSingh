package multibank.DaminiSinghAssignment.PageObjects;

import java.util.ArrayList;
import java.util.List;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;

/**
 * Page Object Model for the "Spot" section on the homepage.
 *
 * This class supports:
 *  - SPOT-001: Navigating through all Spot categories and scraping trading pairs
 *  - SPOT-002: Validating structural elements (column headers, price data, format)
 *
 * It relies on BasePage utility methods such as:
 *  - getTextWhenVisible(...)
 *  - clickWhenReady(...)
 *  - scrollIntoView(...)
 *  - hasSvgInside(...)
 */
public class SpotSectionPage extends BasePage {

    public SpotSectionPage(WebDriver driver) {
        super(driver);
    }

    // =========================
    // Locators for Spot section
    // =========================

    /**
     * Root container for the Spot section on the homepage.
     */
    @FindBy(xpath = "//div[contains(@class,'home_home')]//div[.//span[normalize-space()='Spot']]")
    private WebElement SpotCategoryDiv;

    /**
     * Category filter buttons within the Spot section.
     * Example: "Top", "New", "Favourite" (depending on the UI).
     */
    @FindBy(xpath = "//div[contains(@class,'home_home')]//div[.//span[normalize-space()='Spot']]//div[contains(@class, 'style_list')]/button")
    private List<WebElement> SpotCategoryButtons;

    /**
     * Column headers for the Spot trading table.
     * Example: "Pair", "Price", "24h Change", "High", "Low", "Last 7 Days".
     */
    @FindBy(xpath = "//tr[contains(@class,'style_headers')]/th[.//span]")
    private List<WebElement> spotColumnHeaders;

    /**
     * Trading pair symbols for the current Spot category.
     * Example values: "BTC-USDT", "ETH-USDT".
     */
    @FindBy(xpath = "//div[contains(@class,'home_home')]//div[.//span[normalize-space()='Spot']]//div[contains(@class, 'style_table')]//div[contains(@class, 'asset-list_pair')]")
    private List<WebElement> SpotAssetPairsColumn;

    /**
     * Price column values for each trading pair.
     */
    @FindBy(xpath = "//div[contains(@class,'home_home')]//div[.//span[normalize-space()='Spot']]//div[contains(@class, 'style_table')]//td[contains(@id, 'price-td')]")
    private List<WebElement> SpotPriceColumn;

    /**
     * 24h change column values (percentage or absolute).
     */
    @FindBy(xpath = "//div[contains(@class,'home_home')]//div[.//span[normalize-space()='Spot']]//div[contains(@class, 'style_table')]//td[contains(@id, 'change_in_price-td')]")
    private List<WebElement> Spot24HrChangeColumn;

    /**
     * Daily high price column values.
     */
    @FindBy(xpath = "//div[contains(@class,'home_home')]//div[.//span[normalize-space()='Spot']]//div[contains(@class, 'style_table')]//td[contains(@id, 'high')]")
    private List<WebElement> SpotHighColumn;

    /**
     * Daily low price column values.
     */
    @FindBy(xpath = "//div[contains(@class,'home_home')]//div[.//span[normalize-space()='Spot']]//div[contains(@class, 'style_table')]//td[contains(@id, 'low')]")
    private List<WebElement> SpotLowColumn;

    /**
     * Last 7 days / base volume column values (often visualised with SVG charts).
     */
    @FindBy(xpath = "//div[contains(@class,'home_home')]//div[.//span[normalize-space()='Spot']]//div[contains(@class, 'style_table')]//td[contains(@id, 'base_volume')]")
    private List<WebElement> SpotLast7Column;

    // =========================
    // Public API methods
    // =========================

    /**
     * Returns the visible labels of all Spot category buttons.
     * 
     * Used in SPOT-001:
     *   - iterate through each category
     *   - click the category
     *   - verify trading pairs for that category
     */
    public List<String> getSpotCategoryButtonText() {
        List<String> spotCatButtonNames = new ArrayList<>();
        for (WebElement element : SpotCategoryButtons) {
            spotCatButtonNames.add(getTextWhenVisible(element));
        }
        return spotCatButtonNames;
    }

    /**
     * Returns the visible Spot table column headers.
     *
     * Used in SPOT-002 to validate structural elements and ensure
     * all expected columns are present.
     */
    public List<String> getSpotColumnHeaderText() {
        List<String> spotColumnNames = new ArrayList<>();
        for (WebElement element : spotColumnHeaders) {
            spotColumnNames.add(getTextWhenVisible(element));
        }
        return spotColumnNames;
    }

    /**
     * Simple visibility check for the Spot section container.
     *
     */
    public boolean isSpotSectionVisible() {
        try {
            return SpotCategoryDiv.isDisplayed();
        } catch (Exception e) {
            return false;
        }
    }

    /**
     * Clicks a Spot category button by visible text.
     *
     * If the category is not found, an exception is thrown to make
     * failures explicit in the test report.
     *
     * Example usage in test:
     *   clickSpotCategoryByName("Top");
     *   clickSpotCategoryByName("New");
     */
    public void clickSpotCategoryByName(String categoryName) {
        for (WebElement button : SpotCategoryButtons) {
            String text = getTextWhenVisible(button);
            if (text.equalsIgnoreCase(categoryName)) {
                clickWhenReady(button);
                return;
            }
        }
        throw new RuntimeException("Spot category not found: " + categoryName);
    }

    /**
     * Returns all trading pair symbols currently visible in the Spot table
     * for the active category.
     *
     * Used in SPOT-001 to scrape trading pairs per category.
     */
    public List<String> getVisibleTradingPairs() {
        List<String> assetPairs = new ArrayList<>();
        for (WebElement element : SpotAssetPairsColumn) {
            assetPairs.add(getTextWhenVisible(element));
        }
        return assetPairs;
    }

    /**
     * Checks whether the current Spot category has at least one trading pair.
     *
     * Used in SPOT-001 to ensure no category is empty (unless spec allows it).
     */
    public boolean hasTradingPairsInCurrentCategory() {
        return !SpotAssetPairsColumn.isEmpty();
    }

    /**
     * Validates basic trading pair symbol formatting for all visible pairs.
     *
     * Example expected format: "BTC-USDT", "ETH-USDT"
     * Regex explanation:
     *   [A-Z0-9]+ - [A-Z0-9]+
     *   (alphanumeric base + dash + alphanumeric quote)
     *
     * Used in SPOT-002 as part of structural & data validation.
     */
    public boolean arePairSymbolsInCorrectFormat() {
        for (WebElement pairElement : SpotAssetPairsColumn) {
            String text = getTextWhenVisible(pairElement);
            if (!text.matches("[A-Z0-9]+-[A-Z0-9]+")) {
                return false;
            }
        }
        return true;
    }

    /**
     * Generic helper to check if a numeric/text column contains
     * at least one non-empty value for the current category.
     *
     * Used by price/change/high/low column checks below.
     */
    private boolean isColumnHavingData(List<WebElement> columnCells) {
        for (WebElement cell : columnCells) {
            if (!getTextWhenVisible(cell).isEmpty()) {
                return true;
            }
        }
        return false;
    }

    /**
     * SPOT-002 extension:
     * Validates that each "Last 7 days" / base volume cell
     * contains SVG indicators (mini charts/sparkline).
     *
     * This method:
     *   - Scrolls cells into view if needed
     *   - Uses a BasePage helper hasSvgInside(cell) to detect inline graphs
     */
    public boolean isBaseVolumeColumnHavingSvgIndicators() {

        for (WebElement cell : SpotLast7Column) {

            // Ensure the cell is visible in viewport
            if (!cell.isDisplayed()) {
                scrollIntoView(cell);
            }

            // Check whether SVG-based indicator exists inside the cell
            if (!hasSvgInside(cell)) {
                System.out.println("No SVG found inside: " + cell.getText());
                return false;
            }
        }

        // Only returns true if every cell contains required SVG markup
        return true;
    }

    /**
     * Confirms that the Price column contains at least one non-empty value.
     */
    public boolean isPriceDataPresent() {
        return isColumnHavingData(SpotPriceColumn);
    }

    /**
     * Confirms that the 24h change column contains data.
     */
    public boolean is24HrChangeDataPresent() {
        return isColumnHavingData(Spot24HrChangeColumn);
    }

    /**
     * Confirms that the High column contains data.
     */
    public boolean isHighDataPresent() {
        return isColumnHavingData(SpotHighColumn);
    }

    /**
     * Confirms that the Low column contains data.
     */
    public boolean isLowDataPresent() {
        return isColumnHavingData(SpotLowColumn);
    }

    /**
     * Validates that the Spot column headers match the expected list
     * (in both text and order).
     *
     * Used in SPOT-002:
     *   - Ensures all required columns are present and correctly labeled.
     */
    public boolean validateSpotColumnHeaders(List<String> expectedHeaders) {
        List<String> actualHeaders = getSpotColumnHeaderText();

        if (actualHeaders.size() < expectedHeaders.size()) {
            System.out.println("Header count mismatch. Expected: "
                    + expectedHeaders.size() + " but found: " + actualHeaders.size());
            return false;
        }

        for (int i = 0; i < expectedHeaders.size(); i++) {
            if (!actualHeaders.get(i).equalsIgnoreCase(expectedHeaders.get(i))) {
                System.out.println("Header mismatch at index " + i
                        + " -> Expected: " + expectedHeaders.get(i)
                        + ", Found: " + actualHeaders.get(i));
                return false;
            }
        }
        return true;
    }
}
