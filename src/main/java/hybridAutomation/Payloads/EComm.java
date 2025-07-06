package hybridAutomation.Payloads;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class EComm {

    private static ObjectMapper mapper = new ObjectMapper();
    private static ObjectNode rootNode = mapper.createObjectNode();
    public static JsonNode loginPayload(String email, String password) {
        return rootNode.put("userEmail", email).put("userPassword", password);
    }

    public static JsonNode orderPayload(String country, String... productIds) {
        List<JsonNode> orders = new ArrayList<>();
        for(String productId: productIds) {
            JsonNode order = mapper.createObjectNode().put("country", country).put("productOrderedId", productId);
            orders.add(order);
        }
        return rootNode.putPOJO("orders", orders);
    }
}
