package definitions;

import cucumber.api.java.en.And;
import cucumber.api.java.en.Then;
import io.github.sukgu.Shadow;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Duration;
import java.util.List;
import java.util.Random;

import static org.assertj.core.api.Assertions.assertThat;
import static org.openqa.selenium.support.ui.ExpectedConditions.visibilityOfElementLocated;
import static support.TestContext.*;

public class WorkdayStepDefs {

    @And("I select any position")
    public void iSelectAnyPosition() {
        List<WebElement> jobs = getDriver()
            .findElements(By.xpath("//*[@data-automation-id='jobTitle']"));
        int index = new Random()
            .nextInt(jobs.size());  // each time it would be random
        jobs.get(index)
            .click();     // choose the job (WebElement)
    }

    @And("I go with Apply with LinkedIn")
    public void iGoWithApplyWithLinkedIn() throws InterruptedException {

        getDriver()
            .findElement(By.xpath("(//a[@data-automation-id='adventureButton'])[1]"))
            .isDisplayed();
        getDriver()
            .findElement(By.xpath("(//a[@data-automation-id='adventureButton'])[1]"))
            .click();

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
        Thread.sleep(2000);

        By linkedInButton = By
            .xpath("//*[@id='apply-with-linkedin']/span");
        new WebDriverWait(getDriver(), Duration.ofSeconds(7))
            .until(visibilityOfElementLocated(linkedInButton));
        getDriver()
            .findElement(linkedInButton)
            .click();
    }

    @Then("I verify login window opens")
    public void iVerifyLoginWindowOpens() throws InterruptedException {

        Thread.sleep(3);
        for (String handle : getDriver().getWindowHandles()) {
            getDriver()
                .getWindowHandle()
                .trim()
                .equalsIgnoreCase("LinkedIn Login");
            getDriver()
                .getWindowHandle()
                .equalsIgnoreCase("username");
            getDriver()
                .switchTo()
                .window(handle);
        }
    }

    @And("I select any tech position")
    public void iSelectAnyTechPosition() {

        new WebDriverWait(getDriver(),
            Duration.ofSeconds(30))
            .until(ExpectedConditions.presenceOfAllElementsLocatedBy(By
                .xpath("//*[@data-cy='card-title-link']")));
        List<WebElement> jobs = getDriver()
            .findElements(By.xpath("//*[@data-cy='card-title-link']"));
        int index = new Random()
            .nextInt(jobs.size());
        getExecutor().executeScript("arguments[0].click();", jobs.get(index));
    }

    @And("I go with Apply")
    public void iGoWithApply() {

        new WebDriverWait(getDriver(),
            Duration.ofSeconds(20))
            .until(ExpectedConditions.visibilityOfElementLocated(By
                .xpath("(//apply-button-wc[@class='ml-4 flex-auto md:flex-initial hydrated'])[1]")))
            .click();
    }

    @Then("I verify opens login window")
    public void iVerifyOpensLoginWindow() {

        Shadow shadow = new Shadow(driver);
        WebElement element = shadow.findElement("a[tabindex='0']");
        String text = element.getText();
        assertThat(text).contains("Register");
        System.out.println(text);

//        Shadow shadow2 = new Shadow(driver);

//        WebElement element2 = shadow2.findElement(".neutral.sc-login-dhi-button");
//        String text2 = element2.getText();
//        assertThat(text2).contains("Cancel");
//        System.out.println("-->" + text2 + "<--");
//        System.out.println(text2);

//        WebElement element2 = shadow2.findElement(String.valueOf(".accent.sc-login-dhi-button"));
//        String text2 = element2.getText();
//        assertThat(text2).contains("Log in");
//        System.out.println("-->" + text2 + "<--");
//        System.out.println(text2);
    }
}
