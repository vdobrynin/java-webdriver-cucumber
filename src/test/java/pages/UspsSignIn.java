package pages;

import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.TimeoutException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.FluentWait;
import org.openqa.selenium.support.ui.Wait;

import java.time.Duration;

import static support.TestContext.getDriver;

public class UspsSignIn extends Page {

    @FindBy(xpath = "(//img[@id='usps-logo'])[1]")
    private WebElement logo;
    @FindBy(xpath = "(//a[@href='/entreg/RegistrationAction_input'][contains(.,'Sign Up Now')])[1]")
    private WebElement signUpButton;
    @FindBy(xpath = "(//button[normalize-space()='Sign In'])[1]")
    private WebElement signInButton;
    @FindBy(xpath = "(//span[@class='error-txt-blk'][normalize-space()='A Username is a required entry.'])[1]")
    private WebElement usernameError;

    public boolean isSignUpPossible() {

        return signUpButton.isDisplayed() && signUpButton.isEnabled();
    }

    public boolean isSignInRequired() throws InterruptedException {

        Wait<WebDriver> fluentWait = new FluentWait<WebDriver>(getDriver())
            .withTimeout(Duration.ofSeconds(30))
            .pollingEvery(Duration.ofSeconds(5))
            .ignoring(NoSuchElementException.class);

        fluentWait.until(ExpectedConditions.visibilityOf(signInButton));
        Actions action = new Actions(getDriver());
        action.moveToElement(signInButton)
            .click(signInButton)
            .build()
            .perform();

        try {
            action.moveToElement(signInButton)
                .click(signInButton)
                .build()
                .perform();
//            action.moveToElement(signInButton)
//                .moveToElement(logo)
//                .build()
//                .perform();
//            signInButton.click();
            fluentWait.until(ExpectedConditions.visibilityOf(usernameError));
            return true;
        } catch (TimeoutException e) {
            return false;
        }
    }
}
