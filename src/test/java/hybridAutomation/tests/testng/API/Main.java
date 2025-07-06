package hybridAutomation.tests.testng.API;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

public class Main {


    public static void main(String[] args) throws JsonProcessingException {

        String jsonString = "{\"status\":\"OK\",\"place_id\":\"2676631d7bd54aba19ec25c9af20ea49\",\"scope\":\"APP\",\"reference\":\"4a43c49634189315f273d5536853c2b54a43c49634189315f273d5536853c2b5\",\"id\":\"4a43c49634189315f273d5536853c2b5\"}";

        ObjectMapper objectMapper = new ObjectMapper();
        JsonNode jsonNode = objectMapper.readTree(jsonString);
        String placeId = jsonNode.get("place_id").asText();

        System.out.println("Place ID: " + placeId);

    }
}