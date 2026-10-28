package support;

import static org.assertj.core.api.Assertions.assertThat;

import com.fasterxml.jackson.core.JsonParseException;
import io.restassured.RestAssured;
import io.restassured.path.json.JsonPath;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Date;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

public class RestWrapper {

  private String baseUrl = "https://skryabin.com/recruit/api/v1/";
  private static String loginToken;
  private static Map<String, Object> lastPosition;
  private static Map<String, Object> lastCandidate;
  private static JsonPath metadata; // add to check & save metadata

  //value for all
  public static final String CONTENT_TYPE = "Content-Type";
  public static final String JSON = "application/json";
  public static final String AUTH = "Authorization";

  public static Map<String, Object> getLastPosition() {
    return lastPosition; // get to read data ---> last position
  }

  public static Map<String, Object> getLastCandidate() {
    return lastCandidate;
  }

  public void login(Map<String, String> credentials) throws JsonParseException {
    Response response = null;
    for (int attempt = 1; attempt <= 3; attempt++) {
      response = RestAssured.given()
        .log()
        .all()
        .baseUri(baseUrl)
        .header(CONTENT_TYPE, JSON)
        .body(credentials)
        .when()
        .post("login");

      if (response.statusCode() == 200) {
        break;
      }
      try {
        Thread.sleep(2000);
      } catch (InterruptedException exception) {
        Thread.currentThread().interrupt();
      }
    }

    //verify and extract
    Map<String, Object> result = response
      .then()
      .log()
      .all()
      .statusCode(200)
      .extract()
      .jsonPath()
      .getMap(""); // give me everything if "" all together

    loginToken = "Bearer " + result.get("token");
  }

  public Map<String, Object> createPosition(Map<String, String> position)
    throws ParseException {
    Map<String, String> positionFields = positionFields(position);
    String dateOpen = positionFields.get("dateOpen");
    String isoDateOpen = new SimpleDateFormat("yyyy-MM-dd").format(
      new SimpleDateFormat("MM/dd/yyyy").parse(dateOpen)
    );
    positionFields.put("dateOpen", isoDateOpen);

    String title = positionFields.get("title");
    String uniqueTitle =
      title + new SimpleDateFormat("-yyyy-MM-dd-HH-mm-sss").format(new Date());
    positionFields.put("title", uniqueTitle);

    //prepare
    RequestSpecification request = RestAssured.given()
      .log()
      .all()
      .baseUri(baseUrl)
      .header(CONTENT_TYPE, JSON)
      .header(AUTH, loginToken)
      .body(positionFields);

    //execute
    Response response = request.when().post("positions"); // ??? position
    //            .post(baseUrl + "positions"); // ??? position

    //verify and extract
    Map<String, Object> result = response
      .then()
      .log()
      .all()
      .statusCode(201)
      .extract()
      .jsonPath()
      .getMap("");

    lastPosition = result;
    return result;
  }

  public List<Map<String, Object>> getPositions() {
    //prepare
    RequestSpecification request = RestAssured.given().log().all(); // prep. request to get data for positions in the list

    // execute
    Response response = request.get(baseUrl + "positions");

    // extract and verify
    List<Map<String, Object>> result = response
      .then()
      .log()
      .all()
      .statusCode(200)
      .extract()
      .jsonPath()
      .getList(""); // full list if "", or specific numb. "68" example

    return result;
  }

  public Map<String, Object> updatePosition(
    Map<String, String> updatedFields,
    Object id
  ) {
    Map<String, String> positionFields = positionFields(updatedFields);
    String dateOpen = positionFields.get("dateOpen");
    if (dateOpen != null) {
      try {
        positionFields.put(
          "dateOpen",
          new SimpleDateFormat("yyyy-MM-dd").format(
            new SimpleDateFormat("MM/dd/yyyy").parse(dateOpen)
          )
        );
      } catch (ParseException exception) {
        throw new RuntimeException(exception);
      }
    }

    //prepare
    RequestSpecification request = RestAssured.given()
      .log()
      .all()
      .header(CONTENT_TYPE, JSON)
      .header(AUTH, loginToken)
      .body(positionFields);

    //execute
    Response response = request.put(baseUrl + "positions/" + id);

    //verify and extract
    Map<String, Object> result = response
      .then()
      .log()
      .all()
      .statusCode(200)
      .extract()
      .jsonPath()
      .getMap("");
    //lastPosition = result;              //--> can't do it cause it'll not shows all fields

    for (String key : result.keySet()) {
      // use this ones to check key:keySet by "id"

      lastPosition.put(key, result.get(key));
    }
    //return null;  --> // stop using cause we add for/loop
    return result;
  }

