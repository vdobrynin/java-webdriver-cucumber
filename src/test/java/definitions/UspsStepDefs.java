package definitions;

import cucumber.api.java.en.*;
//import io.cucumber.java.en.*;
import org.openqa.selenium.By;
import org.openqa.selenium.Keys;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.Select;
import org.openqa.selenium.support.ui.WebDriverWait;

import static org.assertj.core.api.Assertions.assertThat;
import static support.TestContext.getDriver;
import static support.TestContext.getExecutor;

public class UspsStepDefs {
    @And("I go to Lookup ZIP page by address")
    public void iGoToLookupZIPPageByAddress() {
//    getDriver().findElement(By.xpath("//a[@id='mail-ship-width']")).click();              // change that at the lecture #8
//    getDriver().findElement(By.xpath("//a[@class='button--link'][contains(@href, 'Zip')]")).click();
//    getDriver().findElement(By.xpath("//a[contains(@class, 'zip-code-address')]")).click();
//    Actions actions = new Actions(getDriver());                             // initializing 'actions' optimization we did first, then comment
//        actions.moveToElement(quickTools).perform();                       // after commenting 3 lines above, we change that too
        WebElement quickTools = getDriver().findElement(By.xpath("//li[contains(@class, 'qt-nav')]"));  // save in variable
        new Actions(getDriver()).moveToElement(quickTools).perform();                    // initializing directly driver actions to have mouse over
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
//    getDriver()
//    .findElement(By.xpath("//select[@id='tState']/option[@value='" + state + "']"))
//    .click();                                                                           // passing 'state'
        WebElement dropdown = getDriver()
            .findElement(By.xpath("//select[@id='tState']"));           // optimization from line above
        new Select(dropdown).selectByValue(state);                                    // initializing constructor
        getDriver()
            .findElement(By.xpath("//a[@id='zip-by-address']"))
            .click();
    }

    @Then("I validate {string} zip code exists in the result")
    public void iValidateZipCodeExistsInTheResult(String zip) throws InterruptedException {
//    String result = getDriver().findElement(By.xpath("//div[@id='zipByAddressDiv']")).getText();    // do not need any more
//    assertThat(result).contains(zip);                                                               // after we implemented 2 lines below
        WebDriverWait wait = new WebDriverWait(getDriver(), 5);
        wait.until(ExpectedConditions
            .textToBePresentInElementLocated(By
                .xpath("//div[@id='zipByAddressDiv']"), zip));              //--> it's assertion itself
//    wait.until(driver -> !driver.findElement(By.xpath("//div[@id='zipByAddressDiv']"))
//            .getText().isEmpty());                                                      // another way to do it(lambda) "!"->".isEmpty()
        System.out.println(zip);
    }

    @When("I go to Calculate Price Page")
    public void iGoToCalculatePricePage() {
//    getDriver().manage().timeouts().implicitlyWait(5, TimeUnit.SECONDS);
        getDriver()
            .findElement(By.xpath("//a[@id='mail-ship-width']"))
            .click();
        getDriver()
            .findElement(By.xpath("//a[@class='button--link'][contains(text(),'Calculate a Price')]"))
            .click();
//    getExecutor().executeScript("arguments[0].click();", getDriver()
//      .findElement(By.xpath("//a[@class='button--link'][contains(text(),'Calculate a Price')]")));
    }

    @And("I selected {string} with {string} shape")
    public void iSelectedWithShape(String str1, String str2) {
        getDriver()
            .findElement(By.xpath("//select[@id='CountryID']//option[contains(text(),'United Kingdom (United Kingdom of Great Britain an')]"))
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
        assertThat(priceToVerify).containsIgnoringCase("2.30");
        System.out.println(price);
    }

    @When("I go to {string} tab")
    public void iGoToTab(String tab) {
//        getDriver().findElement(By.xpath("//a[@class='menuitem'][contains(text(),'Postal Store')]")).click();
        getDriver()
            .findElement(By.xpath("//a[text()='" + tab + "']"))
            .click();  // it's easy to access through "tab" everywhere
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
            .findElement(By.xpath("//div[@class='no-results-found']"));
        assertThat(noResults.isDisplayed()).isTrue();  // this ones doesn't work, I tried, but we need it to verify it's present
        assertThat(noResults.getText()).contains("did not match any products");
        System.out.println(noResults.getText());                           // to see, and test
    }

