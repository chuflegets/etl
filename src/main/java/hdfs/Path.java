package hdfs;

import java.nio.file.Paths;

public class Path extends org.apache.flink.core.fs.Path {
    public Path(String toString) {
        super(toString);
    }

    public Path(org.apache.flink.core.fs.Path path) {
        this(path.toString());
    }

    public Path join(String other) {
        return new Path(Paths.get(this.toString(), other).toString());
    }

    public Path join(Path other) {
        return new Path(Paths.get(this.toString(), other.toString()).toString());
    }
}
