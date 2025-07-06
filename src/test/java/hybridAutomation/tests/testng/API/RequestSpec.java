package hybridAutomation.tests.testng.API;

import hybridAutomation.Payloads.Place;
import com.fasterxml.jackson.databind.JsonNode;
import io.restassured.builder.RequestSpecBuilder;
import io.restassured.specification.RequestSpecification;
import org.hamcrest.Matchers;
import org.testng.Assert;
import org.testng.annotations.Test;

import static io.restassured.RestAssured.given;

public class RequestSpec {
    @Test
    private void demoTest() {
        RequestSpecification requestSpecification = new RequestSpecBuilder().setBaseUri("https://rahulshettyacademy.com")
                .addQueryParam("key", "qaclick123")
                .addHeader("Content-Type", "application/json")
                .build();
        JsonNode response = given().log().all().spec(requestSpecification)
                .body(Place.addPlace())
                .when().post("/maps/api/place/add/json")
                .then().log().all().assertThat().statusCode(200).body("scope", Matchers.equalTo("APP"))
                .header("server", "Apache/2.4.52 (Ubuntu)")
                .extract().response().as(JsonNode.class);
        String placeId = response.get("place_id").asText();
        String updatedAddress = "Updated Address";

        requestSpecification.queryParam("place_id", placeId);

        given().log().all().spec(requestSpecification)
                .body(Place.updatePlace(placeId, updatedAddress))
                .when().put("/maps/api/place/update/json")
                .then().log().all().assertThat().statusCode(200).body("msg", Matchers.equalTo("Address successfully updated"))
                .header("server", "Apache/2.4.52 (Ubuntu)");


        JsonNode getResponse = given().log().all().spec(requestSpecification)
                .when().get("/maps/api/place/get/json")
                .then().log().all().assertThat().statusCode(200)
                .header("server", "Apache/2.4.52 (Ubuntu)")
                .extract().response().as(JsonNode.class);
        Assert.assertEquals(getResponse.get("address").asText(), updatedAddress);

        given().log().all().spec(requestSpecification)
                .body(Place.deletePlace(placeId))
                .when().delete("/maps/api/place/delete/json")
                .then().log().all().assertThat().statusCode(200)
                .body("status", Matchers.equalTo("OK"))
                .header("server", "Apache/2.4.52 (Ubuntu)");

        given().log().all().spec(requestSpecification)
                .when().get("/maps/api/place/get/json")
                .then().log().all().assertThat().statusCode(404)
                .body("msg", Matchers.equalTo("Get operation failed, looks like place_id  doesn't exists"));
    }

}
