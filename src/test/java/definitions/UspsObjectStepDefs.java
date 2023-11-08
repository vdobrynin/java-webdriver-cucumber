package definitions;

import io.cucumber.java.en.And;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import org.openqa.selenium.By;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.WebElement;
import pages.Usps;
import pages.UspsPostalStore;
import pages.UspsSignIn;
import pages.UspsTracking;

import java.text.ParseException;

import static org.assertj.core.api.Assertions.assertThat;
import static support.TestContext.getDriver;
import static support.TestContext.getExecutor;

public class UspsObjectStepDefs {

    @When("I go to {string} oop")
    public void iGoToOop(String buttonType) {

        switch (buttonType.toLowerCase()) {
            case "stamps":
                new Usps().clickStamps();
                break;
            case "boxes":
                new Usps().clickBoxes();
                break;
            case "labels":
                new Usps().clickLabels();
                break;
            case "tracking":
                new Usps().clickTracking();
                break;
            default:
                throw new RuntimeException("Unrecognized buttonType: " + buttonType);
        }
    }

    @And("I sort by {string} oop")
    public void iSortByOop(String text) {

        boolean isPresent;            // for holidays
        try {
            WebElement holiday = getDriver()
                .findElement(By.xpath("(//span[normalize-space()='Holiday'])[1]"));
            isPresent = holiday.isDisplayed();
            new UspsPostalStore().click(holiday);
        } catch (NoSuchElementException e) {
            isPresent = false;
        }

        WebElement button = getDriver()
            .findElement(By.xpath("(//div[contains(@class,'dropdown-selection align-self-center open')])[1]"));
        new UspsPostalStore()
            .click(button);
        new UspsPostalStore().selectSortBy(text);
    }

    @Then("I verify that {string} is cheapest oop")
    public void iVerifyThatIsCheapestOop(String expected) throws ParseException, InterruptedException {

        boolean isFound = new UspsPostalStore()
            .isCheapestItem(expected);
        assertThat(isFound)
            .isTrue();
//                                                  //--> before Lecture #14
//        String actualItem = new UspsPostalStore()
//            .getFirstFoundItem();
//        System.out.println("Actual: " + actualItem);
//        System.out.println("Expected: " + expected);
//        assertThat(actualItem)
//            .contains(expected);
    }

    @And("I verify section {string} exists oop")
    public void iVerifySectionExistsOop(String section) throws InterruptedException {

        String filterText = new UspsPostalStore()
            .getLeftFilters();
        assertThat(filterText)
            .containsIgnoringCase(section);

        WebElement button = getDriver()
            .findElement(By.xpath("(//div[contains(@class,'dropdown-selection align-self-center open')])[1]"));
        getExecutor()
            .executeScript("arguments[0].scrollIntoView();", button);
    }

    @When("I go to {string} menu oop")
    public void iGoToMenuOop(String menu) {

        new Usps().clickMenuItem(menu);
    }

    @And("I search for {string} in store oop")
    public void iSearchForInStoreOop(String search) {

        new UspsPostalStore().searchFor(search);
    }

    @Then("I verify that {string} is required oop")
    public void iVerifyThatIsRequiredOop(String action) throws ParseException, InterruptedException {

        boolean isRequired = new UspsSignIn()
            .isSignInRequired();
        assertThat(isRequired)
            .isTrue();
    }

    @Then("I verify that {string} is possible oop")
    public void iVerifyThatIsPossibleOop(String action) {
//                                                            // Based on below URLs:
        // https://reg.usps.com/entreg/LoginAction_input
        // https://tools.usps.com/go/TrackConfirmAction

        String url = getDriver().getCurrentUrl();
        if (url.contains("Login")) {
            assertThat(new UspsSignIn()
                .isSignUpPossible())
                .isTrue();                      //--> page is login

        } else if (url.contains("Track")) {
            assertThat(new UspsTracking()
                .isSignUpPossible())
                .isFalse();                     // -→looking in the different page -→ tracking

        } else {
            throw new RuntimeException("Unrecognized Url: " + url);
        }
    }
}
