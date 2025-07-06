package hybridAutomation.Core;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

public interface Mapper<T> {

    T map(JsonNode jsonNode);

    ObjectMapper mapper = ObjectMapperCreator.create();
}
