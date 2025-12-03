package multibank.DaminiSinghAssignment.PageObjects;

import java.util.List;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;

public class WhyMultiLinkPage extends BasePage {
	
	public WhyMultiLinkPage(WebDriver driver) {
		
		super(driver);
	}
	
		
	@FindBy (xpath = "//div[contains(@class,'home_main-cards')]//div[contains(@class, 'main-cards_main-card')]//h2")
	private List<WebElement> MainCardsHeading;
	
	@FindBy (xpath = "//div[contains(@class,'home_main-cards')]//div[contains(@class, 'main-cards_main-card')]//*[contains(@class, 'secondary')]")
	private List<WebElement> MainCardSubHeading;
	
	@FindBy (xpath = "//div[contains(@class,'advantages_card-text')]//h3")
	private List<WebElement> OurAdvantagesCardHeading;
	
	@FindBy (xpath = "//div[contains(@class,'advantages_card-text')]//p")
	private List<WebElement> OurAdvantagesCardSubHeading;
	
	@FindBy (xpath = "//button[contains(@class,'button_btn')]")
	private List<WebElement> WhyMultiLinkButtons;
	
	
	//This ensures the full WhyMultilink page is rendered before tests run.
	public void waitForPageToLoad() {
	    waitForVisibility(
	        By.xpath("(//div[contains(@class,'home_main-cards')]//div[contains(@class, 'main-cards_main-card')]//h2)[1]")
	    );
	}
	
	public boolean areAllComponentsVisible() {

	    waitForPageToLoad();

	    return areAllDisplayedWithScroll(MainCardsHeading)
	        || areAllDisplayedWithScroll(MainCardSubHeading)
	        || areAllDisplayedWithScroll(OurAdvantagesCardHeading)
	        || areAllDisplayedWithScroll(OurAdvantagesCardSubHeading)
	        || areAllDisplayedWithScroll(WhyMultiLinkButtons);
	}

	private String normalizeText(String text) {
	    return text.trim().replaceAll("\\s+", " ");
	}
	
	public List<String> getMainCardsHeadingTexts() {
	    waitForPageToLoad();
	    return MainCardsHeading.stream()
	            .map(e -> normalizeText(e.getText()))
	            .toList();
	}

	public List<String> getMainCardSubHeadingTexts() {
	    waitForPageToLoad();
	    return MainCardSubHeading.stream()
	            .map(e -> normalizeText(e.getText()))
	            .toList();
	}

	public List<String> getOurAdvantagesCardHeadingTexts() {
	    waitForPageToLoad();
	    return OurAdvantagesCardHeading.stream()
	            .map(e -> normalizeText(e.getText()))
	            .toList();
	}

	public List<String> getOurAdvantagesCardSubHeadingTexts() {
	    waitForPageToLoad();
	    return OurAdvantagesCardSubHeading.stream()
	            .map(e -> normalizeText(e.getText()))
	            .toList();
	}

	public List<String> getWhyMultiLinkButtonTexts() {
	    waitForPageToLoad();
	    return WhyMultiLinkButtons.stream()
	            .map(e -> normalizeText(e.getText()))
	            .toList();
	}

	

}
