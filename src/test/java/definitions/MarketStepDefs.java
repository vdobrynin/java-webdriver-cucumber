package definitions;

import cucumber.api.java.en.And;
import cucumber.api.java.en.Given;
import cucumber.api.java.en.Then;
import cucumber.api.java.en.When;
import org.openqa.selenium.By;
import org.openqa.selenium.Dimension;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.logging.LogEntries;
import org.openqa.selenium.logging.LogEntry;
import support.TestContext;

import static org.assertj.core.api.Assertions.assertThat;
import static support.TestContext.getDriver;
import static support.TestContext.getExecutor;

public class MarketStepDefs {
    @Given("I open quote page")
    public void iOpenQuotePage() {
        getDriver().get("https://skryabin.com/market/quote.html");
    }

    @And("I print page details")
    public void iPrintPageDetails() {
        System.out.println(getDriver().getTitle());
        System.out.println(getDriver().getCurrentUrl());
        System.out.println(getDriver().getWindowHandles());
    }

    @When("I fill out required fields")
    public void iFillOutRequiredFields() throws InterruptedException {
        getDriver()
            .findElement(By.xpath("//input[@name='username']")).sendKeys("vskryabin");
        getDriver()
            .findElement(By.xpath("//input[@name='email']")).sendKeys("slava@skryabin.com");
        getDriver()
            .findElement(By.xpath("//input[@name='password']")).sendKeys("welcome");
        getDriver()
            .findElement(By.xpath("//input[@name='confirmPassword']")).sendKeys("welcome");
        getDriver()
            .findElement(By.xpath("//input[@id='name']")).click();
        getDriver()
            .findElement(By.xpath("//input[@id='firstName']")).sendKeys("Slava");
        getDriver()
            .findElement(By.xpath("//input[@id='lastName']")).sendKeys("Skryabin");
        getDriver()
            .findElement(By.xpath("//span[text()='Save']")).click();
        WebElement privacy = getDriver()
            .findElement(By.xpath("//input[@name='agreedToPrivacyPolicy']"));
        getExecutor().executeScript("arguments[0].click();", privacy);
    }

    @And("I submit the form")
    public void iSubmitTheForm() throws InterruptedException {
        getDriver().findElement(By.xpath("//button[@id='formSubmit']")).click();
    }

    @Given("I go to {string} page")
    public void iGoToPage(String page) {
        switch (page.toLowerCase()) {
            case "quote":
                getDriver().get("https://skryabin.com/market/quote.html");
                break;
            case "dice":
                getDriver().get("https://www.dice.com/jobs");
                break;
            case "yandex":
                getDriver().get("https://yandex.ru/");
                break;
            case "calculator":
                getDriver().get("http://www.calculator.net/");
                break;
            case "careers":
                getDriver().get("https://workday.wd5.myworkdayjobs.com/Workday");
                break;
            case "converter":
                getDriver().get("https://www.unitconverters.net/");
                break;
            case "google":
                getDriver().get("https://www.google.com/");
                break;
            case "usps":
                getDriver().get("https://www.usps.com/");
                break;
            case "yahoo":
                getDriver().get("https://www.yahoo.com/");
                break;
            case "ups":
                getDriver().get("https://www.ups.com/us/en/Home.page");
                break;
            default:
                throw new RuntimeException("Unsupported page: " + page);
        }
    }

    @And("I go back and forward, then refresh the page")
    public void iGoBackAndForwardThenRefreshThePage() {
        getDriver()
            .navigate()
            .back();
        getDriver()
            .navigate()
            .forward();
        getDriver()
            .navigate()
            .refresh();
    }

