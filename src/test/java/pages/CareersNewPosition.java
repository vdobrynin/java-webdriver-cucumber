package pages;

import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.Select;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Duration;
import java.util.Map;

import static support.TestContext.getDriver;

public class CareersNewPosition extends CareersHeader {
  @FindBy(xpath = "//label[@for='positionTitle']/../input")
  private WebElement title;

  @FindBy(xpath = "//label[@for='positionDescription']/../textarea")
  private WebElement description;

  @FindBy(xpath = "//label[@for='positionAddress']/../input")
  private WebElement address;

  @FindBy(xpath = "//label[@for='positionCity']/../input")
  private WebElement city;

  @FindBy(xpath = "//label[@for='positionState']/../select")
  private WebElement state;

  @FindBy(xpath = "//label[@for='positionZip']/../input")
  private WebElement zip;

  @FindBy(xpath = "//input[@id='positionDateOpen']")
  private WebElement dateOpen;

  @FindBy(xpath = "//button[@id='positionSubmit']")
  private WebElement submit;

  public void createPosition(Map<String, String> position) {
    WebDriverWait wait = new WebDriverWait(getDriver(), Duration.ofSeconds(20));
    WebElement titleField = wait
            .until(ExpectedConditions.visibilityOf(title));

    titleField.clear();
    titleField.sendKeys(position.get("title"));
    wait.until(driver ->
      position.get("title").equals(titleField.getAttribute("value")));

    description.sendKeys(position.get("description"));
    address.sendKeys(position.get("address"));
    city.sendKeys(position.get("city"));
    new Select(state).selectByVisibleText(position.get("state"));
    zip.sendKeys(position.get("zip"));
    dateOpen.sendKeys(position.get("dateOpen"));
    click(submit);
  }
}
