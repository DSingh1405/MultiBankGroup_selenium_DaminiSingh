package multibank.DaminiSinghAssignment.PageObjects;

import java.util.ArrayList;
import java.util.List;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;

public class HomePage extends BasePage {


	public HomePage(WebDriver driver) {
		super(driver);
	}

	// ====== Top Navigation ======

	// All main nav items (Dashboard, Markets, Trade, Wallet, etc.)
	@FindBy(xpath = "//div[contains(@class,'style_menu-container')]//*[self::a or self::span][contains(@class,'style_menu-item')]")
	private List<WebElement> navigationItemList;

	// Sub-menu items shown in pop-over after hover (Spot, Instant Buy, Panic Sell,
	// etc.)
	//[.//text()]
	@FindBy(xpath = "//div[contains(@class,'popover')]//div[contains(@class,'style_text')]")
	private List<WebElement> navigationSubItemList;

	// Logo
	@FindBy(xpath = "//img[contains(@class, 'style_logo')]")
	private WebElement mbLogo;

	// ====== Market Banner (Carousel) ======

	@FindBy(css = ".slick-list")
	private WebElement marketBanner;

	@FindBy(css = ".slick-list div[class*='slick-active'] span")
	private List<WebElement> activeBannerSpans;
	
	@FindBy(xpath = "//button[contains(@class,'style_next')]")
	private WebElement bannerNextArrow;

	// ====== App Store Buttons ======

	@FindBy(xpath = "//div[contains(@class,'app-button')][.//img[@alt='app-store']]")
	private WebElement appleAppStoreButton;

	@FindBy(xpath = "//div[contains(@class,'app-button')][.//img[@alt='google-play']]")
	private WebElement googlePlayStoreButton;

	// ====== Page-level helpers ======

	public String getPageTitle() {
		return driver.getTitle();
	}

	// Reusable actions:

	public List<String> getNavigationItemTexts() {
		List<String> navMainItems = new ArrayList<>();
		for (WebElement element : navigationItemList) {
			navMainItems.add(element.getText().trim());
		}
		return navMainItems;
	}

	public List<String> getNavigationSubItemTexts() {
		List<String> navMainSubItems = new ArrayList<>();
		for (WebElement element : navigationSubItemList) {
			navMainSubItems.add(element.getText().trim());
		}
		return navMainSubItems;
	}

	/**
	 * Clicks a top navigation item by visible text (e.g. "Markets", "Trade",
	 * "Wallet")
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

	/** Hovers on any top navigation item by visible text */
	public void hoverNavigationItem(String name) {
	    for (WebElement element : navigationItemList) {
	        if (element.getText().trim().equalsIgnoreCase(name)) {
	            actions.moveToElement(element).perform();
	            return;
	        }
	    }
	    throw new RuntimeException("Navigation item not found to hover: " + name);
	}
	
	public String getNavigationItemHref(String name) {
	    for (WebElement element : navigationItemList) {
	        if (element.getText().trim().equalsIgnoreCase(name)) {
	            return element.getAttribute("href");
	        }
	    }
	    throw new RuntimeException("Navigation item not found: " + name);
	}
	
	public List<String> getActiveBannerTexts() {
	    List<String> texts = new ArrayList<>();
	    for (WebElement span : activeBannerSpans) {
	        if (span.isDisplayed()) {   // optional, but safer with moving elements
	            texts.add(span.getText().trim());
	        }
	    }
	    return texts;
	}
	
	public WebElement getMarketBanner() {
	    return marketBanner;
	}

	/**
	 * Clicks a sub-menu item under the popover (e.g. "Spot", "Instant Buy", "Panic
	 * Sell")
	 */
	
//	public List<String> getNavigationSubItemTexts() {
//		List<String> navMainSubItems = new ArrayList<>();
//		for (WebElement element : navigationSubItemList) {
//			navMainSubItems.add(element.getText().trim());
//		}
//		return navMainSubItems;
//	}
	
	public void clickSubMenuItem(String name) {
		for (WebElement element : navigationSubItemList) {
			String fullText = element.getText().trim();
			String firstLine = fullText.split("\n")[0];
			System.out.println(firstLine); 
			//System.out.println(element.getText());
			if (element.getText().trim().contains(name)) {
				element.click();
				return;
			}
		}
		throw new RuntimeException("Sub-menu item not found: " + name);
	}

	// Clicks the Apple App Store button
	public void clickAppleAppStore() {
		appleAppStoreButton.click();
	}

	// Clicks the Google Play Store button
	public void clickGooglePlayStore() {
		googlePlayStoreButton.click();
	}
	
	// Clicks the Next Banner button	
	public void clickNextBanner() {
		bannerNextArrow.click();
	}
	
	//Click on any SubMenu Item
	public void openMenuItem(String menuName, String subMenuName) {
	    hoverNavigationItem(menuName);
	    waitForVisibility(By.xpath("//div[contains(@class,'popover')]//div[contains(@class,'style_text')]"));
	    clickSubMenuItem(subMenuName);
	}

	// VERIFICATION METHODS
	// =================================================================

	/** Checks if the MultiBank logo is displayed */
	public boolean isLogoDisplayed() {
		try {
			return mbLogo.isDisplayed();
		} catch (Exception e) {
			return false;
		}
	}

	/**
	 * Verifies that all expected navigation items are present in the top menu.
	 * Example usage: Arrays.asList("Dashboard", "Markets", "Trade", "Wallet")
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

	/** Checks if a given sub menu option (e.g. "Spot") is present after hover */
	public boolean isSubMenuOptionPresent(String expected) {
		List<String> actual = getNavigationSubItemTexts();
		return actual.stream().anyMatch(a -> a.equalsIgnoreCase(expected));
	}
	
	public boolean isMarketBannerVisible() {
	    try {
	        return marketBanner.isDisplayed();
	    } catch (Exception e) {
	        return false;
	    }
	}
	

	/*
	 * List<String> actual = homePage.getActiveBannerTexts(); // compare with
	 * expected (names/titles/symbols)
	 */
}
