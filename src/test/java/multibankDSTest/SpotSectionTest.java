package multibankDSTest;

import java.util.Arrays;
import java.util.List;

import org.testng.Assert;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import multibank.DaminiSinghAssignment.Core.DriverManager;
import multibank.DaminiSinghAssignment.PageObjects.SpotSectionPage;

public class SpotSectionTest extends BaseTest {

    private SpotSectionPage spotSectionPage;

    @BeforeMethod(alwaysRun = true)
    public void initPage() {
        // BaseTest @BeforeMethod already:
        // - creates driver via DriverFactory
        // - stores it in DriverManager (ThreadLocal)
        // - maximizes window
        // - sets implicit wait
        // - navigates to baseUrl

        spotSectionPage = new SpotSectionPage(DriverManager.getDriver());
    }

    /**
     * SPOT-001
     * Home page loaded; navigate to Spot section.
     * Switch through all Spot categories; scrape trading pairs per category.
     * Each category lists trading pairs; no category is empty.
     */
    @Test(priority = 1, description = "SPOT-001: Verify each Spot category has at least one trading pair")
    public void verifySpotCategoriesHaveTradingPairs() {
        Assert.assertTrue(
                spotSectionPage.isSpotSectionVisible(),
                "Spot section is not visible on the homepage."
        );

        List<String> categories = spotSectionPage.getSpotCategoryButtonText();
        Assert.assertFalse(categories.isEmpty(), "No Spot categories found.");

        for (String category : categories) {
            System.out.println("Checking category: " + category);

            // Skip Favorites if you don't want to enforce pairs there
            if ("Favorites".equalsIgnoreCase(category)) {
                continue;
            }

            spotSectionPage.clickSpotCategoryByName(category);

            Assert.assertTrue(
                    spotSectionPage.hasTradingPairsInCurrentCategory(),
                    "Category '" + category + "' has no trading pairs listed."
            );

            List<String> pairs = spotSectionPage.getVisibleTradingPairs();
            System.out.println("Pairs for category '" + category + "': " + pairs);

            Assert.assertFalse(
                    pairs.isEmpty(),
                    "Trading pair list is empty for category: " + category
            );
        }
    }

    /**
     * SPOT-002
     * Validate structural elements: column headers, pair symbol formatting, price data.
     */
    @Test(priority = 2, description = "SPOT-002: Validate Spot table structure and data")
    public void verifySpotTableStructureAndData() {
        List<String> expectedHeaders = Arrays.asList(
                "Pair",
                "Price",
                "24h Change",
                "High",
                "Low",
                "Last 7 days"
        );

        Assert.assertTrue(
                spotSectionPage.validateSpotColumnHeaders(expectedHeaders),
                "Spot column headers do not match expected spec."
        );

        Assert.assertTrue(
                spotSectionPage.arePairSymbolsInCorrectFormat(),
                "One or more pair symbols are not in the expected format (e.g., BTC/USDT)."
        );

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

        Assert.assertTrue(
                spotSectionPage.isBaseVolumeColumnHavingSvgIndicators(),
                "Base Volume / Last 7 column has no data."
        );
    }

    /**
     * Negative/format test:
     * Proves that validateSpotColumnHeaders() correctly FAILS when spec is wrong.
     */
    @Test(
        priority = 3,
        description = "Negative: header validation should fail for wrong spec",
        expectedExceptions = AssertionError.class
    )
    public void verifySpotHeaderValidationFailsForWrongSpec() {

        // Deliberately wrong spec: missing columns + wrong order/name
        List<String> wrongHeaders = Arrays.asList(
                "Symbol",
                "Last Price",
                "High",
                "Low"
        );

        // We EXPECT this assertion to fail, triggering AssertionError,
        // which is why expectedExceptions = AssertionError.class
        Assert.assertTrue(
                spotSectionPage.validateSpotColumnHeaders(wrongHeaders),
                "Validation should fail for incorrect header spec but it passed."
        );
    }
}