package hybridAutomation.Utilities;

import com.fasterxml.jackson.databind.JsonNode;
import io.restassured.builder.RequestSpecBuilder;
import io.restassured.http.ContentType;
import io.restassured.response.ValidatableResponse;
import io.restassured.specification.RequestSpecification;

import java.io.File;
import java.util.Map;

import static io.restassured.RestAssured.given;

public class Api {
    private String baseUri;
    private ContentType contentType;
    private Map<String, String> params;
    private Map<String, String> headers;
    private RequestSpecBuilder requestSpecBuilder = new RequestSpecBuilder();
    private RequestSpecification requestSpecification;
    public Api(String baseUri, ContentType contentType, Map<String, String> params, Map<String, String> headers) {
        this.baseUri= baseUri;
        this.params = params;
        this.headers = headers;
        this.contentType = contentType;
        buildRequestSpec();
    }

    private void buildRequestSpec() {
        setBaseUri(requestSpecBuilder);
        requestSpecification = requestSpecBuilder.build();
        if(params != null) {
            setQueryParams(params);
        }
        if(headers != null) {
            setHeaders(headers);
        }
    }

    private RequestSpecBuilder setBaseUri(RequestSpecBuilder requestSpecBuilder) {
        return requestSpecBuilder.setBaseUri(baseUri).setContentType(contentType);
    }

    public void setContentType(ContentType contentType) {
        requestSpecification.contentType(contentType);
    }

    public void setQueryParams(Map<String, String> params) {
        params.forEach((key,value)-> {
            requestSpecification.queryParam(key, value);
        });
    }

    public void setQueryParam(String key, String value) {
        requestSpecification.queryParam(key, value);
    }

    public void setHeaders(Map<String, String> params) {
        params.forEach((key,value)-> {
            requestSpecification.header(key, value);
        });
    }

    public void setHeader(String key, String value) {
        requestSpecification.header(key, value);
    }

    public void setPathParam(String key, String value) {
        requestSpecification.pathParam(key, value);
    }

    public void setParams(Map<String, String> params) {
        requestSpecification.params(params);
    }

    public void setMultimedia(Map<String, File> params) {
        params.forEach((key,value)-> {
            requestSpecification.multiPart(key, value);
        });
    }


    public JsonNode get(String urlPath) {
        JsonNode response = given().log().all().spec(requestSpecification)
                .when().get(urlPath)
                .then().log().all()
                .extract().response().as(JsonNode.class);
        return response;
    }

    public JsonNode post(String urlPath) {
        JsonNode response = given().log().all().spec(requestSpecification)
                .when().post(urlPath)
                .then().log().all()
                .extract().response().as(JsonNode.class);
        return response;
    }

    public JsonNode post(String urlPath, JsonNode body) {
        JsonNode response = given().log().all().spec(requestSpecification)
                .body(body)
                .when().post(urlPath)
                .then().log().all()
                .extract().response().as(JsonNode.class);
        return response;
    }

    public JsonNode delete(String urlPath, JsonNode body) {
        JsonNode response = given().log().all().spec(requestSpecification)
                .body(body)
                .when().delete(urlPath)
                .then().log().all()
                .extract().response().as(JsonNode.class);
        return response;
    }

    public JsonNode delete(String urlPath) {
        JsonNode response = given().log().all().spec(requestSpecification)
                .when().delete(urlPath)
                .then().log().all()
                .extract().response().as(JsonNode.class);
        return response;
    }

    public ValidatableResponse post(String urlPath, String body, int statusCode) {
        ValidatableResponse response = given().log().all().spec(requestSpecification)
                .body(body)
                .when().post(urlPath)
                .then().log().all().assertThat().statusCode(statusCode);
        return response;
    }

    public JsonNode put(String urlPath, JsonNode body) {
        JsonNode response = given().log().all().spec(requestSpecification)
                .body(body)
                .when().put(urlPath)
                .then().log().all()
                .extract().response().as(JsonNode.class);
        return response;
    }



}
