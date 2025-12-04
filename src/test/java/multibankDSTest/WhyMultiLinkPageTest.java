package multibankDSTest;

import java.io.IOException;
import java.util.List;
import java.util.Map;

import org.testng.Assert;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import multibank.DaminiSinghAssignment.Core.DriverManager;
import multibank.DaminiSinghAssignment.PageObjects.HomePage;
import multibank.DaminiSinghAssignment.PageObjects.WhyMultiLinkPage;
import multibank.DaminiSinghAssignment.Utilities.TestDataLoader;

/**
 * Test class for validating the "Why MultiBank" page.
 * 
 * Covers assignment requirements:
 *   - ABOUT-001: Component visibility validation
 *   - ABOUT-002: Text assertion against external JSON
 * 
 * Tests follow POM structure and reuse BaseTest WebDriver handling.
 */
public class WhyMultiLinkPageTest extends BaseTest {

    private HomePage homePage;
    private WhyMultiLinkPage whyMultiLinkPage;

    /**
     * Executed before every test method.
     * 
     * BaseTest already:
     *   - Initializes WebDriver
     *   - Manages ThreadLocal driver (via DriverManager)
     *   - Maximizes window, sets timeouts, navigates to base URL
     *
     * This method:
     *   - Instantiates HomePage + WhyMultiLinkPage POM objects
     *   - Navigates via top menu: HomePage → About Us → Why Multibank?
     *   - Waits for WhyMultilink page to finish loading
     */
    @BeforeMethod(alwaysRun = true)
    public void navigateToWhyMultiLink() {

        homePage = new HomePage(DriverManager.getDriver());
        whyMultiLinkPage = new WhyMultiLinkPage(DriverManager.getDriver());

        // Navigate to "Why Multibank?" using hover + submenu click
        homePage.openMenuItem("About Us", "Why Multibank?");

        // Wait for page's hero/banner section to fully load
        whyMultiLinkPage.waitForPageToLoad();
    }

    /**
     * ABOUT-001
     * 
     * Objective:
     *   Verify that all major components of the Why MultiBank page 
     *   (headings, subheadings, advantages, CTAs) are visible.
     * 
     * How:
     *   - Each component is scrolled into view via POM logic
     *   - All elements must be displayed for the test to pass
     * 
     * Expected:
     *   - All documented UI sections return true → test passes
     */
    @Test
    public void ABOUT_001_verifyAllComponentsVisible() {
        boolean allVisible = whyMultiLinkPage.areAllComponentsVisible();
        Assert.assertTrue(allVisible, "Some Why MultiLink components are not visible.");
    }

    /**
     * ABOUT-002
     * 
     * Objective:
     *   Validate all UI text against the expected values stored in a JSON file.
     * 
     * JSON contains:
     *    - mainCardsHeadings
     *    - mainCardsSubHeadings
     *    - ourAdvantagesHeadings
     *    - buttons (CTA labels)
     * 
     * Strategy:
     *   - Headings & CTA buttons → strict list equality
     *   - Long descriptive paragraphs → order-independent presence match
     *     (to avoid failures due to hidden/lazy-loaded duplicates)
     *
     * Expected:
     *   - All text matches exactly when whitespace is normalized
     */
    @Test
    public void ABOUT_002_verifyTextsAgainstJson() throws IOException {

        // Load expected values from external JSON using Jackson (via TestDataLoader)
        Map<String, List<String>> expectedTexts =
                TestDataLoader.loadWhyMultiLinkExpectedTexts();

        // ================================
        // 1) MAIN CARD HEADINGS (strict)
        // ================================
        Assert.assertEquals(
                whyMultiLinkPage.getMainCardsHeadingTexts(),
                expectedTexts.get("mainCardsHeadings"),
                "Main card headings mismatch"
        );

        // ==========================================
        // 2) MAIN CARD SUBHEADINGS (presence match)
        // ==========================================
        List<String> actualMainSubs   = whyMultiLinkPage.getMainCardSubHeadingTexts();
        List<String> expectedMainSubs = expectedTexts.get("mainCardsSubHeadings");

        System.out.println("=== ACTUAL MAIN SUBHEADINGS ===");
        actualMainSubs.forEach(System.out::println);

        System.out.println("=== EXPECTED MAIN SUBHEADINGS ===");
        expectedMainSubs.forEach(System.out::println);

        for (String exp : expectedMainSubs) {
            Assert.assertTrue(
                    actualMainSubs.contains(exp),
                    "Expected main subheading not found on page: " + exp
            );
        }

        // ==================================
        // 3) ADVANTAGE HEADINGS (strict)
        // ==================================
        Assert.assertEquals(
                whyMultiLinkPage.getOurAdvantagesCardHeadingTexts(),
                expectedTexts.get("ourAdvantagesHeadings"),
                "Our Advantages headings mismatch"
        );

        // ==================================
        // 4) CTA BUTTONS (strict match)
        //    Duplicate removal is handled in POM
        // ==================================
        Assert.assertEquals(
                whyMultiLinkPage.getWhyMultiLinkButtonTexts(),
                expectedTexts.get("buttons"),
                "CTA button texts mismatch"
        );
    }
}
