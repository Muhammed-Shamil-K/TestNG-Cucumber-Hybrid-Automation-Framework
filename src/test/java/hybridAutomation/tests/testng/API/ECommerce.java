package hybridAutomation.tests.testng.API;

import hybridAutomation.Payloads.EComm;
import hybridAutomation.Utilities.Api;
import com.fasterxml.jackson.databind.JsonNode;
import io.restassured.http.ContentType;
import org.testng.Assert;
import org.testng.annotations.Test;

import java.io.File;
import java.util.Map;

public class ECommerce {

    @Test
    public void demoTest() {
        Api jsonApi = new Api("https://rahulshettyacademy.com", ContentType.JSON, null, null);

        //authorization
        JsonNode loginResponse = jsonApi.post("/api/ecom/auth/login",
                EComm.loginPayload("tve16ie048@cet.ac.in", "Shamil123"));
        Assert.assertEquals(loginResponse.get("message").asText(), "Login Successfully");
        String userId = loginResponse.get("userId").asText();
        String token = loginResponse.get("token").asText();

        //create product
        Api multimediaApi = new Api("https://rahulshettyacademy.com", ContentType.MULTIPART, null, null);
        Map<String, String> formData = Map.of("productName", "Macbook Pro", "productAddedBy", userId,
                "productCategory", "Electronics", "productSubCategory", "Laptop", "productPrice", "150000",
                "productDescription", "16 inch laptop", "productFor", "All");
        multimediaApi.setParams(formData);
        multimediaApi.setMultimedia(Map.of("productImage", new File("src/main/resources/Images/FileUpload.png")));
        multimediaApi.setHeaders(Map.of("authorization", token));
        JsonNode addProductResponse = multimediaApi.post("/api/ecom/product/add-product");
        Assert.assertEquals(addProductResponse.get("message").asText(), "Product Added Successfully");
        String productId = addProductResponse.get("productId").asText();

        //create order
        jsonApi.setHeaders(Map.of("authorization", token, "Content-type", "application/json"));
        JsonNode orderCreateResponse = jsonApi.post("/api/ecom/order/create-order", EComm.orderPayload("India", productId));
        Assert.assertEquals(orderCreateResponse.get("message").asText(), "Order Placed Successfully");
        String orderId = orderCreateResponse.get("orders").get(0).asText();

        //delete order
        multimediaApi.setPathParam("orderId", orderId);
        JsonNode orderDeleteResponse = multimediaApi.delete("/api/ecom/order/delete-order/{orderId}");
        Assert.assertEquals(orderDeleteResponse.get("message").asText(), "Orders Deleted Successfully");

        //delete product
        jsonApi.setPathParam("productId", productId);
        JsonNode deleteResponse = jsonApi.delete("/api/ecom/product/delete-product/{productId}");
        Assert.assertEquals(deleteResponse.get("message").asText(), "Product Deleted Successfully");
    }

    @Test
    public void deleteProduct() {
        Api jsonApi = new Api("https://rahulshettyacademy.com", ContentType.JSON, null, null);
        //authorization
        JsonNode loginResponse = jsonApi.post("/api/ecom/auth/login",
                EComm.loginPayload("tve16ie048@cet.ac.in", "Shamil123"));
        String userId = loginResponse.get("userId").asText();
        String token = loginResponse.get("token").asText();
        //delete product
        String productId = "638f360603841e9c9a4b8133";
        jsonApi.setHeaders(Map.of("authorization", token));
        jsonApi.setPathParam("productId", productId);
        JsonNode deleteResponse = jsonApi.delete("/api/ecom/product/delete-product/{productId}");
    }
}