  public Map<String, Object> getPositionById(Object id) {
    Map<String, Object> result = RestAssured.given()
      .log()
      .all()
      .when()
      .get(baseUrl + "positions/" + id)
      .then()
      .log()
      .all()
      .statusCode(200)
      .extract()
      .jsonPath()
      .getMap(""); // putting everything in one block

    return result;
  }

  public void deletePositionById(Object id) {
    RestAssured.given()
      .log()
      .all()
      .header(AUTH, loginToken)
      .when()
      .delete(baseUrl + "positions/" + id)
      .then()
      .log()
      .all()
      .statusCode(204)
      .header("X-Frame-Options", "SAMEORIGIN"); // this extra & optional to check if it's deleted (i think absent)
  }

  public JsonPath getMetadata() {
    JsonPath result = RestAssured.given()
      .log()
      .all()
      .when()
      .get(baseUrl + "swagger.json")
      .then()
      .log()
      .headers()
      .extract()
      .jsonPath();

    metadata = result; // return metadata in result
    return result;
  }

  private static void assertMetadata(Map<String, Object> actual, String type) {
    //System.out.println(actual);
    for (String key : actual.keySet()) {
      if (actual.get(key) != null) {
        String actualDataType = actual.get(key).getClass().toString(); // converting to string
        //System.out.println(metadata);

        actualDataType = actualDataType.substring(
          actualDataType.lastIndexOf('.') + 1
        ); // read data after "."

        String expectedDataType = metadata.getString(
          "definitions." + type + ".properties." + key + ".type"
        ); // read type & key, after "."
        if (expectedDataType == null) {
          continue;
        }
        assertThat(actualDataType).isEqualToIgnoringCase(expectedDataType); // universal for any data type
      }
    }
  }

  public Map<String, Object> createCandidate(Map<String, String> candidate) {
    Map<String, String> candidateFields = candidateFields(candidate);
    String email = candidateFields.get("email");
    int at = email.indexOf('@');
    candidateFields.put(
      "email",
      email.substring(0, at) +
        "+" +
        System.currentTimeMillis() +
        email.substring(at)
    );

    //prepare
    RequestSpecification request = RestAssured.given()
      .log()
      .all()
      .baseUri(baseUrl)
      .header(CONTENT_TYPE, JSON)
      .header(AUTH, loginToken)
      .body(candidateFields);

    //execute
    Response response = request.post("candidates");

    //verify and extract
    Map<String, Object> result = response
      .then()
      .log()
      .all()
      .statusCode(201)
      .extract()
      .jsonPath()
      .getMap("");

    lastCandidate = result;
    return result;
  }

  public Map<String, Object> getCandidateById(Object id) {
    Map<String, Object> result = RestAssured.given()
      .log()
      .all()
      .when()
      .get(baseUrl + "candidates/" + id)
      .then()
      .log()
      .all()
      .statusCode(200)
      .extract()
      .jsonPath()
      .getMap("");

    return result;
  }

  public void deleteCandidateById(Object id) {
    RestAssured.given()
      .log()
      .all()
      .header(AUTH, loginToken)
      .when()
      .delete(baseUrl + "candidates/" + id)
      .then()
      .log()
      .all()
      .statusCode(204);
  }

  private Map<String, String> positionFields(Map<String, String> source) {
    return filterFields(
      source,
      List.of(
        "title",
        "description",
        "address",
        "city",
        "state",
        "zip",
        "dateOpen",
        "company"
      )
    );
  }

  private Map<String, String> candidateFields(Map<String, String> source) {
    return filterFields(
      source,
      List.of(
        "firstName",
        "middleName",
        "lastName",
        "email",
        "password",
        "address",
        "city",
        "state",
        "zip",
        "summary"
      )
    );
  }

  private Map<String, String> filterFields(
    Map<String, String> source,
    List<String> allowedFields
  ) {
    Map<String, String> result = new LinkedHashMap<>();
    for (String field : allowedFields) {
      if (source.containsKey(field)) {
        result.put(field, source.get(field));
      }
    }
    return result;
  }
}
