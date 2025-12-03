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

public class WhyMultiLinkPageTest extends BaseTest {

    private HomePage homePage;
    private WhyMultiLinkPage whyMultiLinkPage;

    @BeforeMethod(alwaysRun = true)
    public void navigateToWhyMultiLink() {
        // BaseTest @BeforeMethod has already:
        // - created WebDriver via DriverFactory
        // - put it into DriverManager (ThreadLocal)
        // - maximized window, set waits, navigated to baseUrl

        homePage = new HomePage(DriverManager.getDriver());
        whyMultiLinkPage = new WhyMultiLinkPage(DriverManager.getDriver());

        // From homepage → About Us → Why Multibank?
        homePage.openMenuItem("About Us", "Why Multibank?");
        whyMultiLinkPage.waitForPageToLoad();
    }

    /**
     * ABOUT-001
     * From any page, open About → Why MultiLink.
     * Wait for main components/sections; enumerate visible components.
     * All documented components are present and visible.
     */
    @Test
    public void ABOUT_001_verifyAllComponentsVisible() {
        boolean allVisible = whyMultiLinkPage.areAllComponentsVisible();
        Assert.assertTrue(allVisible, "Some Why MultiLink components are not visible.");
    }

    /**
     * ABOUT-002
     * For each component key from JSON, compare UI text to expected.
     */
    @Test
    public void ABOUT_002_verifyTextsAgainstJson() throws IOException {
        // Load JSON from resources
        Map<String, List<String>> expectedTexts =
                TestDataLoader.loadWhyMultiLinkExpectedTexts();

        Assert.assertEquals(
                whyMultiLinkPage.getMainCardsHeadingTexts(),
                expectedTexts.get("mainCardsHeadings"),
                "Main card headings mismatch"
        );

        Assert.assertEquals(
                whyMultiLinkPage.getMainCardSubHeadingTexts(),
                expectedTexts.get("mainCardsSubHeadings"),
                "Main card subheadings mismatch"
        );

        Assert.assertEquals(
                whyMultiLinkPage.getOurAdvantagesCardHeadingTexts(),
                expectedTexts.get("ourAdvantagesHeadings"),
                "Our Advantages headings mismatch"
        );

        Assert.assertEquals(
                whyMultiLinkPage.getOurAdvantagesCardSubHeadingTexts(),
                expectedTexts.get("ourAdvantagesSubHeadings"),
                "Our Advantages subheadings mismatch"
        );

        Assert.assertEquals(
                whyMultiLinkPage.getWhyMultiLinkButtonTexts(),
                expectedTexts.get("buttons"),
                "CTA button texts mismatch"
        );
    }
}
