package pages;

import org.openqa.selenium.TimeoutException;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Duration;

import static support.TestContext.getDriver;

public class UspsSignIn extends Page {

    @FindBy(xpath = "//a[@id='sign-up-button']")
    private WebElement signUpButton;
    @FindBy(xpath = "//button[@id='btn-submit']")
    private WebElement signInButton;
    @FindBy(xpath = "//span[@id='error-username']")
    private WebElement usernameError;


    public boolean isSignUpPossible() {

        return signUpButton.isDisplayed() && signUpButton.isEnabled();
    }

    public boolean isSignInRequired() {

        signInButton.click();
        try {
            new WebDriverWait(getDriver(), Duration.ofSeconds(5))
                .until(ExpectedConditions.visibilityOf(usernameError));
            return true;

        } catch (TimeoutException e) {
            return false;
        }
    }
}
