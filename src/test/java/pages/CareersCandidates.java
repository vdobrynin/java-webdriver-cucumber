package pages;

import org.openqa.selenium.By;
import org.openqa.selenium.TimeoutException;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Duration;

import static support.TestContext.getDriver;

public class CareersCandidates extends Page {
  private static final By CHOSEN_POSITION = By.xpath(
    "//a[contains(@href, '/positions/')]//h4[contains(text(),'Principal Automation Engineer')]"
  );

  @FindBy(xpath = "//a[contains(@href,'/candidates/')]")
  private WebElement candidate;

  @FindBy(
    xpath = "//a[contains(@href, '/positions/')]//h4[contains(text(),'Principal Automation Engineer')]"
  )
  private WebElement chosenPositionTitle;

  //private List<WebElement> chosenPositionTitle;                                //a[contains(@href, '/positions/')]
  @FindBy(xpath = "//button[@class='btn btn-primary']")
  private WebElement applyPositionButton;

  public void setApplyPositionButton() {
    By applyLocator = By.xpath(
      "//button[contains(normalize-space(),'Apply')]"
    );
    WebElement applyButton = new WebDriverWait(getDriver(), Duration.ofSeconds(20))
      .until(ExpectedConditions.elementToBeClickable(applyLocator));
    applyButton.click();

    try {
      new WebDriverWait(getDriver(), Duration.ofSeconds(20))
              .until(ExpectedConditions.urlContains("/my_jobs"));
    } catch (TimeoutException exception) {
      applyButton = new WebDriverWait(getDriver(), Duration.ofSeconds(20))
        .until(ExpectedConditions.elementToBeClickable(applyLocator));
      applyButton.click();
      new WebDriverWait(getDriver(), Duration.ofSeconds(30)).until(
        ExpectedConditions.urlContains("/my_jobs")
      );
    }
  }

  public void setChosenPositionTitles() {
    try {
      WebElement position = new WebDriverWait(getDriver(), Duration.ofSeconds(20))
        .until(ExpectedConditions
                .visibilityOfElementLocated(CHOSEN_POSITION));
      click(position);
      new WebDriverWait(getDriver(), Duration.ofSeconds(20))
              .until(ExpectedConditions
                      .urlContains("/positions/"));
    } catch (TimeoutException exception) {
      getDriver().get("https://skryabin-careers.herokuapp.com/");
      WebElement position = new WebDriverWait(getDriver(), Duration.ofSeconds(30))
        .until(ExpectedConditions
                .visibilityOfElementLocated(CHOSEN_POSITION));
      click(position);
      new WebDriverWait(getDriver(), Duration.ofSeconds(20))
              .until(ExpectedConditions
                      .urlContains("/positions/"));
    }
  }
}
