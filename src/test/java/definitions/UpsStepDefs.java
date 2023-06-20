package definitions;

import cucumber.api.java.en.*;
//import io.cucumber.java.en.*;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.FluentWait;
import org.openqa.selenium.support.ui.Wait;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.io.FileNotFoundException;
import java.text.ParseException;
import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;
import static support.TestContext.*;

public class UpsStepDefs {
    @And("I open Shipping menu")
    public void iOpenShippingMenu() {
        getDriver()
            .findElement(By
                .xpath("//nav[@aria-label='Mega Menu']//a[@id='mainNavDropdown1'][normalize-space()='Shipping']"))
            .click();
    }

    @And("I go to Create a Shipment")
    public void iGoToCreateAShipment() {
        getDriver().findElement(By.xpath("//a[normalize-space()='Create a Shipment']")).isDisplayed();
        getExecutor().executeScript("arguments[0].click();", getDriver().findElement(By
            .xpath("//a[contains(text(),'Create a Shipment')]")));
    }

    @When("I fill out origin shipment fields")
    public void iFillOutOriginShipmentFields() throws InterruptedException, FileNotFoundException {
        Map<String, String> sender = getData("sender");                     // to get data from file .yml lecture #10
        getDriver().findElement(By.xpath("//select[@id='origin-cac_country']//option[@value='252']")).isDisplayed();
        getDriver().findElement(By.xpath("//button[@id='origin-singleLineAddressEditButton']")).click();
        getDriver().findElement(By.xpath("//input[@id='origin-cac_companyOrName']")).sendKeys(sender.get("full name"));                  // changes in the  lecture #10
        getDriver().findElement(By.xpath("//body/div[@class='iw_viewport-wrapper']/main[@id='ups-main']//input[@id='origin-cac_addressLine1']"))
            .sendKeys(sender.get("address Line 1"));
        getDriver().findElement(By.xpath("//input[@id='origin-cac_postalCode']")).sendKeys(sender.get("zip"));

        WebDriverWait wait = new WebDriverWait(getDriver(), 5);
        wait.until(ExpectedConditions
            .textToBePresentInElementValue(By.xpath("//input[@id='origin-cac_city']"), sender.get("city")
                .toUpperCase()));
        wait.until(ExpectedConditions
            .elementToBeSelected(By
                .xpath("//select[@id='origin-cac_state']/option[@value='CA']")));
        getDriver().findElement(By.xpath("//input[@id='origin-cac_email']")).sendKeys(sender.get("email"));
        getDriver().findElement(By.xpath("//input[@id='origin-cac_phone']")).sendKeys(sender.get("phone"));

        new WebDriverWait(getDriver(), 10)
            .until(ExpectedConditions.textToBePresentInElementValue(By
                .xpath("//input[@id='origin-cac_city']"), "LOS ALTOS"));
        new WebDriverWait(getDriver(), 10)
            .until(ExpectedConditions.elementToBeSelected(By
                .xpath("//select[@id='origin-cac_state']//option[@value='CA'][normalize-space()='California']")));
    }

    @And("I submit the shipment form")
    public void iSubmitTheShipmentForm() throws InterruptedException {
        String oldUrl = getDriver().getCurrentUrl();
        if (getDriver().getCurrentUrl().contains("payment")) {
            getExecutor().executeScript("arguments[0].click();", getDriver()
                .findElement(By.xpath("//button[@id='nbsBackForwardNavigationReviewPrimaryButton']")));
        } else {
            getExecutor().executeScript("arguments[0].click();", getDriver()
                .findElement(By.xpath("//button[@id='nbsBackForwardNavigationContinueButton']")));
        }
//    WebDriverWait wait = new WebDriverWait(getDriver(), 10); //button[@id='nbsAddressClassificationContinue']
//    wait.until(ExpectedConditions.not(ExpectedConditions.urlToBe(oldUrl)));
    }

    @When("^I submit the shipment form continue$")
    public void iSubmitTheShipmentFormContinue() {
        getExecutor().executeScript("arguments[0].click();", getDriver().findElement(By
            .xpath("(//button[normalize-space()='Continue'])[1]")));
    }

    @And("^I check a few more details$")
    public void iCheckAFewMoreDetails() {
        getDriver()
            .findElement(By.xpath("//input[@id='nbsShipmentDescription']"))
            .sendKeys("USB Cable");
        getExecutor().executeScript("arguments[0].click();", getDriver()
            .findElement(By
                .xpath("//label[@for='nbsDirectDeliveryOnlyOptionBaseOptionSwitch']")));
        getExecutor().executeScript("arguments[0].click();", getDriver()
            .findElement(By
                .xpath("//label[@for='nbsCarbonNeutralOptionBaseOptionSwitch']")));
    }

