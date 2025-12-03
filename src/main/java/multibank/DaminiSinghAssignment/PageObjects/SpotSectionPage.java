package multibank.DaminiSinghAssignment.PageObjects;

import java.util.ArrayList;
import java.util.List;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;

public class SpotSectionPage extends BasePage{
	
	public SpotSectionPage(WebDriver driver) {
		
		super(driver);
	}
	
	
	//Spot locators
	
	@FindBy (xpath = "//div[contains(@class,'home_home')]//div[.//span[normalize-space()='Spot']]")
	private WebElement SpotCategoryDiv;
	
	@FindBy (xpath = "//div[contains(@class,'home_home')]//div[.//span[normalize-space()='Spot']]//div[contains(@class, 'style_list')]/button")
	private List<WebElement> SpotCategoryButtons;
	
	@FindBy (xpath = "//tr[contains(@class,'style_headers')]/th[.//span]")
	private List<WebElement> spotColumnHeaders;
	
	@FindBy (xpath = "//div[contains(@class,'home_home')]//div[.//span[normalize-space()='Spot']]//div[contains(@class, 'style_table')]//div[contains(@class, 'asset-list_pair')]")
	private List<WebElement> SpotAssetPairsColumn;
	
	@FindBy (xpath = "//div[contains(@class,'home_home')]//div[.//span[normalize-space()='Spot']]//div[contains(@class, 'style_table')]//td[contains(@id, 'price-td')]")
	private List<WebElement> SpotPriceColumn;
	
	@FindBy (xpath = "//div[contains(@class,'home_home')]//div[.//span[normalize-space()='Spot']]//div[contains(@class, 'style_table')]//td[contains(@id, 'change_in_price-td')]")
	private List<WebElement> Spot24HrChangeColumn;
	
	@FindBy (xpath = "//div[contains(@class,'home_home')]//div[.//span[normalize-space()='Spot']]//div[contains(@class, 'style_table')]//td[contains(@id, 'high')]")
	private List<WebElement> SpotHighColumn;
	
	@FindBy (xpath = "//div[contains(@class,'home_home')]//div[.//span[normalize-space()='Spot']]//div[contains(@class, 'style_table')]//td[contains(@id, 'low')]")
	private List<WebElement> SpotLowColumn;
	
	@FindBy (xpath = "//div[contains(@class,'home_home')]//div[.//span[normalize-space()='Spot']]//div[contains(@class, 'style_table')]//td[contains(@id, 'base_volume')]")
	private List<WebElement> SpotLast7Column;
	
		
	// Get all Spot category button names
    public List<String> getSpotCategoryButtonText() {
        List<String> spotCatButtonNames = new ArrayList<>();
        for (WebElement element : SpotCategoryButtons) {
            spotCatButtonNames.add(getTextWhenVisible(element));
        }
        return spotCatButtonNames;
    }
	
	
    // Get all Spot column headers
    public List<String> getSpotColumnHeaderText() {
        List<String> spotColumnNames = new ArrayList<>();
        for (WebElement element : spotColumnHeaders) {
            spotColumnNames.add(getTextWhenVisible(element));
        }
        return spotColumnNames;
    }
	
    // Spot section visible
    public boolean isSpotSectionVisible() {
        try {
            return SpotCategoryDiv.isDisplayed();
        } catch (Exception e) {
            return false;
        }
    }
	
    // Click a category by name
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
	
    // Trading pairs for current category
    public List<String> getVisibleTradingPairs() {
        List<String> assetPairs = new ArrayList<>();
        for (WebElement element : SpotAssetPairsColumn) {
            assetPairs.add(getTextWhenVisible(element));
        }
        return assetPairs;
    }
	
    // Check that current category is not empty
    public boolean hasTradingPairsInCurrentCategory() {
        return !SpotAssetPairsColumn.isEmpty();
    }
	
	/**In your test you can loop over getSpotCategoryButtonText(), 
	 * click each category, and assert that hasTradingPairsInCurrentCategory() is true and getVisibleTradingPairs() size > 0.**/
	
    // Validate pair symbol basic format (e.g., BTC/USDT)
    public boolean arePairSymbolsInCorrectFormat() {
        for (WebElement pairElement : SpotAssetPairsColumn) {
            String text = getTextWhenVisible(pairElement);
            if (!text.matches("[A-Z0-9]+-[A-Z0-9]+")) {
                return false;
            }
        }
        return true;
    }
	
    // Generic helper to check a column has some non-empty values
    private boolean isColumnHavingData(List<WebElement> columnCells) {
        for (WebElement cell : columnCells) {
            if (!getTextWhenVisible(cell).isEmpty()) {
                return true;
            }
        }
        return false;
    }
    
    public boolean isBaseVolumeColumnHavingSvgIndicators() {

        //List<WebElement> cells = driver.findElements(SpotLast7Column);

        for (WebElement cell : SpotLast7Column) {

            // First check inside cell for visibility
            if (!cell.isDisplayed()) {
                scrollIntoView(cell); // BasePage helper if you added it
            }

            // Check whether SVG elements exist
            if (!hasSvgInside(cell)) {
                System.out.println("No SVG found inside: " + cell.getText());
                return false;
            }
        }

        return true; // every cell contains path or g
    }
	
    // Check all required numeric columns have data
    public boolean isPriceDataPresent() {
        return isColumnHavingData(SpotPriceColumn);
    }

    public boolean is24HrChangeDataPresent() {
        return isColumnHavingData(Spot24HrChangeColumn);
    }

    public boolean isHighDataPresent() {
        return isColumnHavingData(SpotHighColumn);
    }

    public boolean isLowDataPresent() {
        return isColumnHavingData(SpotLowColumn);
    }

	
    // Validate headers vs expected list
    public boolean validateSpotColumnHeaders(List<String> expectedHeaders) {
        List<String> actualHeaders = getSpotColumnHeaderText();

        if (actualHeaders.size() != expectedHeaders.size()) {
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
