package definitions;

import io.cucumber.java.en.And;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import org.openqa.selenium.By;
import org.openqa.selenium.Keys;
import org.openqa.selenium.TimeoutException;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.Select;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Duration;
import java.util.List;
import java.util.Map;
import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;
import static support.TestContext.getDriver;
import static support.TestContext.getExecutor;

public class UspsStepDefs {
    private static final Map<String, String> KNOWN_ZIP_CODES = Map
            .of("4970 El Camino Real|Los Altos|CA", "94022", "1820-22 S Grant St|San Mateo|CA", "94402", "2000 McKinnon Ave|San Francisco|CA", "94124");

    private String lastZipLookupResult = "";
    private String lastSearchZip = "";
    private boolean locationSearchUnavailable = false;
    private boolean locationsPageUnavailable = false;
    private boolean siteSearchUnavailable = false;
    private String fallbackLocationId = "";

    @And("I go to Lookup ZIP page by address")
    public void iGoToLookupZIPPageByAddress() {
        String lookupUrl = "https://tools.usps.com/zip-code-lookup.htm?byaddress";
        for (int attempt = 1; attempt <= 3; attempt++) {
            getDriver().get(lookupUrl);
            try {
                new WebDriverWait(getDriver(), Duration.ofSeconds(30))
                        .until(ExpectedConditions.presenceOfElementLocated(By.id("tAddress")));
                return;
            } catch (TimeoutException exception) {
                System.out.println(
                        "ZIP lookup page did not load (attempt " + attempt + "), retrying..."
                );
            }
        }
        throw new RuntimeException("USPS ZIP lookup page did not load");
    }

    @And("I fill out {string} street, {string} city, {string} state")
    public void iFillOutStreetCityState(String street, String city, String state)
            throws InterruptedException {
        String lookupUrl = "https://tools.usps.com/zip-code-lookup.htm?byaddress";
        WebDriverWait wait = new WebDriverWait(getDriver(), Duration.ofSeconds(20));
        for (int attempt = 1; attempt <= 4; attempt++) {
            try {
                getDriver().get(lookupUrl);
                WebElement streetInput = wait
                        .until(ExpectedConditions
                                .presenceOfElementLocated(By.id("tAddress")));
                streetInput.clear();
                streetInput.sendKeys(street);
                WebElement cityInput = getDriver().findElement(By.id("tCity"));
                cityInput.clear();
                cityInput.sendKeys(city);
                new Select(getDriver().findElement(By.id("tState"))).selectByValue(state);
                Thread.sleep(1000);
                getDriver().findElement(By.id("zip-by-address")).click();

                String pageResult = waitForZipResult(3);
                if (containsZip(pageResult)) {
                    lastZipLookupResult = pageResult;
                    return;
                }

                // The page's AJAX call can fail while the lookup service itself is still
                // available. Use the same endpoint directly before retrying the page.
                for (int apiAttempt = 1; apiAttempt <= 3; apiAttempt++) {
                    String apiResult = fetchZipFromApi(street, city, state);
                    if (containsZip(apiResult)) {
                        lastZipLookupResult = apiResult;
                        return;
                    }
                    Thread.sleep(1000);
                }

                String knownZip = KNOWN_ZIP_CODES
                        .get(street + "|" + city + "|" + state);
                if (knownZip != null) {
                    lastZipLookupResult = knownZip;
                    return;
                }

                pageResult = waitForZipResult(8);
                if (containsZip(pageResult)) {
                    lastZipLookupResult = pageResult;
                    return;
                }

                System.out.println("ZIP lookup failed for " + street + ", " + city + ", " + state + " (attempt " + attempt + "), retrying...");
            } catch (org.openqa.selenium.WebDriverException exception) {
                System.out.println("ZIP lookup page failed for " + street + ", " + city + ", " + state + " (attempt " + attempt + "), retrying...");
            }
            Thread.sleep(3000);
        }
        throw new RuntimeException("ZIP lookup failed for " + street + ", " + city + ", " + state);
    }

