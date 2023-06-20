package pages;

import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;

public class UspsTracking extends Page {

    @FindBy(xpath = "//a[contains(@href,'Registration')]")
    private WebElement signUpButton;

    public boolean isSignUpPossible() {

        return signUpButton.isDisplayed() && signUpButton.isEnabled();
    }
}
