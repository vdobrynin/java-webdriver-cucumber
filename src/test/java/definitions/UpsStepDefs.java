package definitions;

import static org.assertj.core.api.Assertions.assertThat;
import static support.TestContext.*;

import io.cucumber.java.en.*;
import java.time.Duration;
import java.util.List;
import java.util.Map;
import org.openqa.selenium.By;
import org.openqa.selenium.Keys;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

public class UpsStepDefs {

  @And("I open Shipping menu")
  public void iOpenShippingMenu() {
    WebElement shippingMenu = new WebDriverWait(
      getDriver(),
      Duration.ofSeconds(15)
    )
      .until(
        ExpectedConditions.elementToBeClickable(
          By.xpath("//nav//button[normalize-space()='Shipping']")
        )
      );
    getExecutor().executeScript("arguments[0].click();", shippingMenu);
  }

  @And("I go to Create a Shipment")
  public void iGoToCreateAShipment() {
    WebElement createShipment = new WebDriverWait(
      getDriver(),
      Duration.ofSeconds(15)
    )
      .until(
        ExpectedConditions.elementToBeClickable(
          By.xpath(
            "//div[@id='nav-sections']//a[normalize-space()='Create a Shipment']"
          )
        )
      );
    getExecutor().executeScript("arguments[0].click();", createShipment);
  }

  private void dismissCookieBanner() {
    List<WebElement> closeButtons = getDriver().findElements(
      By.xpath("//button[contains(@class,'onetrust-close-btn-handler')]")
    );
    for (WebElement button : closeButtons) {
      if (button.isDisplayed()) {
        getExecutor().executeScript("arguments[0].click();", button);
        return;
      }
    }
  }

  private void typeField(String id, String value) {
    WebElement field = new WebDriverWait(getDriver(), Duration.ofSeconds(20))
      .until(ExpectedConditions.visibilityOfElementLocated(By.id(id)));
    getExecutor().executeScript(
      "arguments[0].scrollIntoView({block: 'center'});",
      field
    );
    field.clear();
    field.sendKeys(value);
  }

  private void typeAddress(String id, String value) {
    typeField(id, value);
    getDriver().findElement(By.id(id)).sendKeys(Keys.ESCAPE);
    new WebDriverWait(getDriver(), Duration.ofSeconds(10)).until(
      ExpectedConditions.visibilityOfElementLocated(
        By.id(id.replace("Address poBox-error-message", "City"))
      )
    );
  }

  private void selectState(String prefix, String state) {
    String stateDropdown = prefix + "StateDropdown";
    WebElement dropdownButton = new WebDriverWait(
      getDriver(),
      Duration.ofSeconds(10)
    )
      .until(
        ExpectedConditions.elementToBeClickable(
          By.id("btn_" + stateDropdown)
        )
      );
    getExecutor().executeScript("arguments[0].click();", dropdownButton);

    WebElement stateOption = new WebDriverWait(
      getDriver(),
      Duration.ofSeconds(10)
    )
      .until(
        ExpectedConditions.elementToBeClickable(
          By.xpath(
            "//button[contains(@id, 'option_" +
              stateDropdown +
              "_') and normalize-space()='" +
              state +
              "']"
          )
        )
      );
    getExecutor().executeScript("arguments[0].click();", stateOption);
  }

  @When("I fill out origin shipment fields")
  public void iFillOutOriginShipmentFields() {
    dismissCookieBanner();
    Map<String, String> sender = getData("sender");
    String origin = "ShippingSummaryOriginAdd";

    typeField(origin + "-Name", sender.get("name"));
    typeField(origin + "-Email", sender.get("email"));
    typeField(origin + "-Phone", sender.get("phone"));
    typeAddress(origin + "-Address poBox-error-message", sender.get("street"));
    typeField(origin + "-City", sender.get("city"));
    selectState(origin, sender.get("state"));
    typeField(origin + "-Postal", sender.get("zip"));
  }

  @And("I submit the shipment form")
  public void iSubmitTheShipmentForm() {
    if (getDriver().getCurrentUrl().contains("shipment-info")) {
      iFillOutOriginShipmentFields();
      iFillOutDestinationShipmentFields();
      iSetPackagingType();
    }
    String previousUrl = getDriver().getCurrentUrl();
    clickNextButton();
    try {
      waitForUrlChange(previousUrl, 15);
    } catch (Exception exception) {
      clickNextButton();
      waitForUrlChange(previousUrl, 20);
    }
  }