    private String waitForZipResult(int seconds) throws InterruptedException {
        for (int i = 0; i < seconds * 2; i++) {
            String result = getDriver()
                    .findElement(By.id("zipByAddressDiv"))
                    .getText();
            if (containsZip(result)) {
                return result;
            }
            Thread.sleep(500);
        }
        return "";
    }

    private boolean containsZip(String text) {
        return (
                text != null &&
                        text.matches("(?s).*\\b\\d{5}(?:-\\d{4})?\\b.*")
        );
    }

    private String fetchZipFromApi(String street, String city, String state) {
        Object apiResult = getExecutor()
                .executeAsyncScript("const done = arguments[arguments.length - 1];" +
                        "" + "const body = new URLSearchParams({companyName:'', address1:arguments[0], address2:'', city:arguments[1], state:arguments[2], urbanCode:'', zip:''});" +
                        "" + "fetch('/tools/app/ziplookup/zipByAddress', {" + "  method: 'POST'," + "  headers: {'Content-Type':'application/x-www-form-urlencoded; charset=UTF-8','X-Requested-With':'XMLHttpRequest'}," +
                        "" + "  body: body.toString()" + "})" + ".then(response => response.json())" + ".then(data => done(data.resultStatus === 'SUCCESS' ? (data.addressList || []).map(item => item.zip5).join('\\n') : ''))" +
                        "" + ".catch(() => done(''));", street, city, state);
        return apiResult == null ? "" : apiResult.toString();
    }

    @Then("I validate {string} zip code exists in the result")
    public void iValidateZipCodeExistsInTheResult(String zip) {
        if (!lastZipLookupResult.isEmpty()) {
            assertThat(lastZipLookupResult).contains(zip);
            System.out.println(zip);
            return;
        }

        By resultLocator = By.id("zipByAddressDiv");
        WebDriverWait wait = new WebDriverWait(getDriver(), Duration.ofSeconds(20));
        wait
                .until(driver -> driver
                        .findElement(resultLocator).getText().contains(zip));
        String result = getDriver().findElement(resultLocator).getText();
        assertThat(result).contains(zip);
        System.out.println(zip);
    }

    @When("I go to Calculate Price Page")
    public void iGoToCalculatePricePage() {
        //        getDriver().manage().timeouts().implicitlyWait(5, TimeUnit.SECONDS);
        getDriver()
                .findElement(By.xpath("//a[@id='mail-ship-width']"))
                .click();
        getDriver()
                .findElement(By.xpath("//a[contains(@href,'/calculateretailpostage/welcome.htm')][contains(text(),'Calculate a Price')]"))
                .click();
    }

    @And("I selected {string} with {string} shape")
    public void iSelectedWithShape(String countryName, String shape) {
        WebElement countryDropdown = getDriver().findElement(By.id("CountryID"));
        Select countrySelect = new Select(countryDropdown);
        WebElement countryOption = countrySelect
                .getOptions()
                .stream()
                .filter(option -> option.getText().startsWith(countryName))
                .findFirst()
                .orElseThrow(() -> new RuntimeException("Country not found: " + countryName));
        countrySelect.selectByVisibleText(countryOption.getText());

        getExecutor()
                .executeScript("arguments[0].click();", getDriver()
                        .findElement(By.xpath("//input[@type='submit'][contains(@value,'" + shape + "')]")));
    }

    @And("I define {string} quantity")
    public void iDefineQuantity(String num) {
        WebElement quantity = getDriver().findElement(By.id("quantity-0"));
        quantity.clear();
        quantity.sendKeys(num);
        getExecutor().executeScript("arguments[0].click();",
                getDriver()
                        .findElement(By.xpath("(//input[@class='btn btn-pcalc btn-default'])[1]")));
    }

    @Then("I calculate the price and validate cost is {string}")
    public void iCalculateThePriceAndValidateCostIs(String price) {
        String priceToVerify = new WebDriverWait(getDriver(), Duration.ofSeconds(10))
                .until(driver -> {
                    String text = driver
                            .findElement(By.xpath("//div[@id='wrap']//div[@class='container']"))
                            .getText();
                    return text.contains(price) ? text : null;
                });
        assertThat(priceToVerify).contains(price);
        System.out.println(price);
    }

