package pages;

import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;

public class CareersPositions extends Page {

  @FindBy(xpath = "//a[contains(@href,'/positions/')]/h4[New_Position]")
  private WebElement automationPosition;                  //a[contains(@href, '/candidates/')]

  @FindBy(xpath = "//button[@class='btn btn-primary']")
  private WebElement applyOnPosition;                     //button[@class='btn btn-primary']


  public void choosePosition() {

    click(automationPosition);
  }

  public void clickApplyOnPosition() {

    click(applyOnPosition);
  }
}
