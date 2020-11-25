package streaming;

import org.apache.flink.core.io.SimpleVersionedSerializer;
import org.apache.flink.streaming.api.functions.sink.filesystem.BucketAssigner;
import org.apache.flink.streaming.api.functions.sink.filesystem.bucketassigners.DateTimeBucketAssigner;
import org.apache.flink.streaming.api.functions.sink.filesystem.bucketassigners.SimpleVersionedStringSerializer;
import org.apache.flink.util.Preconditions;
import records.avro.Event;

import java.time.Instant;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;

@SuppressWarnings("StringBufferReplaceableByString")
public class EventTimeBucketAssigner<IN> implements BucketAssigner<IN, String> {
    private static final long serialVersionUID = 1L;
    private static final String DEFAULT_FORMAT_STRING = "yyyy-MM-dd--HH";

    private final String formatString;
    private final ZoneId zoneId;

    private transient DateTimeFormatter dateTimeFormatter;

    public EventTimeBucketAssigner() {
        this(DEFAULT_FORMAT_STRING);
    }

    public EventTimeBucketAssigner(String formatString) {
        this(formatString, ZoneId.systemDefault());
    }

    public EventTimeBucketAssigner(ZoneId zoneId) {
        this(DEFAULT_FORMAT_STRING, zoneId);
    }

    public EventTimeBucketAssigner(String formatString, ZoneId zoneId) {
        this.formatString = Preconditions.checkNotNull(formatString);
        this.zoneId = Preconditions.checkNotNull(zoneId);
    }

    @Override
    public String getBucketId(IN element, Context context) {
        if (dateTimeFormatter == null) {
            dateTimeFormatter = DateTimeFormatter.ofPattern(formatString).withZone(zoneId);
        }
        return dateTimeFormatter.format(Instant.ofEpochSecond(context.timestamp()));
    }

    @Override
    public SimpleVersionedSerializer<String> getSerializer() {
        return SimpleVersionedStringSerializer.INSTANCE;
    }

    @Override
    public String toString() {
        return new StringBuilder("EventTimeBucketAssigner{")
            .append("formatString='").append(formatString).append("', ")
            .append("zoneId=").append(zoneId).append("}")
            .toString();
    }
}
