package hdfs;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class PathTest {

    @Test
    void join() {
        Path foo = new Path(new org.apache.flink.core.fs.Path("foo"));
        Path bar = new Path("bar");
        Path joined = foo.join(bar).join("baz");

        assertEquals("foo/bar/baz", joined.toString());
    }
}