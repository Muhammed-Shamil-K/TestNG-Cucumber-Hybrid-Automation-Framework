package hybridAutomation.Utilities;

import hybridAutomation.Core.Mapper;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class JsonUtils {
    /*
    private static ObjectMapper mapper = new ObjectMapper();
    public static JsonPath rawToJson(String rawData) {
        JsonPath jsonPath = new JsonPath(rawData);
        return jsonPath;
    }

    public static List<JsonNode> toList(JsonNode jsonNode) throws IOException {
        ObjectReader reader = mapper.readerFor(new TypeReference<List<JsonNode>>() {});
        return reader.readValue(jsonNode);
    }

    public static JsonNode toJson(List<JsonNode> arrayNode) throws IOException {
        ObjectReader reader = mapper.readerFor(new TypeReference<JsonNode>() {});
        return reader.readValue(String.valueOf(arrayNode));
    }

    public static JsonNode readFromFile(String path) throws IOException {
        String data = new String(Files.readAllBytes(Paths.get(path)));
        return mapper.readTree(data);
    }

     */





    // added methods


    public ArrayNode createArrayNode() {
        return Mapper.mapper.createArrayNode();
    }

    public ObjectNode createObjectNode() {
        return Mapper.mapper.createObjectNode();
    }

    public JsonNode readFromResource(String path) {
        try {
            InputStream is = JsonUtils.class.getClassLoader().getResourceAsStream(path);
            if (is == null) {
                throw new IllegalArgumentException("Can't open " + path);
            }
            return Mapper.mapper.readTree(is);
        } catch (IOException e) {
            throw new IllegalArgumentException("Can't open " + path);
        }
    }

    public String toString(JsonNode jsonNode) {
        String nodeToString = "";
        try {
            nodeToString = Mapper.mapper.writeValueAsString(jsonNode);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
        return nodeToString;
    }

    public JsonNode parseString(String message) {
        try {
            InputStream is = new ByteArrayInputStream(message.getBytes());
            return Mapper.mapper.readTree(is);
        }catch (IOException e) {
            throw new IllegalArgumentException("Can't parse " + message);
        }
    }

    public JsonNode getJsonNode(JsonNode root, String field) {
        JsonNode node = null;

        if(root.isObject()) {
            node = root.path(field);
            if(node != null && !node.isMissingNode()) {
                return node;
            }

            for(Iterator<JsonNode> it = root.elements(); it.hasNext();) {
                JsonNode element = it.next();
                node = getJsonNode(element, field);
                if(node != null && !node.isMissingNode()) {
                    return node;
                }
            }
        } else if(root.isArray()) {
            ArrayNode arrayNode = (ArrayNode) root;
            for (int i=0; i< arrayNode.size(); i++) {
                JsonNode arrayElement = arrayNode.get(i);
                node = getJsonNode(arrayElement, field);
                if(node != null && !node.isMissingNode()) {
                    return node;
                }
            }
        }
        return node;
    }

    public String getProperty(JsonNode jsonNode, String idProperty) {
        return jsonNode.path(idProperty).asText(null);
    }


    public JsonNode inArray(JsonNode jsonNode) {
        if (jsonNode.isArray()) {
            return jsonNode;
        }
        ArrayNode arrayNode = Mapper.mapper.createArrayNode();
        arrayNode.add(jsonNode);
        return arrayNode;
    }

    public ArrayNode toArrayNode(List<JsonNode> jsonNodeList) {
        return (ArrayNode) toJson(jsonNodeList);
    }

    public ArrayNode toArrayNode(JsonNode jsonNode) {
        if (jsonNode.isArray()) {
            return (ArrayNode) jsonNode;
        }else {
            ArrayNode arrayNode = Mapper.mapper.createArrayNode();
            arrayNode.add(jsonNode);
            return arrayNode;
        }
    }

    public JsonNode toArrayNode(Object object) {
        JsonNode jsonNode = Mapper.mapper.convertValue(object, JsonNode.class);
        if (jsonNode.isArray()) {
            return jsonNode;
        }else {
            return inArray(jsonNode);
        }
    }

    public List<JsonNode> toList(List<JsonNode> jsonNodeList) {
        List<JsonNode> result = new ArrayList<>();
        for (JsonNode jsonNode: jsonNodeList) {
            result.addAll(toList(jsonNode));
        }
        return result;
    }

    public List<JsonNode> toList(JsonNode jsonNode) {
        List<JsonNode> result =  new ArrayList<>();
        if (jsonNode.isArray()) {
            for (JsonNode element:jsonNode) {
                result.add(element);
            }
        }else {
            result.add(jsonNode);
        }
        return result;
    }


    public JsonNode toJson(Object object) {
        if (object == null) {
            return null;
        }
        return Mapper.mapper.convertValue(object, JsonNode.class);
    }

    public JsonNode toJson(List<JsonNode> jsonNodeList) {
        return Mapper.mapper.convertValue(jsonNodeList, ArrayNode.class);
    }

    public JsonNode toJson(String json) {
        JsonNode jsonNode = Mapper.mapper.createObjectNode();
        try {
            jsonNode = Mapper.mapper.readTree(json);
        } catch (IOException e) {
            //log something
            System.out.println("Error converting JSON string to JSON object " + json );
        }
        return jsonNode;
    }

}
