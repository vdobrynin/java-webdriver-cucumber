package definitions;

import io.cucumber.java.en.And;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import org.openqa.selenium.By;

import static org.assertj.core.api.Assertions.assertThat;
import static support.TestContext.getDriver;

public class CalculatorStepDefs {
  @When("I navigate to {string}")
  public void iNavigateTo(String calc) {
    getDriver().findElement(By
            .xpath("//div[@id='homelistdiv']//div[@id='contentout']//a[text()='" + calc + "']")).click();
  }

  @And("I clear all calculator fields")
  public void iClearAllCalculatorFields() {
    getDriver().findElement(By.xpath("//input[@id='csaleprice']")).clear();
    getDriver().findElement(By.xpath("//input[@id='cloanterm']")).clear();
    getDriver().findElement(By.xpath("//input[@id='cinterestrate']")).clear();
    getDriver().findElement(By.xpath("//input[@id='cincentive']")).clear();
    getDriver().findElement(By.xpath("//input[@id='cdownpayment']")).clear();
    getDriver().findElement(By.xpath("//input[@id='ctradeinvalue']")).clear();
    getDriver().findElement(By.xpath("//input[@id='ctradeinowned']")).clear();
    getDriver().findElement(By.xpath("//input[@id='csaletax']")).clear();
    getDriver().findElement(By.xpath("//input[@id='ctitlereg']")).clear();
  }

  @And("I calculate")
  public void iCalculate() {
    getDriver()
      .findElement(By.xpath("//input[@type='submit' and @value='Calculate']"))
      .click();
  }

  @Then("I verify {string} calculator error")
  public void iVerifyCalculatorError(String errors) {
    String someErrors = getDriver()
      .findElement(By.xpath("//div[@id='contentout']//font[text()='" + errors + "']"))
      .getText();
    assertThat(someErrors).contains(errors);
  }

  @And("I enter {string} price, {string} months, {string} interest, {string} downpayment, {string} trade-in, {string} state," +
      " {string} percent tax, {string} fees")
  public void iEnterPriceMonthsInterestDownpaymentTradeInStatePercentTaxFees(String price, String months, String interest, String downpayment, String trade, String state, String tax, String fees) {
    getDriver()
      .findElement(By.xpath("//input[@id='csaleprice']"))
      .sendKeys(price);
    getDriver()
      .findElement(By.xpath("//input[@id='cloanterm']"))
      .sendKeys(months);
    getDriver()
      .findElement(By.xpath("//input[@id='cinterestrate']"))
      .sendKeys(interest);
    getDriver()
      .findElement(By.xpath("//input[@id='cincentive']"))
      .sendKeys("0");
    getDriver()
      .findElement(By.xpath("//input[@id='cdownpayment']"))
      .sendKeys(downpayment);
    getDriver()
      .findElement(By.xpath("//input[@id='ctradeinvalue']"))
      .sendKeys(trade);
    getDriver()
      .findElement(By.xpath("//input[@id='ctradeinowned']"))
      .sendKeys("0");
    getDriver()
      .findElement(By.xpath("//select[@name='cstate']"))
      .sendKeys(state);
    getDriver().findElement(By.xpath("//input[@id='csaletax']")).sendKeys(tax);
    getDriver()
      .findElement(By.xpath("//input[@id='ctitlereg']"))
      .sendKeys(fees);
  }

  @Then("I verify monthly pay is {string}")
  public void iVerifyMonthlyPayIs(String monthlyPay) {
    String payment = getDriver()
      .findElement(By.xpath("//h2[@class='h2result']"))
      .getText();
    assertThat(payment).contains(monthlyPay);
    System.out.println(payment);
  }
}
