package multibankDSTest;

import java.util.Arrays;
import java.util.List;

import org.testng.Assert;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import multibank.DaminiSinghAssignment.Core.DriverManager;
import multibank.DaminiSinghAssignment.PageObjects.SpotSectionPage;

/**
 * Test class for the Spot section on the homepage.
 *
 * Covers assignment items:
 *  - SPOT-001: Validate that each Spot category lists trading pairs
 *  - SPOT-002: Validate structural elements and data presence in Spot table
 *
 * Uses POM pattern via SpotSectionPage and WebDriver lifecycle via BaseTest.
 */
public class SpotSectionTest extends BaseTest {

    private SpotSectionPage spotSectionPage;

    /**
     * Common setup executed before each test method.
     *
     * BaseTest @BeforeMethod (in the parent class) already:
     *  - Creates WebDriver via DriverFactory
     *  - Stores it in DriverManager (ThreadLocal)
     *  - Maximizes browser window
     *  - Sets timeouts / implicit waits
     *  - Navigates to base URL (MultiBank home)
     *
     * Here we only initialize the SpotSectionPage POM using the active driver.
     */
    @BeforeMethod(alwaysRun = true)
    public void initPage() {
        spotSectionPage = new SpotSectionPage(DriverManager.getDriver());
    }

    /**
     * SPOT-001
     *
     * Requirement:
     *  - Home page loaded; navigate to Spot tab/section.
     *  - Switch through all Spot categories; scrape trading pairs per category.
     *  - Each category lists trading pairs; no category is empty (unless spec allows).
     *
     * Implementation:
     *  - Verify Spot section is visible on homepage
     *  - Retrieve all category button names
     *  - For each category:
     *      - Click category
     *      - Assert that trading pairs exist and list is not empty
     */
    @Test(priority = 1, description = "SPOT-001: Verify each Spot category has at least one trading pair")
    public void verifySpotCategoriesHaveTradingPairs() {

        // Ensure Spot section is present before interacting
        Assert.assertTrue(
                spotSectionPage.isSpotSectionVisible(),
                "Spot section is not visible on the homepage."
        );

        // Get all category names (e.g., Top, New, Favorites)
        List<String> categories = spotSectionPage.getSpotCategoryButtonText();
        Assert.assertFalse(categories.isEmpty(), "No Spot categories found.");

        for (String category : categories) {
            System.out.println("Checking category: " + category);

            // Optional: Skip Favorites if spec does not require pairs there
            if ("Favorites".equalsIgnoreCase(category)) {
                continue;
            }

            // Click category button by visible text
            spotSectionPage.clickSpotCategoryByName(category);

            // Check that category has at least one pair in DOM
            Assert.assertTrue(
                    spotSectionPage.hasTradingPairsInCurrentCategory(),
                    "Category '" + category + "' has no trading pairs listed."
            );

            // Scrape currently visible pairs for debug/logging
            List<String> pairs = spotSectionPage.getVisibleTradingPairs();
            System.out.println("Pairs for category '" + category + "': " + pairs);

            // Ensure pair list is not empty for this category
            Assert.assertFalse(
                    pairs.isEmpty(),
                    "Trading pair list is empty for category: " + category
            );
        }
    }

    /**
     * SPOT-002
     *
     * Requirement:
     *  - On Spot section.
     *  - Validate structural elements: column headers, pair symbol formatting, price data.
     *  - All required columns and UI elements visible per spec; data present for pairs.
     *
     * Implementation:
     *  - Validate column headers against expected spec
     *  - Check pair symbol format (e.g., BTC-USDT)
     *  - Check that numeric columns (Price, 24h Change, High, Low) have data
     *  - Check that "Last 7 days" / Base Volume column contains SVG indicators
     */
    @Test(priority = 2, description = "SPOT-002: Validate Spot table structure and data")
    public void verifySpotTableStructureAndData() {

        // Expected header names as per assignment spec
        List<String> expectedHeaders = Arrays.asList(
                "Pair",
                "Price",
                "24h Change",
                "High",
                "Low",
                "Last 7 days"
        );

        // Structural check: headers must match exactly in text and order
        Assert.assertTrue(
                spotSectionPage.validateSpotColumnHeaders(expectedHeaders),
                "Spot column headers do not match expected spec."
        );

        // Symbol format check: e.g., BTC-USDT, ETH-USDT
        Assert.assertTrue(
                spotSectionPage.arePairSymbolsInCorrectFormat(),
                "One or more pair symbols are not in the expected format (e.g., BTC-USDT)."
        );

        // Data presence checks for numeric columns
        Assert.assertTrue(
                spotSectionPage.isPriceDataPresent(),
                "Price column has no data."
        );

        Assert.assertTrue(
                spotSectionPage.is24HrChangeDataPresent(),
                "24h Change column has no data."
        );

        Assert.assertTrue(
                spotSectionPage.isHighDataPresent(),
                "High column has no data."
        );

        Assert.assertTrue(
                spotSectionPage.isLowDataPresent(),
                "Low column has no data."
        );

        // Visual/data check for Last 7 days / Base Volume column (SVG mini-charts)
        Assert.assertTrue(
                spotSectionPage.isBaseVolumeColumnHavingSvgIndicators(),
                "Base Volume / Last 7 days column does not contain SVG indicators."
        );
    }

    /**
     * Negative / robustness test (optional for assignment but good to demonstrate):
     *
     * Purpose:
     *  - Prove that validateSpotColumnHeaders() correctly fails
     *    when given an incorrect spec.
     *
     * Behaviour:
     *  - We deliberately pass a wrong expected header list.
     *  - validateSpotColumnHeaders() returns false.
     *  - Assert.assertTrue(false, ...) throws AssertionError.
     *  - Test is marked as PASSED because we expect an AssertionError.
     */
    @Test(
        priority = 3,
        description = "Negative: header validation should fail for wrong spec",
        expectedExceptions = AssertionError.class
    )
    public void verifySpotHeaderValidationFailsForWrongSpec() {

        // Deliberately wrong spec: missing columns and different names/order
        List<String> wrongHeaders = Arrays.asList(
                "Symbol",
                "Last Price",
                "High",
                "Low"
        );

        // We EXPECT this assertion to fail (return false), which throws AssertionError.
        // expectedExceptions = AssertionError.class tells TestNG this is the expected outcome.
        Assert.assertTrue(
                spotSectionPage.validateSpotColumnHeaders(wrongHeaders),
                "Validation should fail for incorrect header spec but it passed."
        );
    }
}
