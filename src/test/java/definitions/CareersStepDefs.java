package definitions;

import cucumber.api.java.en.*;
import pages.*;
import support.*;

import java.util.List;
import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;
import static support.TestContext.getData;
import static support.TestContext.getTimestamp;

public class CareersStepDefs implements Loggable {

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
        throw new Exception("Page " + page +" not recognized!");
    }
  }

  @And("I login as {string}")
  public void iLoginAs(String role) {

    new Careers()
      .clickLogin()
      .login(getData(role));  // static polymorphism // take (Data) from recruiter file
        new Careers()
          .clickLogin()
          .fillUsername("owen@example.com")
          .fillPassword("welcome")
          .clickSubmit()
          .getLoggedInUser(); // example
  }

  @Then("I verify {string} login")
  public void iVerifyLogin(String role) {

    String actualName = new Careers().getLoggedInUser();
    assertThat(actualName).isEqualTo(getData(role).get("name"));  // take 'name' from recruiter file
  }

  @When("I create {string} position")
  public void iCreatePosition(String positionType) {

    Map<String, String> position = getData(positionType);
    String title = position.get("title");
    title = title + getTimestamp();
    position.put("title", title);

    getLogger().info("\nCreating position: " + position.get("title"));

    new Careers()
      .clickRecruit()
      .clickNewPositionButton()
      //.createPosition(getData(positionType)) //--> was before
      .createPosition(position);
  }

  @And("I verify {string} position created")
  public void iVerifyPositionCreated(String positionType) throws InterruptedException {

    String actualTitle = new CareersRecruit().getLastCreatedPositionTitle();
    String expectedTitle = getData(positionType).get("title") + getTimestamp();

    //System.out.println(actualTitle +" vs " + expectedTitle);
    assertThat(actualTitle).isEqualTo(expectedTitle);
  }

  @When("I apply to {string} position")
  public void iApplyToPosition(String positionType) {
    new Careers()
      .clickApply()
      .createCandidate(getData(positionType));

    new CareersCandidates()
      .setChosenPositionTitles();

    new CareersCandidates()
      .setApplyPositionButton();
  }

  @Then("I verify {string} profile is created")
  public void iVerifyProfileIsCreated(String profileCreated) {

    String actualName = new Careers().getCandidateName();
    String expectedName = getData(profileCreated).get("title") + getTimestamp();
    //String expectedName = "Slava Vlad Skryabin";
    // System.out.println(actualName +" vs " + expectedName);
    assertThat(expectedName).isEqualTo(actualName);
  }

  @And("I see {string} position in my jobs")
  public void iSeePositionInMyJobs(String positionTitle) throws InterruptedException {

    String actualTitle = new CareersMyJobs().getPositionName();
    String expectedTitle = getData(positionTitle).get("titlePrincipal");
    //System.out.println(actualTitle +" vs " + expectedTitle);
    assertThat(expectedTitle).isEqualTo(actualTitle);
  }

  @Given("I login to REST API as {string}")
  public void iLoginToRESTAPIAs(String role) {
    new RestWrapper().login(getData(role));
  }

  @When("I create via REST API {string} position")
  public void iCreateViaRESTAPIPosition(String type) {

    new RestWrapper().createPosition(getData(type));
  }

  @Then("I verify via REST API position is created")
  public void iVerifyViaRESTAPIPositionIsCreated() {

    List<Map<String, Object>> positions = new RestWrapper().getPositions();
    Map<String, Object> lastPosition = RestWrapper.getLastPosition();

    boolean isFound = false;
    for (Map<String, Object> position : positions) {                  // String, String I change to the Object -->

      if (position.get("id").equals(lastPosition.get("id"))) {
        isFound = true;

        for (String key : lastPosition.keySet()) {              // check key & keySet

          Object expected = lastPosition.get(key);
          Object actual = position.get(key);

          assertThat(actual).isEqualTo(expected);
        }
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
    new RestWrapper().updatePosition(getData(type),((Integer)RestWrapper.getLastPosition().get("id")).toString()); // with "Sasha K."
  }

  @Then("I verify via REST API position is updated")
  public void iVerifyViaRESTAPIPositionIsUpdated() {

    Map<String, Object> expectedPosition = RestWrapper.getLastPosition();
    Map<String, Object> actualPosition = new RestWrapper().getPositionById(expectedPosition.get("id"));

    for (String key : expectedPosition.keySet()) {    // check key & keySet

      Object expected = expectedPosition.get(key);
      Object actual = actualPosition.get(key);

      //System.out.println(actual +" vs " + expected);
      assertThat(actual).isEqualTo(expected);
    }
  }

  @And("I delete via REST API created position")
  public void iDeleteViaRESTAPICreatedPosition() {

    Map<String, Object> expectedPosition = RestWrapper.getLastPosition();
    new RestWrapper().deletePositionById(expectedPosition.get("id"));
//    new RestWrapper().deletePosition(((Integer) RestWrapper.getLastPosition().get("id")).toString()); // with "Sasha K."
  }

  @When("I create via REST API {string} candidate")
  public void iCreateViaRESTAPICandidate(String positionType) {

    new RestWrapper().createCandidate(getData(positionType));
  }
}
