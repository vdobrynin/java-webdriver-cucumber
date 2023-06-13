package pages;

import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;

public class CareersMyJobs extends Page {

  @FindBy(xpath = "//a[contains(@href, '/positions/')]//h4[@class='position-name']")
  //private List<WebElement> positionName;
  private WebElement positionName;

  public String getPositionName() throws InterruptedException {

//    new WebDriverWait(getDriver(), 5).until(driver -> positionName.size() > 2);
//    int size = positionName.size();
//
//    WebElement lastPositionName = positionName.get(size - 1);
    return positionName.getText();
  }
}