    @Then("I verify required fields")
    public void iVerifyRequiredFields() {
        String result = getDriver()
            .findElement(By.xpath("//div[@id='quotePageResult']//section")).getText();
        assertThat(result)
            .containsIgnoringCase("vskryabin");
        assertThat(result)
            .containsIgnoringCase("slava@skryabin.com");
        assertThat(result)
            .containsIgnoringCase("Slava");
        assertThat(result)
            .containsIgnoringCase("Skryabin");
        assertThat(result)
            .containsIgnoringCase("Slava Skryabin");
        assertThat(result)
            .doesNotContain("welcome");
        String privacyResult = getDriver()
            .findElement(By.xpath("//b[@name='agreedToPrivacyPolicy']")).getText();
        assertThat(privacyResult)
            .isEqualTo("true");    // it is text cause it's coming from the field
        System.out.println(result);
        System.out.println(privacyResult);
    }

    @And("I print console logs")
    public void iPrintConsoleLogs() throws InterruptedException {
        LogEntries logs = getDriver()
            .manage()
            .logs()
            .get("browser");
        System.out.println("Browser logs for " + getDriver().getCurrentUrl());
        for (LogEntry log : logs) {
            System.out.println(log);
        }
        System.out.println("-----------");
    }

    @And("I change resolution to {string}")
    public void iChangeResolutionTo(String pageSize) {
        if (pageSize.equals("phone")) {
            getDriver()
                .manage()
                .window()
                .setSize(new Dimension(400, 768));
        } else if (pageSize.equals("desktop")) {
            getDriver()
                .manage()
                .window()
                .setSize(new Dimension(1024, 768));
        }
    }

    @When("I fill out optional fields")
    public void iFillOutOptionalFields() {
        getDriver()
            .findElement(By.xpath("//input[@name='phone']"))
            .sendKeys("1234567890");
        getDriver()
            .findElement(By.xpath("//input[@id='dateOfBirth']"))
            .click();
        getDriver()
            .findElement(By.xpath("//select[@class='ui-datepicker-month']/option[@value='3']"))
            .click();
        getDriver()
            .findElement(By.xpath("//select[@class='ui-datepicker-month']/option[@value='10']"))
            .click();
        getDriver()
            .findElement(By.xpath("//select[@class='ui-datepicker-year']/option[@value='1975']"))
            .click();
        getDriver()
            .findElement(By.xpath("//input[@id='dateOfBirth']"))
            .sendKeys("10/03/1975");
        getDriver()
            .findElement(By.xpath("//select[@name='countryOfOrigin']/option[contains(text(),'Monaco')]"))
            .click();
        getDriver()
            .findElement(By.xpath("//*[@name='gender'][@value='male']"))
            .click();
        getDriver()
            .findElement(By.xpath("//input[@type='checkbox'][@name='allowedToContact']"))
            .isSelected();
        getDriver()
            .findElement(By.xpath("//textarea[@id='address']"))
            .sendKeys("4970 El Camino Real, Los Altos, CA 94022");
        getDriver()
            .findElement(By.xpath("//select[@name='carMake']//option[contains(text(),'BMW')]"))
            .click();
        WebElement privacyPolicy = getDriver()
            .findElement(By.xpath("//*[@name='agreedToPrivacyPolicy']"));
        TestContext
            .getExecutor()
            .executeScript("arguments[0].click();", privacyPolicy);
    }

    @And("I submit the page")
    public void iSubmitThePage() {
        getDriver()
            .findElement(By.xpath("//button[@id='formSubmit']"))
            .click();
    }

    @When("I verify email field behavior")
    public void iVerifyEmailFieldBehavior() {
        getDriver()
            .findElement(By.xpath("//input[@name='email']"))
            .clear();
        getDriver()
            .findElement(By.xpath("//input[@name='email']"))
            .sendKeys("slavaskryabin.com");
        getDriver()
            .findElement(By.xpath("//button[@id='formSubmit']"))
            .click();
        String emailError = getDriver()
            .findElement(By.xpath("//label[@id='email-error']"))
            .getText();
        assertThat(emailError)
            .contains("Please enter a valid email address.");
        WebElement emailField = getDriver()
            .findElement(By.xpath("//input[@name='email']"));
        emailField
            .clear();
//    emailField.sendKeys("It is wrong format");
//    emailField.clear(); //emailField.sendKeys(Keys.BACK_SPACE);
        emailField
            .sendKeys("slava@skryabin.com");

        boolean errorDisplayed = getDriver()
            .findElement(By.xpath("//label[@id='email-error']"))
            .isDisplayed();
        assertThat(errorDisplayed)
            .isFalse();
    }

