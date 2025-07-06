package hybridAutomation.Payloads;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import org.apache.commons.lang3.RandomStringUtils;

import java.util.Arrays;
import java.util.concurrent.ThreadLocalRandom;

public class Place {

    private static ObjectMapper mapper = new ObjectMapper();
    private static ObjectNode rootNode = mapper.createObjectNode();

    private static String random = RandomStringUtils.randomAlphanumeric(4);

    public static JsonNode addPlace() {
        JsonNode location = mapper.createObjectNode().put("lat", "-38.383494").put("lng", "33.427362");
        rootNode.put("location", location);
        rootNode.put("accuracy", "50");
        rootNode.put("name", "Neverland- " + RandomStringUtils.randomAlphanumeric(4));
        rootNode.put("phone number", "(+91) 983 893 3978");
        rootNode.put("address",   "21, side layout, " + RandomStringUtils.randomAlphanumeric(4) +" 09");
        rootNode.putPOJO("types", Arrays.asList("shoe park", "shop"));
        rootNode.put("website", "http://google.com");
        rootNode.put("language", "French-IN");
        return rootNode;
    }

    public static JsonNode updatePlace(String placeId, String updatedAddress) {
        rootNode.put("place_id", placeId);
        rootNode.put("address", updatedAddress);
        rootNode.put("key", "qaclick123");
        return rootNode;
    }

    public static JsonNode deletePlace(String placeId) {
        return rootNode.putPOJO("place_id", placeId);
    }
}