    @When("I verify origin shipment fields submitted")
    public void iVerifyOriginShipmentFieldsSubmitted() throws InterruptedException, FileNotFoundException {
        boolean shipmentIsDisplayed = getDriver()
            .findElement(By.xpath("//div[@class='ups-wrap']//div[@class='ups-wrap_inner']"))
            .isDisplayed();
        assertThat(shipmentIsDisplayed).isTrue();

        Map<String, String> sender = getData("sender");
        String origin = getDriver().findElement(By.xpath("//ship-app-agent-summary[@class='ng-star-inserted']")).getText();
        assertThat(origin).containsIgnoringCase(sender.get("full name"));
        assertThat(origin).containsIgnoringCase(sender.get("address Line 1"));
        assertThat(origin).containsIgnoringCase(sender.get("zip"));
        assertThat(origin).containsIgnoringCase(sender.get("city"));     //.toUpperCase()); ?
        assertThat(origin).containsIgnoringCase(sender.get("state"));
        assertThat(origin).containsIgnoringCase(sender.get("email"));
        assertThat(origin).containsIgnoringCase(sender.get("phone"));
    }

    @And("I cancel the shipment form")
    public void iCancelTheShipmentForm() {
        getExecutor().executeScript("arguments[0].click();", getDriver()
            .findElement(By.xpath("//button[@id='nbsBackForwardNavigationCancelShipmentButton']")));
        getDriver().findElement(By.xpath("//button[@id='nbsCancelShipmentWarningYes']")).click();
    }

    @When("I verify shipment form is reset")
    public void iVerifyShipmentFormIsReset() {
        String name = getDriver().findElement(By.xpath("//input[@id='origin-cac_companyOrName']"))
            .getAttribute("value");
        assertThat(name).isEmpty();
    }

    @When("I fill out destination shipment fields")
    public void iFillOutDestinationShipmentFields() throws InterruptedException {
        getDriver().findElement(By.xpath("//select[@id='destination-cac_country']//option[.='United States']")).click();
        getDriver().findElement(By.xpath("//span[normalize-space()='Edit Address - Add Suite/Apt']")).click();
        getDriver().findElement(By.xpath("//input[@id='destination-cac_companyOrName']")).sendKeys("John Doe");
        getDriver().findElement(By.xpath("(//input[@id='destination-cac_addressLine1'])[1]")).sendKeys("870 7th Ave");
        getDriver().findElement(By.xpath("//input[@id='destination-cac_postalCode']")).sendKeys("10019");

        WebDriverWait wait = new WebDriverWait(getDriver(), 5);
        wait.until(ExpectedConditions.textToBePresentInElementValue(By.xpath("//input[@id='destination-cac_city']"), "NEW YORK"));
        wait.until(ExpectedConditions.elementToBeSelected(By.xpath("//select[@id='destination-cac_state']//option[.='New York']")));

        getExecutor().executeScript("arguments[0].click();", getDriver()
            .findElement(By.xpath("//button[@id='nbsBackForwardNavigationContinueButton']")));

        WebDriverWait wait1 = new WebDriverWait(getDriver(), 5);
        wait1.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//button[@id='nbsAddressClassificationContinue']")));
        getExecutor().executeScript("arguments[0].click();", getDriver()
            .findElement(By.xpath("//button[@id='nbsAddressClassificationContinue']")));
    }

    @Then("I verify total charges appear")
    public void iVerifyTotalChargesAppear() throws InterruptedException {
        By spinner = By.xpath("//span[@id='nbsBalanceBarTotalCharges']");    // this is Vlad's
        new WebDriverWait(getDriver(), 10)
            .until(ExpectedConditions.visibilityOfElementLocated(spinner));

        WebElement element = getDriver().findElement(spinner);
        String oldText = element.getText();
        new WebDriverWait(getDriver(), 10).until(ExpectedConditions.textToBePresentInElement(element, oldText));

        assertThat(oldText).containsIgnoringCase("$55.13");
        System.out.println("\n" + element.getText());
    }

    @And("I set packaging type")
    public void iSetPackagingType() throws InterruptedException {
        getDriver()
            .findElement(By.xpath("//option[.='UPS Express Box - Small']"))
            .click();
        WebElement packageWeight = getDriver().findElement(By.xpath("//input[@name='nbsPackagePackageWeightField0']"));
        Wait<WebElement> wait = new FluentWait<WebElement>(packageWeight);
        packageWeight.click();

        packageWeight.sendKeys("1");
        wait.until(WebElement -> {
            String value = WebElement.getAttribute("value");
            return "1".equalsIgnoreCase(value);
        });
    }

    @Then("I go back")
    public void iGoBack() {
        getExecutor().executeScript("arguments[0].click();", getDriver()
            .findElement(By.xpath("//button[@id='nbsBackForwardNavigationBackButton']")));
    }

