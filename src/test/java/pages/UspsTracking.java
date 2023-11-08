package pages;

import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;

public class UspsTracking extends Page {

    @FindBy(xpath = "//button[@class='button tracking-btn disabled'][contains(.,'Track')]")
//    @FindBy(xpath = "//a[contains(@href,'Registration')]")                                    // old version
    private WebElement signUpButton;

    public boolean isSignUpPossible() {
        return !signUpButton.isDisplayed() && !signUpButton.isEnabled();
    }
}
