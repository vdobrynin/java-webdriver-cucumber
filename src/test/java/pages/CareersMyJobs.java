package pages;

import org.openqa.selenium.By;
import org.openqa.selenium.TimeoutException;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Duration;
import java.util.List;

import static support.TestContext.getDriver;

public class CareersMyJobs extends Page {
  @FindBy(xpath = "//a[contains(@href, '/positions/')]//h4[@class='position-name']")
  private List<WebElement> positionName;

  public String getPositionName() {
    new WebDriverWait(getDriver(), Duration.ofSeconds(20))
            .until(driver -> positionName.size() > 0);
    return positionName.get(positionName.size() - 1).getText();
  }

  public String getPositionName(String expectedTitle) {
    By locator = By
            .xpath("//a[contains(@href, '/positions/')]//h4[contains(normalize-space(),'" + expectedTitle + "')]");
    try {
      return new WebDriverWait(getDriver(), Duration.ofSeconds(30))
        .until(ExpectedConditions.visibilityOfElementLocated(locator))
        .getText();
    } catch (TimeoutException exception) {
      getDriver().navigate().refresh();
      return new WebDriverWait(getDriver(), Duration.ofSeconds(30))
        .until(ExpectedConditions.visibilityOfElementLocated(locator))
        .getText();
    }
  }
}
