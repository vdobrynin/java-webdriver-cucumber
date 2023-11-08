package pages;

import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;

public class UspsTracking extends Page {

    @FindBy(xpath = "//a[contains(@href,'Registration')]")                                    // old version signin not present
    private WebElement signUpButton;

    public boolean isSignUpPossible() {

        boolean isPresent;            // sign up button
        try {
            isPresent = signUpButton.isDisplayed();
        } catch (NoSuchElementException e) {
            isPresent = false;
        }
        return isPresent;
    }
}
