package definitions;

import io.cucumber.java.en.And;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import org.openqa.selenium.By;
import org.openqa.selenium.support.ui.ExpectedConditions;

import static org.assertj.core.api.Assertions.assertThat;
import static support.TestContext.getDriver;
import static support.TestContext.getWait;

public class ConverterStepDefs {
  @When("I click on {string}")
  public void iClickOn(String tab) {
    getDriver().findElement(By.xpath("//a[contains(@href,'" + tab + "')]"))
            .click();
  }

  @And("I set {string} to {string}")
  public void iSetTo(String from, String to) {
    getDriver()
      .findElement(By.xpath("//select[@id='calFrom']//option[text()='" + from + "']"))
            .click();
    getDriver()
      .findElement(By.xpath("//select[@id='calTo']//option[text()='" + to + "']"))
            .click();
  }

  @Then("I enter into From field {string} and verify {string} result")
  public void iEnterIntoFromFieldAndVerifyResult(String fromValue, String toValue) {
    getDriver().findElement(By.xpath("//input[@name='fromVal']"))
      .sendKeys(fromValue);
    String result = getDriver().findElement(By.xpath("//input[@name='toVal']"))
      .getAttribute("value");
    assertThat(result).contains(toValue);
    System.out.println(result);
  }

  @When("I click on {string} button")
  public void iClickOnButton(String tab) {
    getDriver().findElement(By.xpath("//a[contains(text(),'" + tab + "')]"))
            .click();
  }

  @And("I verify {string}")
  public void iVerify(String tabContent) {
    String result = getWait().until(
        ExpectedConditions
                .visibilityOfElementLocated(By.xpath("//*[contains(text(),'" + tabContent + "')]")))
            .getText();
    assertThat(result).contains(tabContent);
  }
}
