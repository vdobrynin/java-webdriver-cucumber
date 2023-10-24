package pages;

import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.*;

public class QuoteResult extends Page {

    @FindBy(xpath = "//div[@id='quotePageResult']//section")
    private WebElement sectionResult;
    @FindBy(xpath = "//b[@name='agreedToPrivacyPolicy']")
    private WebElement privacyPolicy;

    public String getSectionResult() { return sectionResult.getText(); }

    public String getPrivacyPolicy() {
        return privacyPolicy.getText();
    }
}