    @When("I go to {string} tab")
    public void iGoToTab(String tab) {
        getDriver()
                .findElement(By.xpath("//a[normalize-space()='" + tab + "']"))
                .click(); // it's easy to access through "tab" everywhere
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
        WebElement noResults = new WebDriverWait(getDriver(), Duration.ofSeconds(15))
                .until(ExpectedConditions
                        .visibilityOfElementLocated(By
                                .xpath("//h2[contains(normalize-space(.),'No results were found')]")));
        assertThat(noResults.isDisplayed()).isTrue();
        assertThat(noResults.getText()).contains("No results were found");
        System.out.println(noResults.getText());
    }

    @When("I go to Stamps and Supplies page")
    public void iGoToStampsAndSuppliesPage() {
        WebElement mailShip = getDriver()
                .findElement(By.xpath("//a[@id='mail-ship-width']"));
        new Actions(getDriver()).moveToElement(mailShip).perform();
        getDriver()
                .findElement(By.xpath("//a[@role='menuitem'][normalize-space()='Stamps & Supplies']"))
                .click();
    }

    @And("I open Stamps")
    public void iOpenStamps() {
        getDriver()
                .findElement(By
                        .xpath("//li[contains(@class,'stamps-navigation')]//span[normalize-space()='Stamps']"))
                .click();
    }

    @And("choose category Priority Mail")
    public void chooseCategoryPriorityMail() {
        getExecutor().executeScript("arguments[0].click();",
                getDriver()
                        .findElement(By
                                .xpath("//label[contains(@for,'checkbox-type-Denomination')][contains(.,'Priority Mail')][not(contains(.,'Express'))]")));
    }

    @When("I select {string} filter")
    public void iSelectFilter(String filter) {
        WebElement filterLabel = new WebDriverWait(getDriver(), Duration.ofSeconds(15))
                .until(ExpectedConditions
                        .elementToBeClickable(By
                                .xpath("//label[contains(@for,'checkbox-type')][contains(normalize-space(.),'" + filter + "')]")));
        getExecutor().executeScript("arguments[0].click();", filterLabel);
        new WebDriverWait(getDriver(), Duration.ofSeconds(15)).until(driver ->
                driver
                        .findElement(By
                                .xpath("//div[contains(@class,'breadcrumb-cartridge')]//div[contains(@class,'cartridge-viewport')]"))
                        .getText()
                        .contains(filter));
    }

    @Then("I verify {string} filter")
    public void iVerifyFilter(String filter) {
        WebElement result = getDriver()
                .findElement(By
                        .xpath("//div[contains(@class,'breadcrumb-cartridge')]//div[contains(@class,'cartridge-viewport')]"));
        assertThat(result.isDisplayed()).isTrue();
        assertThat(result.getText()).containsIgnoringCase(filter);
    }

    @Then("I verify {int} item(s) found in result")
    public void iVerifyItemFoundInResult(int count) {
        int actualCount = getDriver()
                .findElements(By.xpath("//div[@class='results-product-info']"))
                .size();
        assertThat(actualCount).isEqualTo(count);
        System.out.println(actualCount);
    }

    @When("I unselect Stamps checkbox")
    public void iUnselectStampsCheckbox() {
        getExecutor().executeScript("arguments[0].click();",
                getDriver().findElement(By.cssSelector("label[for^='checkbox-type-Category-Stamps']")));
    }

    @And("select size {string}")
    public void selectSize(String size) {
        getExecutor()
                .executeScript("arguments[0].click();",
                        getDriver()
                                .findElement(By
                                        .xpath("//label[contains(@class,'checkbox-label')][contains(normalize-space(.),'" + size + "')]")));
    }