    @When("I go to Stamps and Supplies page")
    public void iGoToStampsAndSuppliesPage() {
        WebElement mailShip = getDriver().findElement(By.xpath("//a[@id='mail-ship-width']"));
        new Actions(getDriver()).moveToElement(mailShip).perform();
        getDriver()
            .findElement(By.xpath("//a[contains(text(),'Stamps & Supplies')]"))
            .click();
    }

    @And("I open Stamps")
    public void iOpenStamps() { //li[contains(@class,'stamps-navigation')]
        getDriver()
            .findElement(By.xpath("//li[contains(@class,'stamps-navigation')]//a"))
            .click();
    }

    @And("choose category Priority Mail")
    public void chooseCategoryPriorityMail() {
        getDriver()
            .findElement(By.xpath("//label[contains(@for, 'Service-Priority Mail-')]"))
            .click();
    }

    @Then("I verify {int} item found in result")
    public void iVerifyItemFoundInResult(int count) {                         // findElements --> plural
        int actualCount = getDriver()
            .findElements(By.xpath("//div[contains(@class,'result-page-stamps-holder ')]"))
            .size();
        assertThat(actualCount).isEqualTo(count);
        System.out.println(actualCount);
    }

    @When("I unselect Stamps checkbox")
    public void iUnselectStampsCheckbox() {
        getDriver()
            .findElement(By.xpath("//label[@for='checkbox-type-Category-Stamps'][contains(text(),'Stamps (92)')]"))
            .click();
    }

    @And("select size {string}")
    public void selectSize(String arg0) {
        getDriver()
            .findElement(By.xpath("//label[@for='checkbox-type-Size-Large-11'][contains(text(), 'Large')]"))
            .click();
    }

    @And("I click {string} color")
    public void iClickColor(String arg0) {
        getDriver()
            .findElement(By.xpath("//div[@class='result-facid-holder-grid-color']//div[3]"))
            .click();
    }

    @Then("I verify {string} and {string} filters")
    public void iVerifyAndFilters(String arg0, String arg1) {
        WebElement result = getDriver()
            .findElement(By.xpath("//div[@class='breadcrumb-cartridge']"));
        assertThat(result.isDisplayed()).isTrue();
        assertThat(result.getText()).contains("Blue Large");
    }

    @Then("I verify {string} sale")
    public void iVerifySale(String arg0) {
        WebElement result = getDriver()
            .findElement(By.xpath("//div[@class='result-products-holder']"));
        assertThat(result.isDisplayed()).isTrue();
        assertThat(result.getText()).contains("20% OFF");
    }

    @When("I perform {string} search")
    public void iPerformSearch(String text) throws InterruptedException {
        WebElement search = getDriver().findElement(By.xpath("//li[contains(@class, 'nav-search')]"));
        WebElement searchInput = getDriver()
            .findElement(By.xpath("//input[@id='global-header--search-track-search']"));
        new Actions(getDriver()).moveToElement(search)
            .click(searchInput)
            .sendKeys(text)
            .sendKeys(Keys.ENTER)
            .perform();
    }

    @And("I select {string} in results")
    public void iSelectInResults(String resultText) {     //span[contains(text(),'Passport Application')]
//        getDriver().findElement(By.xpath("//span[@id='title_20']/span[contains(text(),'"+ resultText +"')]")).click(); //-->it'll not work
        getExecutor().executeScript("arguments[0].click();", getDriver()
            .findElement(By.xpath("//span[contains(text(),'" + resultText + "')]")));
    }

    @And("I click {string} button")
    public void iClickButton(String buttonText) {
//        a[@class='button--primary'][contains(text(),'Find a Post Office')]
        getDriver()
            .findElement(By.xpath("//a[@class='button--primary'][contains(text(),'" + buttonText + "')]"))
            .click();
    }

    @And("I select {string} zip code within {string} and search")
    public void iSelectZipCodeWithinAndSearch(String zip, String miles) {
        getDriver()
            .findElement(By.xpath("//input[@id='tCityStateZip']"))
            .sendKeys(zip); // input zip
        getDriver()
            .findElement(By.xpath("//div[@id='sWithinList']"))
            .click();
//        div[ @id='sWithinList']//a[contains(text(),'10 miles')]
        getDriver()
            .findElement(By.xpath("//div[@id='sWithinList']//a[text()= '" + miles + "']"))
            .click(); // choose dropDown for miles
        getDriver()
            .findElement(By.xpath("//input[@id='bSearch']"))
            .click();
    }