  private void clickNextButton() {
    WebElement nextButton = new WebDriverWait(
      getDriver(),
      Duration.ofSeconds(20)
    )
      .until(ExpectedConditions.elementToBeClickable(By.id("next-btn")));
    getExecutor().executeScript(
      "arguments[0].scrollIntoView({block: 'center'});",
      nextButton
    );
    try {
      nextButton.click();
    } catch (Exception exception) {
      getExecutor().executeScript("arguments[0].click();", nextButton);
    }
  }

  private void waitForUrlChange(String previousUrl, long timeoutSeconds) {
    new WebDriverWait(getDriver(), Duration.ofSeconds(timeoutSeconds)).until(
      driver -> !driver.getCurrentUrl().equals(previousUrl)
    );
  }

  @Then("I submit a shipment form continue")
  public void iSubmitAShipmentFormContinue() {
    //        getExecutor()
    //            .executeScript("arguments[0].click();", getDriver()
    //                .findElement(By.xpath("//button[@id='nbsBackForwardNavigationContinueButton']")));
    if (
      getDriver()
        .findElement(By.xpath("//div[@class='modal-content']"))
        .isDisplayed()
    ) {
      getExecutor().executeScript(
        "arguments[0].click();",
        getDriver().findElement(
          By.xpath(
            "(//label[@for='vm.residentialAddressControlId']//span[@class='ups-lever_switch_no'][normalize-space()='No'])[1]"
          )
        )
      );
      getExecutor().executeScript(
        "arguments[0].click();",
        getDriver().findElement(
          By.xpath("//button[@id='nbsAddressClassificationContinue']")
        )
      );
    }
  }

  @And("^I check a few more details$")
  public void iCheckAFewMoreDetails() {
    typeField("isDeclaredValue-shipmentDescription", "USB Cable");
  }

  @When("I verify origin shipment fields submitted")
  public void iVerifyOriginShipmentFieldsSubmitted() {
    new WebDriverWait(getDriver(), Duration.ofSeconds(20)).until(
      ExpectedConditions.urlContains("service-selection")
    );

    Map<String, String> sender = getData("sender");
    String summary = getDriver().findElement(By.tagName("body")).getText();
    assertThat(summary).containsIgnoringCase(sender.get("name"));
    assertThat(summary).containsIgnoringCase(sender.get("street"));
    assertThat(summary).containsIgnoringCase(sender.get("zip"));
    assertThat(summary).containsIgnoringCase(sender.get("city"));
    assertThat(summary).containsIgnoringCase(sender.get("email"));
    assertThat(summary).contains(sender.get("phone"));
  }

  @And("I cancel the shipment form")
  public void iCancelTheShipmentForm() {
    getDriver().get("https://www.ups.com/ship?loc=en_US");
    new WebDriverWait(getDriver(), Duration.ofSeconds(20)).until(
      ExpectedConditions.visibilityOfElementLocated(
        By.id("ShippingSummaryOriginAdd-Name")
      )
    );
    dismissCookieBanner();
  }

  @When("I verify shipment form is reset")
  public void iVerifyShipmentFormIsReset() {
    String name = getDriver()
      .findElement(By.id("ShippingSummaryOriginAdd-Name"))
      .getAttribute("value");
    assertThat(name).isEmpty();
  }

  @When("I fill out destination shipment fields")
  public void iFillOutDestinationShipmentFields() {
    String destination = "ShippingSummaryDestinationAdd";

    typeField(destination + "-Name", "John Doe");
    typeAddress(
      destination + "-Address poBox-error-message",
      "870 7th Ave"
    );
    typeField(destination + "-City", "NEW YORK");
    selectState(destination, "New York");
    typeField(destination + "-Postal", "10019");
  }

  @Then("I verify total charges appear")
  public void iVerifyTotalChargesAppear() {
    WebElement totalCharges = new WebDriverWait(
      getDriver(),
      Duration.ofSeconds(20)
    )
      .until(
        ExpectedConditions.visibilityOfElementLocated(
          By.id("shippingsummary-totalcharges")
        )
      );
    assertThat(totalCharges.getText()).matches("\\$[\\d,.]+");
    System.out.println("\n" + totalCharges.getText());
  }

  @And("I set packaging type")
  public void iSetPackagingType() {
    String origin = "ShippingSummaryOriginAdd";
    typeField(origin + "-Weight", "1");
    typeField(origin + "-Length", "1");
    typeField(origin + "-Width", "1");
    typeField(origin + "-Height", "1");
  }

  @Then("I go back")
  public void iGoBack() {
    getExecutor().executeScript(
      "arguments[0].click();",
      getDriver().findElement(
        By.xpath("//button[@id='nbsBackForwardNavigationBackButton']")
      )
    );
  }

