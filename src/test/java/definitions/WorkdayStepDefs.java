package definitions;

import cucumber.api.java.en.*;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.util.List;
import java.util.Random;

import static org.assertj.core.api.Assertions.assertThat;
import static support.TestContext.getDriver;

public class WorkdayStepDefs {
    @And("I select any position")
    public void iSelectAnyPosition() {
        List<WebElement> jobs = getDriver().findElements(By.xpath("//*[@data-automation-id='promptOption']"));
        int index = new Random().nextInt(jobs.size());  // each time it would be random
        jobs.get(index).click();     // choose the job (WebElement)
    }

    @And("I go with Apply with LinkedIn")
    public void iGoWithApplyWithLinkedIn() throws InterruptedException {

        WebElement outerFrame = getDriver().findElement(By.xpath("(//iframe[@data-automation-id='applyWithLinkedinFrame'])[1]"));
        getDriver().switchTo().frame(outerFrame);
        //--> //original-->(//iframe[contains(@src, 'linkedin.com')])[1]")
        WebElement linkedInFrame = getDriver().findElement(By.xpath("//iframe[contains(@src, 'linkedin.com')]"));
        getDriver().switchTo().frame(linkedInFrame);
        Thread.sleep(1000);

        By linkedInButton = By.xpath("(//button[@name='awli-button-member-logged-out'])[1]");
        new WebDriverWait(getDriver(), 5).until(ExpectedConditions.visibilityOfElementLocated(linkedInButton));

        getDriver().findElement(linkedInButton).click();
    }

    @Then("I verify login window opens")
    public void iVerifyLoginWindowOpens() {

        for (String handle : getDriver().getWindowHandles()) {
            getDriver().switchTo().window(handle);
        }

        String title = getDriver().getTitle();
        assertThat(title).contains("LinkedIn");
        //    assertThat(title).containsIgnoringCase("linkedIn");

        WebElement userName = getDriver().findElement(By.xpath("//*[@id='username']"));
        assertThat(userName.isDisplayed()).isTrue();
    }

    @And("I select any tech position")
    public void iSelectAnyTechPosition() {

        List<WebElement> jobs = getDriver().findElements(By.xpath("//a[@class='dice-btn-link loggedInVisited easy-apply']"));
        int index = new Random().nextInt(jobs.size());
        jobs.get(index).click();
    }

    @And("I go with Apply")
    public void iGoWithApply() {
        getDriver().findElement(By.xpath("//div[@class='pull-right hidden-xs']//button[@id='applybtn-2']")).click();
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
