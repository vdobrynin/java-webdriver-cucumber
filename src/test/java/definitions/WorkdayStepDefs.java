package definitions;

import io.cucumber.java.en.And;
import io.cucumber.java.en.Then;
import org.openqa.selenium.By;
import org.openqa.selenium.SearchContext;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Duration;
import java.util.List;
import java.util.Random;
import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;
import static org.openqa.selenium.support.ui.ExpectedConditions.visibilityOfElementLocated;
import static support.TestContext.*;

public class WorkdayStepDefs {

    @And("I select any position")
    public void iSelectAnyPosition() {

        fluentWait
            .until(ExpectedConditions.presenceOfAllElementsLocatedBy(By.xpath("//*[@data-automation-id='jobTitle']")));
        List<WebElement> jobs = getDriver()
            .findElements(By.xpath("//*[@data-automation-id='jobTitle']"));
        int index = new Random()
            .nextInt(jobs.size());      // each time it would to be a random
        jobs.get(index)
            .click();                   // choose the job (WebElement)
    }

    @And("I go with Apply with LinkedIn")
    public void iGoWithApplyWithLinkedIn() {
//                                                // click on 'apply' to go to 'applywithlinkedin'
        fluentWait.until(ExpectedConditions.presenceOfElementLocated(By
            .cssSelector(".css-e46bon")));
        getDriver()
            .findElement(By.cssSelector(".css-e46bon"))
            .click();

        fluentWait.until(ExpectedConditions.presenceOfElementLocated(By
            .xpath("(//iframe[@title='applyWithLinkedIn'])[1]")));
        WebElement outerFrame = getDriver()
            .findElement(By.xpath("(//iframe[@title='applyWithLinkedIn'])[1]"));
        getDriver()
            .switchTo()
            .frame(outerFrame);

        WebElement linkedInFrame = getDriver()
            .findElement(By.xpath("//iframe[contains(@id,'xdOrigin')]"));
        getDriver()
            .switchTo()
            .frame(linkedInFrame);

        By linkedInButton = By
            .xpath("//*[@id='apply-with-linkedin']/span");
        new WebDriverWait(getDriver(), Duration.ofSeconds(10))
            .until(visibilityOfElementLocated(linkedInButton));
        getDriver()
            .findElement(linkedInButton)
            .click();
    }

    @Then("I verify login window opens")
    public void iVerifyLoginWindowOpens() throws InterruptedException {

        Thread.sleep(3000);
        Set<String> allWindows = getDriver().getWindowHandles();
//                                                                    // switch to last window
        for (String window : allWindows) {
            getDriver()
                .switchTo()
                .window(window);
        }

        String actual = getDriver()
            .findElement(By.xpath("(//form[@class='login__form']" +
                "//label[normalize-space()='Email or Phone'][contains(.,'Email or Phone')])[1]"))
            .getText();
        assertThat(actual)
            .contains("Email or Phone");
        System.out.println(actual);

        String name = getDriver()
            .findElement(By.xpath("(//form[@class='login__form']" +
                "//button[normalize-space()='Sign in'][contains(.,'Sign in')])[1]"))
            .getText();
        assertThat(name)
            .contains("Sign in");
        System.out.println(name);
    }

    @And("I select any tech position")
    public void iSelectAnyTechPosition() throws InterruptedException {

        fluentWait
            .until(ExpectedConditions.presenceOfAllElementsLocatedBy(By.xpath("//a[@data-cy='card-title-link']")));
        List<WebElement> jobs = getDriver()
            .findElements(By.xpath("//a[@data-cy='card-title-link']"));
        int index = new Random()
            .nextInt(jobs.size());
        getExecutor()
            .executeScript("arguments[0].click();", jobs.get(index));

        Thread.sleep(3000);
        Set<String> allWindows = getDriver().getWindowHandles();
//                                                                    // switch to last window
        for (String window : allWindows) {
            getDriver()
                .switchTo()
                .window(window);
        }
    }

    @And("I go with Apply")
    public void iGoWithApply() throws InterruptedException {

        Thread.sleep(3000);
        getDriver()
            .findElement(By.cssSelector("#dhiJobHeaderRedesign div[class='flex flex-row flex-wrap mt-2 md:flex-nowrap']"))
            .isDisplayed();
        Thread.sleep(1000);
        getDriver()
            .findElement(By.cssSelector("#buttonsDiv"))
            .isSelected();
        Thread.sleep(1000);
        getDriver()
            .findElement(By.cssSelector("#buttons"))
            .isSelected();
/*
                                                // This is Element inside single shadow DOM.
*/
        String cssSelectorForHost1 = "apply-button-wc[class$='hydrated']";
        Thread.sleep(3000);
        SearchContext shadow = getDriver()
            .findElement(By.cssSelector("apply-button-wc[class$='hydrated']"))
            .getShadowRoot();
        Thread.sleep(3000);
        shadow
            .findElement(By.cssSelector(".btn.btn-primary"))
            .click();
        Thread.sleep(3000);
    }

    @Then("I verify opens login window")
    public void iVerifyOpensLoginWindow() {

        fluentWait
            .until(visibilityOfElementLocated(By.xpath("(//a[normalize-space()='Register'])[1]")));
        WebElement element = getDriver()
            .findElement(By.xpath("(//a[normalize-space()='Register'])[1]"));
        String text = element
            .getText();
        assertThat(text)
            .contains("Register");
        System.out.println(text);

        WebElement element1 = getDriver()
            .findElement(By.xpath("//div[@class='button-container sc-login-form']" +
                "//login-dhi-button[@id='signin']/button[@type='button']"));
        String text2 = element1
            .getText();
        assertThat(text2)
            .contains("Log in");
        System.out.println(text2);
    }
}
