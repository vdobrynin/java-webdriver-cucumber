package pages;

import org.openqa.selenium.Keys;
import org.openqa.selenium.TimeoutException;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.FindBy;

import static support.TestContext.getDriver;

public class UspsSignIn extends Page {

    @FindBy(xpath = "//*[@id='sign-up-button']")
    private WebElement signUpButton;
    @FindBy(xpath = "//*[@id='btn-submit']")
    private WebElement signInButton;
    @FindBy(xpath = "//*[@id='error-username']")
    private WebElement usernameError;

    public boolean isSignUpPossible() { return signUpButton.isDisplayed() && signUpButton.isEnabled(); }

    public boolean isSignInRequired() {

        Actions action = new Actions(getDriver());
        signInButton.click();

        try {
            action.moveToElement(signInButton)
                .click(signInButton)
                .sendKeys(Keys.ENTER)
                .perform();

            return true;
        } catch (TimeoutException e) {
            return false;
        }
    }
}