    @Then("I verify total charges changed")
    public void iVerifyTotalChargesChanged() {
        By spinner = By.xpath("//span[@id='total-charges-spinner']");       //--> this is Vlad's
        new WebDriverWait(getDriver(), 10).until(ExpectedConditions.visibilityOfElementLocated(spinner));
        WebElement element = getDriver().findElement(spinner);
        assertThat(element.getText()).isNotEmpty();
    }

    @And("I select cheapest delivery option")
    public void iSelectCheapestDeliveryOption() throws ParseException, InterruptedException {
        //p[contains(@id,'nbsServiceTileTotalCharge')]

        getExecutor().executeScript("arguments[0].click();", getDriver()
            .findElement(By.xpath("//div[@class='row ups-shipping_schedule_row']//strong[contains(text(),'$37.92')]")));

        //    List<WebElement> prices = getDriver().findElements(By.xpath("//p[contains(@id,'nbsServiceTileTotalCharge')]"));
        //    Locale locale = new Locale("en", "US");
        //    NumberFormat formatter = NumberFormat.getCurrencyInstance(locale);
        //    double cheapestPrice = Double.MAX_VALUE;
        //
        //    for (WebElement price : prices) {
        //      if (price.isDisplayed()) {
        //        double currentPrice = formatter.parse(price.getText()).doubleValue();
        //        if (currentPrice < cheapestPrice) {
        //          cheapestPrice = currentPrice;
        //        }
        //      }
        //    }
        //    getExecutor().executeScript("arguments[0].click;", getDriver()
        //      .findElement(By.xpath("//p/strong[contains(text(),'" + cheapestPrice + "')]")));
        //    Thread.sleep(5000);
    }

    @And("I set packaging type {int}nd time")
    public void iSetPackagingTypeNdTime(int arg0) {
        getDriver().findElement(By.xpath("//input[@id='nbsPackagePackageWeightField0']")).clear();
        getDriver().findElement(By.xpath("//select[@id='nbsPackagePackagingTypeDropdown0']")).isSelected();
        getDriver().findElement(By.xpath("//select[@id='nbsPackagePackagingTypeDropdown0']/option[contains(text(),'UPS Express Box - Small')]")).click();
        getDriver().findElement(By.xpath("//input[@id='nbsPackagePackageWeightField0']")).sendKeys("1");
    }

    @And("I set Saturday Delivery type")
    public void iSetSaturdayDeliveryType() {
        getDriver().findElement(By.xpath("//input[@id='nbsShipmentDescription']")).sendKeys("Birthday Gift");
        getDriver().findElement(By.xpath("//..//..//strong[contains(text(),'Saturday Delivery (+$)')]")).click();
    }

    @And("I select Paypal payment type")
    public void iSelectPaypalPaymentType() {
        getDriver().findElement(By.xpath("//div[@id='tile-4']//label[@class='ups-tile_button_content']")).click();
        getDriver().findElement(By.xpath("//label[@class='ups-radio-custom-label ng-star-inserted']")).isSelected();
    }

    @Then("I review all recorded details on the review page")
    public void iReviewAllRecordedDetailsOnTheReviewPage() {

        new WebDriverWait(getDriver(), 5)
            .until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//ng-component[@class='ng-star-inserted']//div[@class='ups-wrap_inner']")));
        String result1 = getDriver().findElement(By.xpath("//origin-return-drawer[@class='ng-star-inserted']//div[@class='ups-drawer-content']")).getText();
        assertThat(result1).contains("Admin");
        assertThat(result1).contains("4970 El Camino Real");
        assertThat(result1).contains("94022");
        assertThat(result1).containsIgnoringCase("LOS ALTOS");
        assertThat(result1).containsIgnoringCase("CA");
        assertThat(result1).containsIgnoringCase("admin@example.com");
        //    assertThat(result1).containsIgnoringCase("1234567890");
        String result2 = getDriver().findElement(By.xpath("//destination-drawer//div[@class='ups-drawer-content']")).getText();
        assertThat(result2).contains("John Doe");
        assertThat(result2).contains("870 7th Ave");
        assertThat(result2).contains("10019");
        assertThat(result2).containsIgnoringCase("NEW YORK");
        assertThat(result2).containsIgnoringCase("NY");
        String result3 = getDriver().findElement(By.xpath("//package-drawer//div[@class='ups-drawer-content']")).getText();
        assertThat(result3).containsIgnoringCase("UPS Express Box - Small - 1 lbs");
        assertThat(result3).containsIgnoringCase("1 lbs");
        String result4 = getDriver().findElement(By.xpath("//options-drawer//div[@class='ups-drawer-content']")).getText();
        assertThat(result4).containsIgnoringCase("Birthday Gift");
        assertThat(result4).containsIgnoringCase("Saturday Delivery");
    }
}
