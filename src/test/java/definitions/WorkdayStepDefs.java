package definitions;

import static org.assertj.core.api.Assertions.assertThat;
import static org.openqa.selenium.support.ui.ExpectedConditions.visibilityOfElementLocated;
import static support.TestContext.*;

import io.cucumber.java.en.And;
import io.cucumber.java.en.Then;
import java.time.Duration;
import java.util.List;
import java.util.Random;
import java.util.Set;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

public class WorkdayStepDefs {

  @And("I select any position")
  public void iSelectAnyPosition() {
    fluentWait.until(
      ExpectedConditions.presenceOfAllElementsLocatedBy(
        By.xpath("//*[@data-automation-id='jobTitle']")
      )
    );
    List<WebElement> jobs = getDriver().findElements(
      By.xpath("//*[@data-automation-id='jobTitle']")
    );
    int index = new Random().nextInt(jobs.size()); // each time it would to be a random
    jobs.get(index).click(); // choose the job (WebElement)
  }

  @And("I go with Apply with LinkedIn")
  public void iGoWithApplyWithLinkedIn() {
    //                                                // click on 'apply' to go to 'applywithlinkedin'
    fluentWait.until(
      ExpectedConditions.presenceOfElementLocated(
        By.cssSelector(".css-1jxfphv")
      )
    );
    getDriver().findElement(By.cssSelector(".css-1jxfphv")).click();

    fluentWait.until(
      ExpectedConditions.presenceOfElementLocated(
        By.xpath("(//iframe[@title='applyWithLinkedIn'])[1]")
      )
    );
    WebElement outerFrame = getDriver().findElement(
      By.xpath("(//iframe[@title='applyWithLinkedIn'])[1]")
    );
    getDriver().switchTo().frame(outerFrame);

    WebElement linkedInFrame = getDriver().findElement(
      By.xpath("//iframe[contains(@id,'xdOrigin')]")
    );
    getDriver().switchTo().frame(linkedInFrame);

    By linkedInButton = By.xpath("//*[@id='apply-with-linkedin']/span");
    new WebDriverWait(getDriver(), Duration.ofSeconds(10)).until(
      visibilityOfElementLocated(linkedInButton)
    );
    getDriver().findElement(linkedInButton).click();
  }

  @Then("I verify login window opens")
  public void iVerifyLoginWindowOpens() {
    Set<String> allWindows = getDriver().getWindowHandles();
    for (String window : allWindows) {
      getDriver().switchTo().window(window);
    }

    WebElement heading = fluentWait.until(
      visibilityOfElementLocated(By.xpath("//h1[normalize-space()='Sign in']"))
    );
    assertThat(heading.getText()).contains("Sign in");

    WebElement email = fluentWait.until(
      visibilityOfElementLocated(By.id("username"))
    );
    assertThat(email.getAttribute("aria-label")).contains("Email or phone");

    WebElement signIn = fluentWait.until(
      visibilityOfElementLocated(
        By.xpath("//button[@type='submit'][normalize-space()='Sign in']")
      )
    );
    assertThat(signIn.getText()).contains("Sign in");
  }

  @And("I select any tech position")
  public void iSelectAnyTechPosition() {
    fluentWait.until(
      ExpectedConditions.presenceOfAllElementsLocatedBy(
        By.xpath("//a[@data-testid='job-search-job-detail-link']")
      )
    );
    List<WebElement> jobs = getDriver().findElements(
      By.xpath("//a[@data-testid='job-search-job-detail-link']")
    );
    int index = new Random().nextInt(jobs.size());
    getExecutor().executeScript("arguments[0].click();", jobs.get(index));

    fluentWait.until(
      ExpectedConditions.presenceOfElementLocated(
        By.xpath("//a[@data-testid='apply-button']")
      )
    );
  }

  @And("I go with Apply")
  public void iGoWithApply() {
    WebElement applyButton = fluentWait.until(
      ExpectedConditions.elementToBeClickable(
        By.xpath("//a[@data-testid='apply-button']")
      )
    );
    getExecutor().executeScript("arguments[0].click();", applyButton);
  }

  @Then("I verify opens login window")
  public void iVerifyOpensLoginWindow() {
    WebElement heading = fluentWait.until(
      visibilityOfElementLocated(
        By.xpath(
          "//*[normalize-space()='Create an account or sign in to continue']"
        )
      )
    );
    assertThat(heading.getText())
      .contains("Create an account or sign in to continue");

    WebElement continueWithEmail = fluentWait.until(
      visibilityOfElementLocated(
        By.xpath("//*[normalize-space()='Continue with email']")
      )
    );
    assertThat(continueWithEmail.getText()).contains("Continue with email");
  }
}
