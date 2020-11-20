package records;

import org.apache.flink.api.common.serialization.AbstractDeserializationSchema;
import org.apache.flink.shaded.jackson2.com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.flink.shaded.jackson2.com.fasterxml.jackson.databind.node.ObjectNode;

import java.io.IOException;

public class WAFEventDeserializationSchema extends AbstractDeserializationSchema<WAFEvent> {

    private static final long serialVersionUID = 1L;

    private final ObjectMapper mapper = new ObjectMapper();

    @Override
    public WAFEvent deserialize(byte[] bytes) throws IOException {
        String message = mapper.readValue(bytes, ObjectNode.class).get("message").toString();
        return new WAFEvent(message);
    }
}
