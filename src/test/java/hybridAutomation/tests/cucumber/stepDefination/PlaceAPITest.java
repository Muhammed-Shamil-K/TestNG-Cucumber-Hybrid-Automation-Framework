package hybridAutomation.tests.cucumber.stepDefination;

import hybridAutomation.Payloads.Place;
import hybridAutomation.enums.PlaceAPI;
import com.fasterxml.jackson.databind.JsonNode;
import io.cucumber.java.en.And;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import io.restassured.builder.RequestSpecBuilder;
import io.restassured.builder.ResponseSpecBuilder;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;
import io.restassured.specification.ResponseSpecification;

import static io.restassured.RestAssured.given;
import static org.testng.Assert.assertEquals;

public class PlaceAPITest {

    RequestSpecification requestSpec = new RequestSpecBuilder().setBaseUri("https://rahulshettyacademy.com")
            .addQueryParam("key", "qaclick123")
            .addHeader("Content-Type", "application/json")
            .build();

    ResponseSpecification responseSpec = new ResponseSpecBuilder()
            .expectHeader("server", "Apache/2.4.52 (Ubuntu)")
            .build();

    static Response response;

    private static String placeId;


    @Given("User has {string} payload")
    public void user_has_payload(String api) {
        JsonNode payload;
        switch (api) {
            case "AddPlace":
                payload = Place.addPlace();
                requestSpec = given().log().all().spec(requestSpec).body(payload);
                break;
            case "DeletePlace":
                payload = Place.deletePlace(placeId);
                requestSpec = given().log().all().spec(requestSpec).body(payload);
                break;
            default:
                requestSpec = given().log().all().spec(requestSpec);
                break;
        }
    }
    @When("User calls {string} API as {string} request with the payload")
    public void user_calls_api_as_request_with_the_payload(String apiName, String method) {
        PlaceAPI api = PlaceAPI.valueOf(apiName);
        if (method.equalsIgnoreCase("POST")) {
            response = requestSpec.when().post(api.getResource())
                    .then().log().all()
                    .spec(responseSpec)
                    .extract().response();
        } else if(method.equalsIgnoreCase("GET")) {
            requestSpec.param("place_id", placeId);
            response = requestSpec.when().get(api.getResource())
                    .then().log().all()
                    .spec(responseSpec)
                    .extract().response();
        } else if(method.equalsIgnoreCase("DELETE")) {
            response = requestSpec.when().delete(api.getResource())
                    .then().log().all()
                    .spec(responseSpec)
                    .extract().response();
        }

    }
    @Then("API call is success with status code as {string}")
    public void apiCallIsSuccessWithStatusCodeAs(String arg0) {
        assertEquals(response.getStatusCode(), Integer.parseInt(arg0));
    }
    @Then("{string} in response is {string}")
    public void in_response_is(String string1, String string2) {
        JsonNode jsonNode = response.as(JsonNode.class);
        assertEquals(jsonNode.get(string1).asText(), string2);
    }

    @And("{string} is returned in the response")
    public void isReturnedInTheResponse(String param) {
        JsonNode jsonNode = response.as(JsonNode.class);
        placeId = jsonNode.get(param).asText();
    }

    @Given("Basics has GetPlace payload")
    public void userHasGetPlacePayload() {
        JsonNode jsonNode = response.as(JsonNode.class);
        placeId = jsonNode.get("place_id").asText();
        requestSpec = given().log().all().spec(requestSpec).queryParam("place_id", placeId);
    }


}
