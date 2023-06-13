package pages;

import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;

public class CareersCandidates extends Page {

  @FindBy(xpath = "//a[contains(@href,'/candidates/')]")
  private WebElement candidate;

  @FindBy(xpath = "//a[contains(@href, '/positions/')]//h4[contains(text(),'Principal Automation Engineer')]")
  private WebElement chosenPositionTitle;
  //private List<WebElement> chosenPositionTitle;                                //a[contains(@href, '/positions/')]

  @FindBy(xpath = "//button[@class='btn btn-primary']")
  private WebElement applyPositionButton;

  public void setApplyPositionButton() {

    click(applyPositionButton);
  }

  public void setChosenPositionTitles() {

    try {
      Thread.sleep(2000);
    } catch (Exception e) {

    }
    click(chosenPositionTitle);
  }
}
