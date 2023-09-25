package support;

import io.restassured.RestAssured;
import io.restassured.path.json.JsonPath;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;

import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Date;
import java.util.List;
import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;

public class RestWrapper {

    private String baseUrl = "https://skryabin.com/recruit/api/v1/";
    private static String loginToken;

    private static Map<String, Object> lastPosition;

    private static JsonPath metadata;           // add to check & save metadata

    //value for all
    public static final String CONTENT_TYPE = "Content-Type";
    public static final String JSON = "application/json";
    public static final String AUTH = "Authorization";

    public static Map<String, Object> getLastPosition() {

        return lastPosition;                                // get to read data --> last position
    }

    public void login(Map<String, String> credentials) {

        // prepare
        RequestSpecification request = RestAssured
            .given()
            .log().all()
            .header(CONTENT_TYPE, JSON)
            .body(credentials);

        //execute
        Response response = request

            .post(baseUrl + "login");

        //verify and extract
        Map<String, String> result = response
            .then()
            .log().all()
            .statusCode(200)
            .extract()
            .jsonPath()
            .getMap("");                        // give me everything if "" all together

        loginToken = "Bearer " + result.get("token");
        getMetadata();                                      // added to grab metadata on login
    }

    public Map<String, Object> createPosition(Map<String, String> position) {

        String dateOpen = position.get("dateOpen");                           // convert date from US format to ISO format
        //System.out.println(dateOpen);                                           // simple check what we have
        LocalDate localDate = LocalDate.parse(dateOpen, DateTimeFormatter.ofPattern("MM/dd/yyyy"));
        String isoDateOpen = localDate.format(DateTimeFormatter.ofPattern("yyyy-MM-dd"));
        position.put("dateOpen", isoDateOpen);                                             // convert date back

        //prepare
        RequestSpecification request = RestAssured
            .given()
            .log().all()
            .header(CONTENT_TYPE, JSON)
            .header(AUTH, loginToken)
            .body(position);

        //execute
        Response response = request

            .post(baseUrl + "positions"); // ??? position

        //verify and extract
        Map<String, Object> result = response
            .then()
            .log().all()
            .statusCode(201)
            .extract()
            .jsonPath()
            .getMap("");

        lastPosition = result;
        return result;
    }

    public List<Map<String, Object>> getPositions() {

        //prepare
        RequestSpecification request = RestAssured
            .given()
            .log().all();                     // prep. request to get data for positions in the list

        // execute
        Response response = request.get(baseUrl + "positions");

        // extract and verify
        List<Map<String, Object>> result = response
            .then()
            .log().all()
            .statusCode(200)
            .extract()
            .jsonPath()
            .getList("");                         // full list if "", or specific numb. "68" example

        assertMetadata(result.get(0), "positions");           // i think it's to check if list not empty
        return result;
    }

    public Map<String, Object> updatePosition(Map<String, String> updatedFields, Object id) {

        //prepare
        RequestSpecification request = RestAssured
            .given()
            .log().all()
            .header(CONTENT_TYPE, JSON)
            .header(AUTH, loginToken)
            .body(updatedFields);

        //execute
        Response response = request

            .put(baseUrl + "positions/" + id);

        //verify and extract
        Map<String, Object> result = response
            .then()
            .log().all()
            .statusCode(200)
            .extract()
            .jsonPath()
            .getMap("");
        //lastPosition = result;              //--> can't do it cause it'll not shows all fields

        for (String key : result.keySet()) {         // use this ones to check key:keySet by "id"

            lastPosition.put(key, result.get(key));
        }
        //return null;  --> // stop using cause we add for/loop
        return result;
    }

    public Map<String, Object> getPositionById(Object id) {

        Map<String, Object> result = RestAssured
            .given()
            .log().all()
            .when()
            .get(baseUrl + "positions/" + id)
            .then()
            .log().all()
            .statusCode(200)
            .extract()
            .jsonPath()
            .getMap("");                        // putting everything in one block

        assertMetadata(result, "positions");          // check for candidates
        return result;
    }

    public void deletePositionById(Object id) {

        RestAssured
            .given()
            .log().all()
            .header(AUTH, loginToken)
            .when()
            .delete(baseUrl + "positions/" + id)
            .then()
            .log().all()
            .statusCode(204)
            .header("X-Frame-Options", "SAMEORIGIN");   // this extra & optional to check if it's deleted (i think absent)
    }

    public JsonPath getMetadata() {

        JsonPath result = RestAssured
            .given()
            .log().all()
            .when()
            .get(baseUrl + "swagger.json")
            .then()
            .log().headers()
            .extract()
            .jsonPath();

        metadata = result;                  // return metadata in result
        return result;
    }

    private static void assertMetadata(Map<String, Object> actual, String type) {
        //System.out.println(actual);
        for (String key : actual.keySet()) {

            if (actual.get(key) != null) {
                String actualDataType = actual.get(key).getClass().toString();  // converting to string
                //System.out.println(metadata);

                actualDataType = actualDataType.substring(actualDataType.lastIndexOf('.') + 1);                // read data after "."

                String expectedDataType = metadata.getString("definitions." + type + ".properties." + key + ".type");   // read type & key, after "."
                assertThat(actualDataType).isEqualToIgnoringCase(expectedDataType);                     // universal for any data type
            }
        }
    }

    public Map<String, Object> createCandidate(Map<String, String> candidate) {

        String dateOpen = candidate.get("dateOpen");                           // convert date from US format to ISO format
        //System.out.println(dateOpen);                                           // simple check what we have
        String isoDateOpen = new SimpleDateFormat("yyyy-MM-dd").format(new Date(dateOpen));
        candidate.put("dateOpen", isoDateOpen);                                              // convert date back

        //prepare
        RequestSpecification request = RestAssured
            .given()
            .log().all()
            .header(CONTENT_TYPE, JSON)
            .header(AUTH, loginToken)
            .body(candidate);

        //execute
        Response response = request

            .post(baseUrl + "positions"); // ??? position

        //verify and extract
        Map<String, Object> result = response
            .then()
            .log().all()
            .statusCode(201)
            .extract()
            .jsonPath()
            .getMap("");

        lastPosition = result;
        return result;
    }
}
