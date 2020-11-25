package records;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Paths;

import static org.junit.jupiter.api.Assertions.*;

class WAFEventTest {

    private WAFEvent wafEvent;

    @BeforeEach
    void setUp() {
        try {
            URL resource = getClass().getClassLoader().getResource("message.txt");
            if (resource == null) {
                throw new IllegalArgumentException("file not found!");
            }
            String message = Files.readString(Paths.get(resource.toURI()));
            wafEvent = new WAFEvent(message);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @AfterEach
    void tearDown() {
        wafEvent = null;
    }

    @Test
    void getTimestamp() {
        assertNotNull(wafEvent.getTimestamp());
    }

    @Test
    void getSourceIP() {
        assertNotNull(wafEvent.getSourceIP());
    }

    @Test
    void getGeolocation() {
        assertNotNull(wafEvent.getGeolocation());
    }

    @Test
    void getResponseCode() {
        assertNotNull(wafEvent.getResponseCode());
    }

    @Test
    void getAttackType() {
        assertNotNull(wafEvent.getAttackType());
    }

    @Test
    void getUsername() {
        assertNotNull(wafEvent.getUsername());
    }

    @Test
    void getSessionId() {
        assertNotNull(wafEvent.getSessionId());
    }

    @Test
    void getMethod() {
        assertNotNull(wafEvent.getRequestMethod());
    }

    @Test
    void getUri() {
        assertNotNull(wafEvent.getUri());
    }

    @Test
    void getContentType() {
        assertNotNull(wafEvent.getContentType());
    }

    @Test
    void getContentLength() {
        assertNotNull(wafEvent.getContentLength());
    }

    @Test
    void getLanguage() {
        assertNotNull(wafEvent.getLanguage());
    }

    @Test
    void getReferer() {
        assertNotNull(wafEvent.getReferer());
    }

    @Test
    void getUserAgent() {
        assertTrue(wafEvent.getUserAgent() != null);
    }

    @Test
    void getStatus() {
        assertTrue(wafEvent.getRequestStatus() != null);
    }

    @Test
    void testToString() {
        assertTrue(wafEvent.toString() != null);
    }
}