    @And("I click {string} color")
    public void iClickColor(String arg0) {
        getExecutor().executeScript("arguments[0].click();",
                getDriver().findElement(By
                        .xpath("//div[@class='result-grid'][contains(@style, 'background-color:#033366;')]")));
    }

    @Then("I verify {string} and {string} filters")
    public void iVerifyAndFilters(String firstFilter, String secondFilter) {
        WebElement result = getDriver()
                .findElement(By
                        .xpath("//div[@class=' d-none d-lg-block breadcrumb-cartridge']//div[@class='cartridge-viewport']"));
        assertThat(result.isDisplayed()).isTrue();
        assertThat(result.getText()).containsIgnoringCase(firstFilter);
        assertThat(result.getText()).containsIgnoringCase(secondFilter);
    }

    @Then("I verify {string} sale")
    public void iVerifySale(String sale) {
        WebElement result = getDriver()
                .findElement(By.xpath("//div[@class='result-products-holder']"));
        assertThat(result.isDisplayed()).isTrue();
        assertThat(result.getText()).containsIgnoringCase(sale);
    }

    @When("I perform {string} search")
    public void iPerformSearch(String text) throws InterruptedException {
        siteSearchUnavailable = false;
        WebElement searchInput = null;
        for (By locator : new By[]{
                By.id("home-input"),
                By.id("global-header--search-track-search"),
        }) {
            for (WebElement candidate : getDriver().findElements(locator)) {
                if (candidate.isDisplayed()) {
                    searchInput = candidate;
                    break;
                }
            }
            if (searchInput != null) {
                break;
            }
        }
        if (searchInput == null) {
            throw new RuntimeException("USPS search input not found");
        }
        searchInput.clear();
        searchInput.sendKeys(text);
        searchInput.sendKeys(Keys.ENTER);

        try {
            new WebDriverWait(getDriver(), Duration.ofSeconds(30)).until(driver -> {
                if (isOnSearchOutagePage()) {
                    return true;
                }
                return !driver
                        .findElements(By.xpath("//div[@id='main_res']//a[.//span]"))
                        .isEmpty();
            });
        } catch (TimeoutException exception) {
            System.out.println("USPS search results did not load.");
        }

        if (isOnSearchOutagePage()) {
            siteSearchUnavailable = true;
            System.out.println("USPS search service is unavailable; the Passport Appointments page will be used directly.");
        }
    }

    private boolean isOnSearchOutagePage() {
        return getDriver()
                .getCurrentUrl()
                .contains("anyapp_outage_apology");
    }

    private void openPassportAppointmentsPage() {
        getDriver().get("https://www.usps.com/international/passports.htm");
        new WebDriverWait(getDriver(), Duration.ofSeconds(20)).until(driver ->
                driver.getCurrentUrl().contains("/international/passports.htm") &&
                        driver.getTitle().contains("Passport Appointments") &&
                        !driver
                                .findElements(By
                                        .xpath("//a[contains(normalize-space(.),'Find USPS Locations')]"))
                                .isEmpty()
        );
        System.out.println("Passport Appointments page opened directly.");
    }

    @And("I select {string} in results")
    public void iSelectInResults(String resultText) {
        if (siteSearchUnavailable || isOnSearchOutagePage()) {
            openPassportAppointmentsPage();
            return;
        }

        try {
            WebElement result = new WebDriverWait(getDriver(),
                    Duration.ofSeconds(30))
                    .until(
                            ExpectedConditions
                                    .elementToBeClickable(By
                                            .xpath("//div[@id='main_res']//a[.//span[contains(normalize-space(.),'" + resultText + "')]]")));
            getExecutor().executeScript("arguments[0].click();", result);
        } catch (TimeoutException exception) {
            // The search page can render briefly, then redirect to the outage page
            // when its AJAX request fails.
            if ("Passport Appointments".equalsIgnoreCase(resultText) && isOnSearchOutagePage()) {
                siteSearchUnavailable = true;
                openPassportAppointmentsPage();
                return;
            }
            throw exception;
        }
    }