    @Then("I verify {string} present in search results")
    public void iVerifyPresentInSearchResults(String city) {  //div[@id='search-results']
        WebElement search = getDriver()
            .findElement(By.xpath("//div[@id='polo-search-form']//input[@id='bSearch']"));
        getExecutor().executeScript("arguments[0].click();", search);
        WebElement result = getDriver()
            .findElement(By.xpath("//div[@class='polocator-info']"));
        assertThat(result.isDisplayed()).isTrue();
        assertThat(result.getText()).contains("MOUNTAIN VIEW");
    }

    @When("I go to {string} under {string}")
    public void iGoToUnder(String menuItem, String menu) { //a[contains(text(),'Track & Manage')]
        WebElement menuElement = getDriver()
            .findElement(By.xpath("//a[contains(text(),'" + menu + "')]"));
        new Actions(getDriver()).moveToElement(menuElement).perform();                                        // --> mouse over
        getDriver()
            .findElement(By.xpath("//a[contains(text(),'" + menuItem + "')]"))
            .click(); //a[contains(text(),'PO Boxes')]
    }

    @And("I reserve new PO box for {string}")
    public void iReserveNewPOBoxFor(String zip) {
        getDriver()
            .findElement(By.xpath("//input[@id='searchInput']"))
            .sendKeys(zip);
        getDriver()
            .findElement(By.xpath("//span[@class='icon-search']"))
            .click();
    }

    @Then("I verify that {string} present")
    public void iVerifyThatPresent(String location) {
        String actualSearchResult = getDriver()
            .findElement(By.xpath("//div[@id='locationContainer_1']"))
            .getText(); // --> Vlad's
        assertThat(actualSearchResult).containsIgnoringCase(location);
//        WebElement actualSearchResult = getDriver().findElement(By.xpath("//div[@id='locationContainer_1']")); // find location way above the real ones
//        assertThat(actualSearchResult.getText().compareToIgnoreCase(location));                                   // --> Vlad's variation too
//        WebDriverWait wait = new WebDriverWait(getDriver(), 5);   // --> it's me
//        wait.until(ExpectedConditions.textToBePresentInElementLocated(By.xpath("//div[@id='locationContainer_1']"), location));
//        WebElement actualSearchResult = getDriver().findElement(By.xpath("//div[@id='locationContainer_1']"));
//        assertThat(actualSearchResult.getText().compareToIgnoreCase(location));
    }

    @And("I verify that {string} PO Box is available in {string}")
    public void iVerifyThatPOBoxIsAvailableIn(String size, String location) {
        getDriver()
            .findElement(By.xpath("//h2[string()='" + location + "']"))
            .click(); //h2[string()='Los Altos — Post Office™'] --> change to +location+
        String actualSizes = getDriver().findElement(By.xpath("//div[@id='availableboxes']")).getText(); //--> find all available boxes to get text
        assertThat(actualSizes).containsIgnoringCase(size);
    }
}

//  @When("I open {string} search results")
//  public void iOpenSearchResults(String arg0) {
//    getDriver().findElement(By.xpath("//span[@class='po-name'][contains(text(),'MOUNTAIN VIEW')]")).click();
//  }
//
//  @Then("I verify {string} address, {string} appointment hours, {string} photo hours")
//  public void iVerifyAddressAppointmentHoursPhotoHours(String str1, String str2, String str3) {
//    WebElement result = getDriver().findElement(By.xpath("//span[@class='po-name'][contains(text(),'MOUNTAIN VIEW')]"));
//    assertThat(result.isDisplayed()).isTrue();
//    assertThat(result.getText()).contains("211 HOPE ST\" address");
//    assertThat(result.getText()).contains("9:00am - 12:00pm");
//    assertThat(result.getText()).contains("1:00pm - 3:00pm");
//  }
//
//  @When("I go back to list")
//  public void iGoBackToList() {
//    //getDriver().getWindowHandle().
//    getDriver().navigate().back();
//    getDriver().navigate().forward();
//    getDriver().navigate().refresh();
//  }
//
//  @Then("I verify I'm on Find Locations search page")
//  public void iVerifyIMOnFindLocationsSearchPage() {
//    //div[@id='main-inner']//div[@id='polo-index-title']
//
//  }

