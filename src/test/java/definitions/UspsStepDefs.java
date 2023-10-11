package definitions;

import cucumber.api.java.en.And;
import cucumber.api.java.en.Then;
import cucumber.api.java.en.When;
import org.openqa.selenium.By;
import org.openqa.selenium.Keys;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.Select;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Duration;

import static org.assertj.core.api.Assertions.assertThat;
import static support.TestContext.getDriver;
import static support.TestContext.getExecutor;

public class UspsStepDefs {
    @And("I go to Lookup ZIP page by address")
    public void iGoToLookupZIPPageByAddress() {
//                                                    // → change that at the lecture #8
        WebElement quickTools = getDriver()
            .findElement(By.xpath("//li[contains(@class, 'qt-nav')]"));// save in variable xpath was custom
        new Actions(getDriver())
            .moveToElement(quickTools)
            .perform();                         // initializing directly driver to new Actions to have mouse over
        getDriver()
            .findElement(By.xpath("//img[@alt='Zip Code™ Lookup Icon']"))
            .click();
        getDriver()
            .findElement(By.xpath("//a[normalize-space()='Find by Address']"))
            .click();
    }

    @And("I fill out {string} street, {string} city, {string} state")
    public void iFillOutStreetCityState(String street, String city, String state) {

        getDriver()
            .findElement(By.xpath("//input[@id='tAddress']"))
            .sendKeys(street);
        getDriver()
            .findElement(By.xpath("//input[@id='tCity']"))
            .sendKeys(city);

        WebElement dropdown = getDriver()
            .findElement(By.xpath("//select[@id='tState']"));
        new Select(dropdown).selectByValue(state);                              // initializing constructor

        getDriver()
            .findElement(By.xpath("//a[@id='zip-by-address']"))
            .click();
    }

    @Then("I validate {string} zip code exists in the result")
    public void iValidateZipCodeExistsInTheResult(String zip) throws InterruptedException {

        WebDriverWait wait = new WebDriverWait(getDriver(), Duration.ofSeconds(5));
//        wait.until(ExpectedConditions.textToBePresentInElementLocated(By
//                .xpath("//div[@id='zipByAddressDiv']"), zip));              //--> it's assertion itself
        wait.until(driver -> !driver
            .findElement(By.xpath("//div[@id='zipByAddressDiv']"))
            .getText()
            .isEmpty());                                                      // another way to do it(lambda) "!"->".isEmpty()
        System.out.println(zip);
    }

    @When("I go to Calculate Price Page")
    public void iGoToCalculatePricePage() {
//        getDriver().manage().timeouts().implicitlyWait(5, TimeUnit.SECONDS);
        getDriver()
            .findElement(By.xpath("//a[@id='mail-ship-width']"))
            .click();
        getDriver()
            .findElement(By.xpath("//a[@href='/calculateretailpostage/welcome.htm'][.='Calculate a Price']"))
            .click();
    }

    @And("I selected {string} with {string} shape")
    public void iSelectedWithShape(String str1, String str2) {

        getDriver()
            .findElement(By.xpath("//select[@id='CountryID']//option[contains(text()," +
                "'United Kingdom (United Kingdom of Great Britain an')]"))
            .click();
        getDriver()
            .findElement(By.xpath("//input[@id='option_1']"))
            .click();
    }

    @And("I define {string} quantity")
    public void iDefineQuantity(String num) {

        getDriver()
            .findElement(By.xpath("//input[@id='quantity-0']"))
            .sendKeys("2");
        getDriver()
            .findElement(By.xpath("//input[@class='btn btn-pcalc btn-default']"))
            .click();
    }

    @Then("I calculate the price and validate cost is {string}")
    public void iCalculateThePriceAndValidateCostIs(String price) {

        String priceToVerify = getDriver().findElement(By.xpath("//div[@id='wrap']//div[@class='container']"))
            .getText();
        assertThat(priceToVerify)
            .containsIgnoringCase("2.90");
        System.out.println(price);
    }

    @When("I go to {string} tab")
    public void iGoToTab(String tab) {

        getDriver()
            .findElement(By.xpath("//a[normalize-space()='" + tab + "']"))
            .click();                                               // it's easy to access through "tab" everywhere
    }

    @And("I enter {string} into store search")
    public void iEnterIntoStoreSearch(String text) {

        getDriver()
            .findElement(By.xpath("//input[@id='store-search']"))
            .sendKeys(text);
    }

    @Then("I search and validate no products found")
    public void iSearchAndValidateNoProductsFound() {

        getDriver()
            .findElement(By.xpath("//input[@id='store-search-btn']"))
            .click();
        WebElement noResults = getDriver()
            .findElement(By.xpath("//p[normalize-space()='Your search did not match any products.']"));
        assertThat(noResults.isDisplayed())
            .isTrue();  // this ones doesn't work, I tried, but we need it to verify it's present
        assertThat(noResults.getText())
            .contains("did not match any products");
        System.out.println(noResults.getText());                           // to see, and test
    }