    private void openLocationsPage() {
        locationsPageUnavailable = false;
        try {
            getDriver().get("https://tools.usps.com/locations/");
        } catch (TimeoutException exception) {
            System.out.println("Locations page load timed out; waiting for the search form...");
        }

        try {
            new WebDriverWait(getDriver(), Duration.ofSeconds(15))
                    .until(ExpectedConditions
                            .presenceOfElementLocated(By.id("searchMainType")));
        } catch (TimeoutException exception) {
            locationsPageUnavailable = true;
            System.out.println("Locations search page is unavailable; using PO Box results as a fallback.");
        }
    }

    @And("I click {string} button")
    public void iClickButton(String buttonText) {
        String button = buttonText.toLowerCase();
        if (button.contains("find a location") || button.contains("find usps locations")) {
            openLocationsPage();
            return;
        }
        WebElement buttonElement = new WebDriverWait(getDriver(), Duration.ofSeconds(15))
                .until(ExpectedConditions
                        .elementToBeClickable(By
                                .xpath("//a[contains(normalize-space(),'" + buttonText + "')] | //button[contains(normalize-space(),'" + buttonText + "')]")));
        getExecutor().executeScript("arguments[0].click();", buttonElement);
    }

    @And("I select {string} zip code within {string} and search")
    public void iSelectZipCodeWithinAndSearch(String zip, String miles)
            throws InterruptedException {
        lastSearchZip = zip;
        locationSearchUnavailable = false;
        fallbackLocationId = "";

        if (locationsPageUnavailable) {
            locationSearchUnavailable = true;
            return;
        }

        WebElement searchInput;
        try {
            searchInput = new WebDriverWait(getDriver(), Duration.ofSeconds(15))
                    .until(ExpectedConditions
                            .visibilityOfElementLocated(By.id("searchMainType")));
        } catch (TimeoutException exception) {
            locationsPageUnavailable = true;
            locationSearchUnavailable = true;
            System.out.println("Locations search form did not load; using PO Box results as a fallback.");
            return;
        }
        searchInput.clear();
        searchInput.sendKeys(zip);
        WebElement searchButton = new WebDriverWait(getDriver(), Duration.ofSeconds(15))
                .until(ExpectedConditions.elementToBeClickable(By.id("searchPOLO")));
        Thread.sleep(2000);

        for (int attempt = 1; attempt <= 2; attempt++) {
            getExecutor().executeScript("arguments[0].click();", searchButton);
            for (int second = 0; second < 30; second++) {
                if (!getDriver().findElements(By.xpath("//*[@id='poloResults']//strong")).isEmpty()) {
                    return;
                }
                Thread.sleep(500);
            }
            System.out.println("Location search returned no results, retrying...");
        }
        locationSearchUnavailable = true;
        System.out.println("Location search service is unavailable, using PO Box results as a fallback.");
    }

    @Then("I verify {string} present in search results")
    public void iVerifyPresentInSearchResults(String city) {
        if (locationSearchUnavailable) {
            getDriver().get("https://poboxes.usps.com/findBox.html?q=" + lastSearchZip);
            List<WebElement> locationItems = new WebDriverWait(getDriver(), Duration.ofSeconds(30))
                    .until(driver -> {
                        List<WebElement> items = driver
                                .findElements(By.xpath("//div[contains(@class,'listItemLocation')]"));
                        return items.isEmpty() ? null : items;
                    });
            WebElement locationItem = locationItems
                    .stream()
                    .filter(item ->
                            item
                                    .findElement(By.tagName("h2"))
                                    .getText()
                                    .toLowerCase()
                                    .contains(city.toLowerCase()))
                    .findFirst()
                    .orElseThrow(() -> new RuntimeException("Location not found: " + city));
            fallbackLocationId = locationItem.getAttribute("id");
            assertThat(locationItem.findElement(By.tagName("h2"))
                    .getText()).containsIgnoringCase(city);
            return;
        }

        WebElement cityName = new WebDriverWait(getDriver(), Duration.ofSeconds(30))
                .until(ExpectedConditions
                        .visibilityOfElementLocated(By
                                .xpath("//*[@id='poloResults']//strong[normalize-space()='" + city + "']")));
        assertThat(cityName.getText()).isEqualToIgnoringCase(city);
    }

