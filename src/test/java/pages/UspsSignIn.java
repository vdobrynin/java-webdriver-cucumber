package pages;

import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;

public class UspsSignIn extends Page {
    @FindBy(id = "new-account-button")
    private WebElement signUpButton;

    @FindBy(xpath = "//button[normalize-space()='Sign In']")
    private WebElement signInButton;

    @FindBy(xpath = "//input[@type='text' or @type='email']")
    private WebElement usernameInput;

    @FindBy(xpath = "//input[@type='password']")
    private WebElement passwordInput;

    public boolean isSignUpPossible() {
        return signUpButton.isDisplayed() && signUpButton.isEnabled();
    }

    public boolean isSignInRequired() {
        return (
                usernameInput.isDisplayed() &&
                        passwordInput.isDisplayed() &&
                        signInButton.isDisplayed()
        );
    }
}
