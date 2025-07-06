package hybridAutomation.tests.testng.API;

import hybridAutomation.Core.TestProperties;
import hybridAutomation.Payloads.Place;
import hybridAutomation.Utilities.Api;
import com.fasterxml.jackson.databind.JsonNode;
import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import io.restassured.path.json.JsonPath;
import org.testng.Assert;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.text.MessageFormat;
import java.util.Map;

import static io.restassured.RestAssured.given;
 
public class Basics {

    @Test(dataProvider = "UserData")
    public void addUser(String userName, String userJob) {
        RestAssured.baseURI="https://reqres.in";
        JsonNode resp= given().log().all().
                header("Content-Type","application/json").
                body("{\n" +
                        "    \"name\": \"" + userName + "\",\n" +
                        "    \"job\": \"" + userJob +"\"\n" +
                        "}").
                when().
                post("/api/users").
                then().log().all().
                extract().response().as(JsonNode.class);
        String id=resp.get("id").asText();
        System.out.println("ID of the created Basics is " + id);
    }

    @DataProvider(name = "UserData")
    private Object[][] getData() {
        return new Object[][] {{"Name1", "Job1"}, {"Name2", "Job2"}, {"Name3", "Job3"}};
    }


    @Test
    public void demo() throws IOException {
        String data = new String(Files.readAllBytes(Paths.get("src/main/resources/json/user.json")));
        System.out.println(data);
        RestAssured.baseURI="https://reqres.in";
        JsonNode resp= given().log().all().
                header("Content-Type","application/json").
                body(data).
                when().
                post("/api/users").
                then().log().all().
                extract().response().as(JsonNode.class);
        String id=resp.get("id").asText();
        System.out.println("ID of the created Basics is " + id);

    }


    //this test made fail by making the resource token1 instead of token
    @Test
    @TestProperties(id = {"TC_1","TC_2"})
    public void OAuthTest() {
        RestAssured.baseURI = "https://rahulshettyacademy.com";

        //call to authorization server to get access token
        String responseFromOAuthServer = given().log().all()
                .formParams("client_id", "692183103107-p0m7ent2hk7suguv4vq22hjcfhcr43pj.apps.googleusercontent.com")
                .formParams("client_secret", "erZOWM9g3UtwNRj340YYaK_W")
                .formParams("grant_type", "client_credentials")
                .formParams("scope", "trust")
                .when().log().all().post("/oauthapi/oauth2/resourceOwner/token1")
                .then().log().all().extract().response().asString();
        JsonPath authResponse = new JsonPath(responseFromOAuthServer);
        String token = authResponse.get("access_token");
        System.out.println(token);

        // actual API call to the server

        String responseFromServer = given().log().all()
                .queryParam("access_token", token)
                .when().log().all().get("/oauthapi/getCourseDetails")
                .then().log().all().extract().response().asString();

        //print total number of web automation courses
        JsonPath serverResponse = new JsonPath(responseFromServer);
        int coursesNumber = serverResponse.get("courses.webAutomation.size()");
        String message = MessageFormat.format("There are {0} web automation courses in here", coursesNumber);
        System.out.println(message);
    }

    @Test
    public void demoTest() {
        Api api = new Api("https://rahulshettyacademy.com", ContentType.JSON, Map.of("key", "qaclick123"),
                Map.of("Content-Type", "application/json"));
        JsonNode postResponse = api.post("/maps/api/place/add/json", Place.addPlace());

        String placeId = postResponse.get("place_id").asText();
        String updatedAddress = "Updated Address";

        api.setQueryParams(Map.of("place_id", placeId));
        JsonNode putResponse = api.put("/maps/api/place/update/json", Place.updatePlace(placeId, updatedAddress));
        Assert.assertEquals(putResponse.get("msg").asText(), "Address successfully updated");

        JsonNode getResponse = api.get("/maps/api/place/get/json");
        Assert.assertEquals(getResponse.get("address").asText(), updatedAddress);

        JsonNode deleteResponse = api.delete("/maps/api/place/delete/json", Place.deletePlace(placeId));
        Assert.assertEquals(deleteResponse.get("status").asText(), "OK");

        JsonNode getResponseAfterDelete = api.get("/maps/api/place/get/json");
        Assert.assertEquals(getResponseAfterDelete.get("msg").asText(), "Get operation failed, looks like place_id  doesn't exists");
    }


}