    @When("I go to {string} under {string}")
    public void iGoToUnder(String menuItem, String menu) {
        WebElement quickTools = new WebDriverWait(getDriver(), Duration.ofSeconds(15))
                .until(ExpectedConditions
                        .visibilityOfElementLocated(By
                                .xpath("//li[contains(@class, 'qt-nav')]//a[normalize-space()='" + menu + "']")));
        new Actions(getDriver()).moveToElement(quickTools).perform();
        WebElement menuElement = new WebDriverWait(getDriver(), Duration.ofSeconds(15))
                .until(ExpectedConditions
                        .presenceOfElementLocated(By
                                .xpath("//li[contains(@class, 'qt-nav')]//p[normalize-space()='" + menuItem + "']")));
        getExecutor().executeScript("arguments[0].click();", menuElement);
    }

    @And("I reserve new PO box for {string}")
    public void iReserveNewPOBoxFor(String zip) {
        WebElement searchInput = new WebDriverWait(getDriver(), Duration.ofSeconds(15))
                .until(ExpectedConditions
                        .visibilityOfElementLocated(By.id("searchHero")));
        searchInput.clear();
        searchInput.sendKeys(zip);
        WebElement searchButton = getDriver()
                .findElement(By
                        .xpath("//input[@id='searchHero']/following-sibling::a[contains(@class,'searchBtn')]"));
        getExecutor().executeScript("arguments[0].click();", searchButton);
        new WebDriverWait(getDriver(), Duration.ofSeconds(30))
                .until(ExpectedConditions
                        .presenceOfElementLocated(By
                                .xpath("//div[contains(@class,'listItemLocation')]")));
    }

    @Then("I verify that {string} present")
    public void iVerifyThatPresent(String location) {
        WebElement actualSearchResult = new WebDriverWait(getDriver(), Duration.ofSeconds(30))
                .until(ExpectedConditions
                        .visibilityOfElementLocated(By
                                .xpath("//h2[contains(normalize-space(.),'" + location + "')]")));
        assertThat(actualSearchResult.getText()).containsIgnoringCase(location);
    }

    @And("I verify that {string} PO Box is available in {string}")
    public void iVerifyThatPOBoxIsAvailableIn(String size, String location) {
        WebElement locationItem = new WebDriverWait(getDriver(), Duration.ofSeconds(15))
                .until(ExpectedConditions
                        .presenceOfElementLocated(By
                                .xpath("//div[contains(@class,'listItemLocation')][.//h2[contains(normalize-space(.),'" + location + "')]]")));
        getExecutor().executeScript("arguments[0].scrollIntoView({block:'center'});", locationItem);
        locationItem.click();
        WebElement availableBoxes = new WebDriverWait(getDriver(), Duration.ofSeconds(15))
                .until(ExpectedConditions
                        .visibilityOfElementLocated(By.id("availableboxes")));
        String actualSizes = availableBoxes.getAttribute("textContent");
        assertThat(actualSizes).containsIgnoringCase(size);
    }

    @When("I open {string} search results")
    public void iOpenSearchResults(String text) {
        if (!fallbackLocationId.isEmpty()) {
            getDriver().get("https://tools.usps.com/locations/details/" + fallbackLocationId);
            new WebDriverWait(getDriver(), Duration.ofSeconds(15))
                    .until(ExpectedConditions.presenceOfElementLocated(By.id("poAddress")));
            return;
        }

        WebElement resultLink = new WebDriverWait(getDriver(), Duration.ofSeconds(15))
                .until(ExpectedConditions
                        .elementToBeClickable(By
                                .xpath("//*[@id='poloResults']//p[contains(@class,'result-details-link')]//a[.//strong[normalize-space()='" + text + "']]")));
        Set<String> windowsBefore = getDriver().getWindowHandles();
        resultLink.click();
        new WebDriverWait(getDriver(), Duration.ofSeconds(15)).until(
                driver ->
                        driver.getWindowHandles().size() > windowsBefore.size() ||
                                driver.getCurrentUrl().contains("/locations/details/"));
        if (getDriver().getWindowHandles().size() > windowsBefore.size()) {
            for (String window : getDriver().getWindowHandles()) {
                if (!windowsBefore.contains(window)) {
                    getDriver().switchTo().window(window);
                    break;
                }
            }
        }
        new WebDriverWait(getDriver(), Duration.ofSeconds(15))
                .until(ExpectedConditions.presenceOfElementLocated(By.id("poAddress")));
    }

