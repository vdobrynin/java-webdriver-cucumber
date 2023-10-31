package pages;

import org.openqa.selenium.TimeoutException;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.ui.ExpectedConditions;

import static support.TestContext.fluentWait;
import static support.TestContext.getDriver;

public class UspsSignIn extends Page {

    @FindBy(xpath = "//a[@id='sign-up-button']")
    private WebElement signUpButton;
    @FindBy(xpath = "//button[@id='btn-submit']")
    private WebElement signInButton;
    @FindBy(xpath = "//span[@id='error-username']")
    private WebElement usernameError;
    private Object scenario;

    public boolean isSignUpPossible() {

        return signUpButton.isDisplayed() && signUpButton.isEnabled();
    }

    public boolean isSignInRequired() throws InterruptedException {

        Actions action = new Actions(getDriver());
        fluentWait.until(ExpectedConditions.visibilityOf(signInButton));
        signInButton.click();

        try {
            fluentWait.until(ExpectedConditions.visibilityOf(signInButton));
            action.moveToElement(signInButton)
                .click(signInButton)
                .build()
                .perform();

            fluentWait.until(ExpectedConditions.visibilityOf(usernameError));
            return true;
        } catch (TimeoutException e) {
            return false;
        }
    }
}
