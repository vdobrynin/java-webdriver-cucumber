package definitions;

import cucumber.api.java.en.And;
import cucumber.api.java.en.Then;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.util.List;
import java.util.Random;

import static org.assertj.core.api.Assertions.assertThat;
import static org.openqa.selenium.support.ui.ExpectedConditions.visibilityOfElementLocated;
import static support.TestContext.getDriver;
import static support.TestContext.getExecutor;

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
        new WebDriverWait(getDriver(), 7)
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

        List<WebElement> jobs = getDriver()
            .findElements(By.xpath("//*[@data-cy='card-title-link']"));
        int index = new Random()
            .nextInt(jobs.size());
        getExecutor().executeScript("arguments[0].click();", jobs.get(index));
    }

    @And("I go with Apply")
    public void iGoWithApply() {

        getDriver()
            .findElement(By.cssSelector(".btn.btn-primary"))
            .click();
    }

    @Then("I verify opens login window")
    public void iVerifyOpensLoginWindow() {

        String register = getDriver().findElement(By.xpath("//body[@class='modal-open']//div[@class='modal-dialog login-reg-modal-dailog']" +
            "//div[@class='modal-content']//li[@class=\"col-lg-6 col-md-6 col-sm-6 col-xs-6 ''\"]//a[contains(text(),'Register')]")).getText();
        assertThat(register).containsIgnoringCase("Register");

        String singIn = getDriver().findElement(By.xpath("//body[@class='modal-open']//div[@class='modal-dialog login-reg-modal-dailog']" +
            "//div[@class='modal-content'] //li[@class='col-lg-6 col-md-6 col-sm-6 col-xs-6 active']//a[contains(text(),'Sign In')]")).getText();
        assertThat(singIn).containsIgnoringCase("Sign In");
    }
}
