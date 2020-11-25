package records;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Paths;

import static org.junit.jupiter.api.Assertions.*;

class WAFEventDeserializationSchemaTest {

    private byte[] jsonBytes;
    private WAFEventDeserializationSchema schema;

    @BeforeEach
    void setUp() {
        try {
            URL resource = getClass().getClassLoader().getResource("filebeat_record.json");
            if (resource == null) {
                throw new IllegalArgumentException("file not found!");
            }
            jsonBytes = Files.readAllBytes(Paths.get(resource.toURI()));
            schema = new WAFEventDeserializationSchema();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @AfterEach
    void tearDown() {
        jsonBytes = null;
    }

    @Test
    void deserialize() {
        try{
            WAFEvent event = schema.deserialize(jsonBytes);
            assertNotNull(event);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}