    @And("I {string} agreement")
    public void iAgreement(String clickArg) {
        WebElement thirdPartyButton = getDriver()
            .findElement(By.xpath("//*[@id='thirdPartyButton']"));
        TestContext.getExecutor()
            .executeScript("arguments[0].click();", thirdPartyButton);
        if (clickArg.equals("accept")) {
            getDriver()
                .switchTo()
                .alert()
                .accept();
        } else if (clickArg.equals("dismiss")) {
            getDriver()
                .switchTo()
                .alert()
                .dismiss();
        }
    }

    @Then("I verify that fields values recorded correctly")
    public void iVerifyThatFieldsValuesRecordedCorrectly() {
        boolean resultDisplayed = getDriver()
            .findElement(By.xpath("//legend[@class='applicationResult']"))
            .isDisplayed();
        assertThat(resultDisplayed)
            .isTrue();
        String resultText = getDriver()
            .findElement(By.xpath("//div[@class='well form-container container-fluid']"))
            .getText();
        assertThat(resultText)
            .contains("vskryabin");
        assertThat(resultText)
            .contains("Skryabin");
        assertThat(resultText)
            .contains("slava@skryabin.com");
        assertThat(resultText)
            .contains("Slava Skryabin");
        assertThat(resultText)
            .doesNotContain("password");
        assertThat(resultText)
            .contains("[entered]");
    }

    @When("I {string} third party agreement")
    public void iThirdPartyAgreement(String action) throws InterruptedException {
        getDriver()
            .findElement(By.xpath("//button[@id='thirdPartyButton']"))
            .click();
        switch (action.toLowerCase()) {
            case "accept":
                getDriver()
                    .switchTo()
                    .alert()
                    .accept();
                break;
            case "reject":
                getDriver()
                    .switchTo()
                    .alert()
                    .dismiss();
                break;
            default:
                throw new RuntimeException("Unknown" + action);
        }
    }

    @And("I input {string} {string} as contact")
    public void  iInputAsContact(String firstName, String lastName) {
        getDriver()
            .switchTo()
            .frame("additionalInfo");               // --> switch to "iframe" & it has a name "additionalInfo"
        getDriver()
            .findElement(By.xpath("//input[@id='contactPersonName']"))
            .sendKeys(firstName + " " + lastName);
        getDriver()
            .findElement(By.xpath("//input[@id='contactPersonPhone']"))
            .sendKeys("1234567890");
        getDriver()
            .switchTo()
            .defaultContent();   // --> switch back to defaultContent (original page)
    }

    @And("I validate document {string} present")
    public void iValidateDocumentPresent(String document) {
        String savedWindowsHandle = getDriver()
            .getWindowHandle(); // --> remember current windows before switch to new window (tab)
//        System.out.println(savedWindowsHandle);
        getDriver()
            .findElement(By.xpath("//button[contains(@onclick,'window')]"))
            .click();                                                   // --> click to switch to the new tab open
        for (String handle : getDriver().getWindowHandles()) { // switch to new window (tab)-->(use getWindowHandles cause ti's will handles all of them)
            getDriver()
                .switchTo()
                .window(handle);
        }

        String actualText = getDriver()
            .findElement(By.xpath("//body"))
            .getText(); // --> (//body visible part of the page)
        assertThat(actualText)
            .contains(document);                            // verify if "document 2" or part
        getDriver()
            .switchTo()
            .window(savedWindowsHandle); // --> switch back to original window (tab)
    }
}