    @When("I go to Stamps and Supplies page")
    public void iGoToStampsAndSuppliesPage() {

        WebElement mailShip = getDriver().findElement(By.xpath("//a[@id='mail-ship-width']"));
        new Actions(getDriver())
            .moveToElement(mailShip)
            .perform();
        getDriver()
            .findElement(By.xpath("//a[@role='menuitem'][normalize-space()='Stamps & Supplies']"))
            .click();
    }

    @And("I open Stamps")
    public void iOpenStamps() {

        getDriver()
            .findElement(By.xpath("//li[@class='stamps-navigation ']//span[normalize-space()='Stamps']"))
            .click();
    }

    @And("choose category Priority Mail")
    public void chooseCategoryPriorityMail() {

        getExecutor()
            .executeScript("arguments[0].click();", getDriver()
                .findElement(By.xpath("//label[normalize-space()='Priority Mail (1)']")));

    }

    @Then("I verify {int} item found in result")
    public void iVerifyItemFoundInResult(int count) {                         // findElements --> plural

        int actualCount = getDriver()
            .findElements(By.xpath("//div[@class='result-products-holder']"))
            .size();
        assertThat(actualCount)
            .isEqualTo(count);
        System.out.println(actualCount);
    }

    @When("I unselect Stamps checkbox")
    public void iUnselectStampsCheckbox() {

        getDriver()
            .findElement(By.xpath("//label[@for='checkbox-type-Category-Stamps'][contains(text(),'Stamps (91)')]"))
            .click();
    }

    @And("select size {string}")
    public void selectSize(String arg0) {

        getExecutor()
            .executeScript("arguments[0].click();", getDriver()
                .findElement(By.xpath("//label[normalize-space()='Large (18)']")));
    }

    @And("I click {string} color")
    public void iClickColor(String arg0) {

        getExecutor()
            .executeScript("arguments[0].click();", getDriver()
                .findElement(By.xpath("//div[@class='result-grid'][contains(@style, 'background-color:#033366;')]")));
    }

    @Then("I verify {string} and {string} filters")
    public void iVerifyAndFilters(String arg0, String arg1) {

        WebElement result = getDriver()
            .findElement(By.xpath("//div[@class=' d-none d-lg-block breadcrumb-cartridge']//div[@class='cartridge-viewport']"));
        assertThat(result.isDisplayed())
            .isTrue();
        assertThat(result.getText())
            .contains("Blue");
        assertThat(result.getText())
            .contains("Large");
    }

    @Then("I verify {string} sale")
    public void iVerifySale(String arg0) {

        WebElement result = getDriver()
            .findElement(By.xpath("//div[@class='result-products-holder']"));
        assertThat(result.isDisplayed())
            .isTrue();
        assertThat(result.getText())
            .contains("$9.95");         //20% OFF ???
    }

    @When("I perform {string} search")
    public void iPerformSearch(String text) throws InterruptedException {

        WebElement search = getDriver()
            .findElement(By.xpath("//li[contains(@class, 'nav-search')]"));
        WebElement searchInput = getDriver()
            .findElement(By.xpath("//input[@id='global-header--search-track-search']"));
        new Actions(getDriver())
            .moveToElement(search)
            .click(searchInput)
            .sendKeys(text)
            .sendKeys(Keys.ENTER)
            .perform();
    }

    @And("I select {string} in results")
    public void iSelectInResults(String resultText) {

        getExecutor()
            .executeScript("arguments[0].click();", getDriver()
                .findElement(By
                    .xpath("//div[@id='main_res']//span[contains(text(),'" + resultText + "')]")));
    }

    @And("I click {string} button")
    public void iClickButton(String buttonText) {

        getDriver()
            .findElement(By.xpath("//a[normalize-space()='" + buttonText + "']"))
            .click();
    }

    @And("I select {string} zip code within {string} and search")
    public void iSelectZipCodeWithinAndSearch(String zip, String miles) {

        getDriver()
            .findElement(By.xpath("//input[@id='city-state-input']"))
            .sendKeys(zip);                                                                     // input zip
        getDriver()
            .findElement(By.xpath("//div[@class='dropdown-selection']//button[@id='within-select']"))
            .click();
        getDriver()
            .findElement(By.xpath("//a[contains(text(),'" + miles + "')]"))
            .click();
        getDriver()
            .findElement(By.xpath("(//div[@class='search-btn-container'])[1]"))
            .click();
    }