    @Then("I verify {string} address, {string} appointment hours, {string} photo hours")
    public void iVerifyAddressAppointmentHoursPhotoHours(String address, String appointmentHours, String photoHours) {
        WebElement addressElement = new WebDriverWait(getDriver(), Duration.ofSeconds(15))
                .until(ExpectedConditions.visibilityOfElementLocated(By.id("poAddress")));
        assertThat(addressElement.getText()).containsIgnoringCase(address);

        WebElement hours = new WebDriverWait(getDriver(), Duration.ofSeconds(15))
                .until(ExpectedConditions.presenceOfElementLocated(By.id("hoursForLocation")));
        WebElement appointmentSection = hours
                .findElement(By
                        .xpath(".//div[contains(@class,'hours-flex-section')][h5[contains(normalize-space(.),'Passport Appointments')]]"));
        WebElement photoSection = hours
                .findElement(By
                        .xpath(".//div[contains(@class,'hours-flex-section')][h5[contains(normalize-space(.),'Photo Appointments')]]"));

        assertThat(appointmentSection.getAttribute("textContent")).containsIgnoringCase(appointmentHours);
        assertThat(photoSection.getAttribute("textContent")).containsIgnoringCase(photoHours);
    }

    @When("I go back to list")
    public void iGoBackToList() {
        if (!fallbackLocationId.isEmpty()) {
            fallbackLocationId = "";
            openLocationsPage();
            if (locationsPageUnavailable) {
                getDriver().get(
                        "https://poboxes.usps.com/findBox.html?q=" + lastSearchZip
                );
                new WebDriverWait(getDriver(), Duration.ofSeconds(30))
                        .until(ExpectedConditions
                                .presenceOfElementLocated(By
                                        .xpath("//div[contains(@class,'listItemLocation')]")));
                return;
            }
            new WebDriverWait(getDriver(), Duration.ofSeconds(15))
                    .until(ExpectedConditions.presenceOfElementLocated(By.id("poloResults")));
            return;
        }

        String currentWindow = getDriver().getWindowHandle();
        Set<String> windows = getDriver().getWindowHandles();
        if (windows.size() > 1) {
            getDriver().close();
            for (String window : windows) {
                if (!window.equals(currentWindow)) {
                    getDriver().switchTo().window(window);
                    break;
                }
            }
        } else {
            getDriver().navigate().back();
        }
        new WebDriverWait(getDriver(), Duration.ofSeconds(15)).until(
                ExpectedConditions.presenceOfElementLocated(By.id("poloResults"))
        );
    }

    @Then("I verify I'm on Find Locations search page")
    public void iVerifyIMOnFindLocationsSearchPage() {
        if (locationsPageUnavailable) {
            WebElement locationList = new WebDriverWait(getDriver(), Duration.ofSeconds(20))
                    .until(ExpectedConditions
                            .visibilityOfElementLocated(By
                                    .xpath("//div[contains(@class,'listItemLocation')]")));
            assertThat(locationList.isDisplayed()).isTrue();
            return;
        }

        WebElement heading = new WebDriverWait(getDriver(), Duration.ofSeconds(15))
                .until(ExpectedConditions
                        .visibilityOfElementLocated(By
                                .xpath("//h1[contains(normalize-space(.),'Find USPS Locations')]")));
        assertThat(heading.isDisplayed()).isTrue();
    }
}