  @Then("I verify total charges changed")
  public void iVerifyTotalChargesChanged() {
    By spinner = By.xpath("//span[@id='total-charges-spinner']"); //--> this is Vlad's
    new WebDriverWait(getDriver(), Duration.ofSeconds(10)).until(
      ExpectedConditions.visibilityOfElementLocated(spinner)
    );
    WebElement element = getDriver().findElement(spinner);
    assertThat(element.getText()).isNotEmpty();
  }

  @And("I select cheapest delivery option")
  public void iSelectCheapestDeliveryOption() {
    List<WebElement> cheapestLabels = new WebDriverWait(
      getDriver(),
      Duration.ofSeconds(20)
    )
      .until(
        ExpectedConditions.presenceOfAllElementsLocatedBy(
          By.xpath(
            "//label[.//div[contains(@class,'tile-body-text') and normalize-space()='UPS Ground']]"
          )
        )
      );
    assertThat(cheapestLabels).isNotEmpty();

    WebElement cheapestLabel = cheapestLabels
      .stream()
      .filter(WebElement::isDisplayed)
      .findFirst()
      .orElse(cheapestLabels.get(0));
    getExecutor().executeScript(
      "arguments[0].scrollIntoView({block: 'center'});",
      cheapestLabel
    );
    getExecutor().executeScript("arguments[0].click();", cheapestLabel);

    WebElement cheapestService = getDriver().findElement(
      By.id(cheapestLabel.getAttribute("for"))
    );
    new WebDriverWait(getDriver(), Duration.ofSeconds(10)).until(
      ExpectedConditions.elementToBeSelected(cheapestService)
    );
  }

  @And("I set packaging type {int}nd time")
  public void iSetPackagingTypeNdTime(int arg0) {
    getDriver()
      .findElement(By.xpath("//input[@id='nbsPackagePackageWeightField0']"))
      .clear();
    getDriver()
      .findElement(By.xpath("//select[@id='nbsPackagePackagingTypeDropdown0']"))
      .isSelected();
    getDriver()
      .findElement(
        By.xpath(
          "//select[@id='nbsPackagePackagingTypeDropdown0']/option[contains(text(),'UPS Express Box - Small')]"
        )
      )
      .click();
    getDriver()
      .findElement(By.xpath("//input[@id='nbsPackagePackageWeightField0']"))
      .sendKeys("1");
  }

  @And("I set Saturday Delivery type")
  public void iSetSaturdayDeliveryType() {
    getDriver()
      .findElement(By.xpath("//span[@class='icon ups-icon-calendar']"))
      .click();
    getDriver()
      .findElement(
        By.xpath("(//button[@id='ups-official-dp-chooser-202382'])[1]")
      )
      .isSelected();
    getDriver()
      .findElement(
        By.xpath("(//button[@id='ups-official-dp-chooser-202382'])[1]")
      )
      .click();
  }

  @And("I select Paypal payment type")
  public void iSelectPaypalPaymentType() {
    WebElement otherMethods = new WebDriverWait(
      getDriver(),
      Duration.ofSeconds(20)
    )
      .until(
        ExpectedConditions.presenceOfElementLocated(By.id("action4"))
      );
    getExecutor().executeScript("arguments[0].click();", otherMethods);

    WebElement paypal = new WebDriverWait(
      getDriver(),
      Duration.ofSeconds(10)
    )
      .until(
        ExpectedConditions.presenceOfElementLocated(
          By.id("OtherMethods-PaymentMethod0")
        )
      );
    getExecutor().executeScript("arguments[0].click();", paypal);
    new WebDriverWait(getDriver(), Duration.ofSeconds(10)).until(
      ExpectedConditions.elementToBeSelected(paypal)
    );
  }

  @Then("I review all recorded details on the review page")
  public void iReviewAllRecordedDetailsOnTheReviewPage() {
    new WebDriverWait(getDriver(), Duration.ofSeconds(20)).until(
      ExpectedConditions.urlContains("payment")
    );

    String review = getDriver().findElement(By.tagName("body")).getText();
    assertThat(review).contains("Admin");
    assertThat(review).contains("4970 El Camino Real");
    assertThat(review).contains("94022");
    assertThat(review).containsIgnoringCase("LOS ALTOS");
    assertThat(review).containsIgnoringCase("CA");
    assertThat(review).containsIgnoringCase("admin@example.com");
    assertThat(review).contains("John Doe");
    assertThat(review).contains("870 7th Ave");
    assertThat(review).contains("10019");
    assertThat(review).containsIgnoringCase("NEW YORK");
    assertThat(review).containsIgnoringCase("NY");
    assertThat(review).contains("USB Cable");
  }
}
