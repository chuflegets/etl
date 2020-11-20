package records;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import javax.print.DocFlavor;
import java.io.File;
import java.io.InputStream;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Paths;

import static org.junit.jupiter.api.Assertions.assertTrue;

class HttpRequestTest {

    private HttpRequest request;

    @BeforeEach
    public void setUp() {
        try {
            URL resource = getClass().getClassLoader().getResource("request.txt");
            if (resource == null) {
                throw new IllegalArgumentException("file not found!");
            }
            String rawRequest = Files.readString(Paths.get(resource.toURI()));
            request = new HttpRequest(
                "POST",
                "/vod/auth.svc/android.tablet/users/authenticate",
                "passed",
                "",
                rawRequest
            );
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @AfterEach
    public void tearDown() {
        request = null;
    }

    @Test
    void getMethod() {
        assertTrue(request.getMethod() != null);
    }

    @Test
    void getUri() {
        assertTrue(request.getUri() != null);
    }

    @Test
    void getStatus() {
        assertTrue(request.getStatus() != null);
    }

    @Test
    void header() {
    }

    @Test
    void testHeader() {
    }

    @Test
    void testToString() {
        assertTrue(request.toString() != null);
    }
}