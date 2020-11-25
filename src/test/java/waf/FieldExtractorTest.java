package waf;

import org.apache.flink.core.fs.FileSystem;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class FieldExtractorTest {

    @Test
    void getFS() {
        FileSystem fs = FieldExtractor.getFS();
        assertNotNull(fs);
    }
}