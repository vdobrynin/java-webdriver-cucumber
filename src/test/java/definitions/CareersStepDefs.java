package definitions;

import static org.assertj.core.api.Assertions.assertThat;
import static support.TestContext.getData;
import static support.TestContext.getTimestamp;

import com.fasterxml.jackson.core.JsonParseException;
import io.cucumber.java.en.And;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;

import java.text.ParseException;
import java.util.List;
import java.util.Map;

import pages.*;
import support.Loggable;
import support.RestWrapper;

public class CareersStepDefs implements Loggable {
    private String lastCreatedPositionTitle;
    private String lastCandidateName;

    @Given("^I open \"([^\"]*)\" page$")
    public void iOpenPage(String page) throws Throwable {
        switch (page) {
            case "careers":
                new Careers().open();
                break;
            case "sample":
                new QuoteForm().open();
                break;
            default:
                throw new Exception("Page " + page + " not recognized!");
        }
    }

    @And("I login as {string}")
    public void iLoginAs(String role) {
        //        Map<String, String> user = getData(role);  // change to below --> login(getData(role))
        new Careers().clickLogin();
        new CareersLogin().login(getData(role)); // static polymorphism // take (Data) from recruiter file
        //        new Careers()
        //            .clickLogin()
        //            .getLoggedInUser();             // example

        //        new Careers()
        //            .clickLogin()
        //            .fillUsername("owen@example.com")
        //            .fillPassword("welcome")
        //            .clickSubmit()
        //            .getLoggedInUser();             // this example we change at lecture #15.04
    }

    @Then("I verify {string} login")
    public void iVerifyLogin(String role) {
        String actualName = new Careers().getLoggedInUser();
        assertThat(actualName).isEqualTo(getData(role).get("name")); // take 'name' from recruiter file
    }

    @When("I create {string} position")
    public void iCreatePosition(String positionType) {
        Map<String, String> position = getData(positionType);
        String title = position.get("title");
        title = title + getTimestamp();
        position.put("title", title);
        lastCreatedPositionTitle = title;

        getLogger().info("\nCreating position: " + position.get("title"));
        new Careers()
                .clickRecruit()
                .clickNewPositionButton()
                //.createPosition(getData(positionType))        //--> was before
                .createPosition(position);
    }

    @And("I verify {string} position created")
    public void iVerifyPositionCreated(String positionType) {
        String expectedTitle = lastCreatedPositionTitle != null ? lastCreatedPositionTitle : getData(positionType)
                .get("title") + getTimestamp();
        String actualTitle = new CareersRecruit()
                .getCreatedPositionTitle(expectedTitle);
        assertThat(actualTitle).isEqualTo(expectedTitle);
    }

    @When("I apply to {string} position")
    public void iApplyToPosition(String positionType) {
        Map<String, String> candidate = getData(positionType);
        String email = candidate.get("email");
        int at = email.indexOf('@');
        candidate.put("email", email
                .substring(0, at) + "+" + System.currentTimeMillis() + email.substring(at));
        lastCandidateName = (candidate
                .get("firstName") + " " + candidate.get("middleName") + " " + candidate.get("lastName"))
                .replaceAll("\\s+", " ")
                .trim();

        new Careers().clickApply().createCandidate(candidate);
        new CareersCandidates().setChosenPositionTitles();
        new CareersCandidates().setApplyPositionButton();
    }

    @Then("I verify {string} profile is created")
    public void iVerifyProfileIsCreated(String profileCreated) {
        String actualName = new Careers().getCandidateName();
        String expectedName = lastCandidateName != null ? lastCandidateName : getData(profileCreated)
                .get("title") + getTimestamp();
        assertThat(actualName).isEqualTo(expectedName);
    }

    @And("I see {string} position in my jobs")
    public void iSeePositionInMyJobs(String positionTitle) {
        String expectedTitle = getData(positionTitle).get("titlePrincipal");
        String actualTitle = new CareersMyJobs()
                .getPositionName(expectedTitle);
        assertThat(actualTitle).contains(expectedTitle);
    }

    @Given("I login to REST API as {string}")
    public void iLoginToRESTAPIAs(String role) throws JsonParseException {
        new RestWrapper().login(getData(role));
    }

    @When("I create via REST API {string} position")
    public void iCreateViaRESTAPIPosition(String type) throws ParseException {
        new RestWrapper().createPosition(getData(type));
    }

    @Then("I verify via REST API position is in the list")
    public void iVerifyViaRESTAPIPositionIsInTheList() {
        List<Map<String, Object>> actualPositions = new RestWrapper().getPositions();
        Map<String, Object> lastPosition = RestWrapper.getLastPosition();

        boolean isFound = false;
        for (Map<String, Object> actualPosition : actualPositions) {
            // String, String I change to the Object -->
            if (actualPosition.get("id").equals(lastPosition.get("id"))) {
                isFound = true;
                for (String key : lastPosition.keySet()) {
                    // check key & keySet
                    Object expected = lastPosition.get(key);
                    Object actual = actualPosition.get(key);
                    System.out.println("Verifying field: " + key);
                    System.out.println("Expected: " + expected);
                    System.out.println("Actual: " + actual);
                    assertThat(actual).isEqualTo(expected);
                }
                break;
            }
        }
        assertThat(isFound).isTrue();
    }

    @When("I update via REST API {string} position")
    public void iUpdateViaRESTAPIPosition(String type) {
        //Map<String,Object> newPositionsFields = Map.of("title", "SDET", "address", "4970 ElCamino Real, #110"); // this is hard coding
        Map<String, String> newPositionFields = getData(type + "_updated");
        Object id = RestWrapper.getLastPosition().get("id");

        //new RestWrapper().updatePosition(newPositionFields, id);
        new RestWrapper().updatePosition(getData(type), ((Integer) RestWrapper.getLastPosition()
                .get("id")).toString()
        );                                                                  // with "Sasha K."
    }

    @Then("I verify via REST API position is updated")
    public void iVerifyViaRESTAPIPositionIsUpdated() {
        Map<String, Object> expectedPosition = RestWrapper.getLastPosition();
        Map<String, Object> actualPosition = new RestWrapper().getPositionById(
                expectedPosition.get("id")
        );

        for (String key : expectedPosition.keySet()) {
            // check key & keySet
            Object expected = expectedPosition.get(key);
            Object actual = actualPosition.get(key);
            System.out.println(actual + " vs " + expected);
            assertThat(actual).isEqualTo(expected);
        }
    }

    @And("I delete via REST API created position")
    public void iDeleteViaRESTAPICreatedPosition() {
        Map<String, Object> expectedPosition = RestWrapper.getLastPosition();
        new RestWrapper().deletePositionById(expectedPosition.get("id"));
    }

    @When("I create via REST API {string} candidate")
    public void iCreateViaRESTAPICandidate(String positionType) {
        new RestWrapper().createCandidate(getData(positionType));
    }

    @Then("I verify via REST API candidate is created")
    public void iVerifyViaRESTAPICandidateIsCreated() {
        Map<String, Object> expectedCandidate = RestWrapper.getLastCandidate();
        Map<String, Object> actualCandidate = new RestWrapper()
                .getCandidateById(expectedCandidate.get("id"));

        for (String key : expectedCandidate.keySet()) {
            if (key.equals("password")) {
                continue;
            }
            assertThat(actualCandidate.get(key)).isEqualTo(expectedCandidate.get(key));
        }
    }

    @And("I delete via REST API created candidate")
    public void iDeleteViaRESTAPICreatedCandidate() {
        new RestWrapper()
                .deleteCandidateById(RestWrapper.getLastCandidate().get("id"));
    }
}