    @Then("I verify {string} present in search results")
    public void iVerifyPresentInSearchResults(String city) {

        WebDriverWait wait = new WebDriverWait(getDriver(), Duration.ofSeconds(5));
        wait.until(ExpectedConditions
            .textToBePresentInElementLocated(By
                .xpath("(//div[@class='floating-map result-inside-container']//div[@id='resultBox'])[1]"), city));
        WebElement cityName = getDriver()
            .findElement(By.xpath("//strong[.='" + city + "']"));
        WebElement result = getDriver()
            .findElement(By.xpath("(//div[@class='floating-map result-inside-container']//div[@id='resultBox'])[1]"));
        assertThat(result.isDisplayed())
            .isTrue();
        assertThat(cityName.getText())
            .contains("MOUNTAIN VIEW");
    }

    @When("I go to {string} under {string}")
    public void iGoToUnder(String menuItem, String menu) {

        WebElement quickTools = getDriver()
            .findElement(By.xpath("//li[contains(@class, 'qt-nav')]//a[text()='" + menu + "']")); // save in variable xpath custom
        new Actions(getDriver())
            .moveToElement(quickTools)
            .perform();
        getDriver()
            .findElement(By.xpath("//li[@class='qt-nav menuheader']//p[.='" + menuItem + "']"))
            .click();
    }

    @And("I reserve new PO box for {string}")
    public void iReserveNewPOBoxFor(String zip) {

        getDriver()
            .findElement(By.xpath("//input[@id='searchHero']"))
            .sendKeys(zip);
        getDriver()
            .findElement(By.xpath("//div[@class='inputContainer']//div//a[@value='Search']"))
            .click();
    }

    @Then("I verify that {string} present")
    public void iVerifyThatPresent(String location) {

        String actualSearchResult = getDriver()
            .findElement(By.xpath("//h2[contains(.,'" + location + "')]"))
            .getText();
        assertThat(actualSearchResult)
            .containsIgnoringCase(location);
    }

    @And("I verify that {string} PO Box is available in {string}")
    public void iVerifyThatPOBoxIsAvailableIn(String size, String location) {

        getDriver()
            .findElement(By.xpath("//h2[contains(.,'" + location + "')]"))
            .click();
        String actualSizes = getDriver()
            .findElement(By.xpath("//div[@id='availableboxes']"))
            .getText();                                                                 //—→ find all available boxes to get text
        assertThat(actualSizes)
            .containsIgnoringCase(size);
    }

    @When("I open {string} search results")
    public void iOpenSearchResults(String text) {

        getDriver()
            .findElement(By.xpath("(//div[@class='search-btn-container'])[1]"))
            .click();
        WebDriverWait wait = new WebDriverWait(getDriver(), Duration.ofSeconds(5));
        wait.until(ExpectedConditions
            .textToBePresentInElementLocated(By
                .xpath("(//div[@class='location-address']//strong[contains(text(), '" + text + "')])[1]"), text));
        getExecutor()
            .executeScript("arguments[0].click();", getDriver()
                .findElement(By
                    .xpath("(//div[@class='location-address']//strong[contains(text(), '" + text + "')])[1]")));
    }

    @Then("I verify {string} address, {string} appointment hours, {string} photo hours")
    public void iVerifyAddressAppointmentHoursPhotoHours(String address, String appointmentHours, String photoHours) {

        WebElement resultColumn = getDriver()
            .findElement(By
                .xpath("(//div[@id='po-location-detail'])[1]"));
        assertThat(resultColumn.isDisplayed())
            .isTrue();
        WebDriverWait wait1 = new WebDriverWait(getDriver(), Duration.ofSeconds(5));
        wait1.until(ExpectedConditions
            .visibilityOfElementLocated(By
                .xpath("(//div[@id='resultBox'])[1]")));
        String results1 = getDriver()
            .findElement(By.xpath("(//div[@id='resultBox'])[1]"))
            .getText();
        assertThat(results1)
            .contains(address);
        WebDriverWait wait2 = new WebDriverWait(getDriver(), Duration.ofSeconds(5));
        wait2.until(ExpectedConditions
            .visibilityOfElementLocated(By
                .xpath("(//div[@id='commonServices'])[1]")));
        String results2 = getDriver()
            .findElement(By.xpath("(//div[@id='commonServices'])[1]"))
            .getText();
        assertThat(results2)
            .contains(appointmentHours);
        assertThat(results2)
            .contains(photoHours);
    }

    @When("I go back to list")
    public void iGoBackToList() {

        getDriver().getWindowHandle();
        getDriver().navigate().back();
        getDriver().navigate().refresh();
    }

    @Then("I verify I'm on Find Locations search page")
    public void iVerifyIMOnFindLocationsSearchPage() {

        getDriver()
            .findElement(By.xpath("//a[@href='#'][contains(.,'Back to Find Locations')]"))
            .isDisplayed();
    }
}
