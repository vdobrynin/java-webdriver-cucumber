package definitions;

import io.cucumber.java.en.*;
import pages.Careers;
import pages.QuoteForm;
import pages.QuoteResult;
import pages.Usps;
import support.Loggable;

import static org.assertj.core.api.Assertions.assertThat;

public class QuoteObjectStepDefs implements Loggable {

    @Given("I go to {string} page oop")
    public void iGoToPageOop(String page) {
//        System.out.println("Navigating to " + page);
//        LogManager.getLogger(QuoteObjectStepDefs.class).info("Navigating to " + page);
//        getLogger().info("Navigating to " + page);                                    // ---> look above
        switch (page.toLowerCase()) {
            case "quote":
                new QuoteForm().open();
                break;
            case "careers":
                new Careers().open();
                break;
            case "usps":
                new Usps().open();
                break;
            default:
                throw new RuntimeException("Unknown page: " + page);
        }
    }

    @When("I fill out required fields oop")
    public void iFillOutRequiredFieldsOop() {

        QuoteForm form = new QuoteForm();
        form.fillUsername("skryabin");
        form.fillEmail("slava@skryabin.com");
        form.fillPassword("welcome");
        form.fillConfirmPassword("welcome");
        form.fillName("Slava", "Skryabin");
        form.fillName("Slava", "Vlad", "Skryabin");
        form.clickPrivacy();
    }

    @And("I submit the form oop")
    public void iSubmitTheFormOop() throws InterruptedException {

        new QuoteForm().clickSubmit();         //         // ---> submit the form
    }

    @Then("I verify required fields oop")
    public void iVerifyRequiredFieldsOop() {

        QuoteResult resultPage = new QuoteResult();      // define new page verification (construct the class wit h the name)
        String actualResultText = resultPage
            .getSectionResult();
        String actualPrivacyPolicy = resultPage
            .getPrivacyPolicy();

        assertThat(actualPrivacyPolicy)
            .isEqualTo("true");
        assertThat(actualResultText)
            .contains(("skryabin"));
        assertThat(actualResultText)
            .contains("slava@skryabin.com");
        assertThat(actualResultText)
            .doesNotContain("welcome");
        assertThat(actualResultText)
            .contains("Slava Vlad Skryabin");
    }
}
