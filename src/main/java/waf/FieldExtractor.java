package waf;

import functions.POJOToAvro;
import hdfs.Path;
import org.apache.flink.api.common.eventtime.WatermarkStrategy;
import org.apache.flink.core.fs.FSDataOutputStream;
import org.apache.flink.core.fs.FileSystem;
import org.apache.flink.formats.parquet.avro.ParquetAvroWriters;
import org.apache.flink.runtime.fs.hdfs.HadoopFileSystem;
import org.apache.flink.runtime.fs.hdfs.HadoopFsFactory;
import org.apache.flink.runtime.state.StateBackend;
import org.apache.flink.runtime.state.filesystem.FsStateBackend;
import org.apache.flink.streaming.api.datastream.DataStream;
import org.apache.flink.streaming.api.environment.CheckpointConfig;
import org.apache.flink.streaming.api.environment.StreamExecutionEnvironment;
import org.apache.flink.streaming.api.functions.sink.filesystem.StreamingFileSink;
import org.apache.flink.streaming.api.functions.sink.filesystem.bucketassigners.DateTimeBucketAssigner;
import org.apache.flink.streaming.api.functions.sink.filesystem.rollingpolicies.CheckpointRollingPolicy;
import org.apache.flink.streaming.connectors.kafka.FlinkKafkaConsumer;
import records.WAFEvent;
import records.WAFEventDeserializationSchema;
import records.avro.Event;
import streaming.EventTimeBucketAssigner;

import java.io.FileInputStream;
import java.io.IOException;
import java.net.URI;
import java.time.Duration;
import java.util.Properties;

@SuppressWarnings("StringBufferReplaceableByString")
public class FieldExtractor {
    private static final Properties properties = new Properties();

    public static FileSystem getFS() {
        FileSystem fs = null;
        try {
            StringBuilder sb = new StringBuilder("hdfs://").append(properties.getProperty("hadoop.server"));
            fs = new HadoopFsFactory().create(URI.create(sb.toString()));
        } catch (IOException e) {
            e.printStackTrace();
        }
        return fs;
    }

    public static Path getEventsFile(FileSystem fs, String client) throws IOException {
        Path eventsFile = new Path("data").join(client).join("events.parquet");
        if (!fs.exists(eventsFile)) {
            fs.mkdirs(eventsFile);
        }
        return eventsFile;
    }

    public static Path getCheckpointDir(FileSystem fs, String client) throws IOException {
        Path checkpointDir = new Path("checkpoint").join(client);
        if (!fs.exists(checkpointDir)) {
            fs.mkdirs(checkpointDir);
        }
        return checkpointDir;
    }

    public static void main(String[] args) throws Exception {
        if (args.length != 1) {
            StringBuilder sb = new StringBuilder("wrong number of arguments.");
            sb.append("Expected 1 (properties file path), got ").append(args.length).append(".");
            throw new IllegalArgumentException(sb.toString());
        }
        properties.load(new FileInputStream(args[0]));

        FileSystem fs = getFS();
        String client = properties.getProperty("client");
        Path eventsFile = getEventsFile(fs, client);
        Path checkpointDir = getCheckpointDir(fs, client);

        Properties kafkaProperties = new Properties();
        kafkaProperties.setProperty("bootstrap.servers", properties.getProperty("kafka.server"));
        kafkaProperties.setProperty("group.id", properties.getProperty("group.id"));

        StreamExecutionEnvironment env = StreamExecutionEnvironment.getExecutionEnvironment();
        env.enableCheckpointing(60_000);
        env.setStateBackend(
            (StateBackend) new FsStateBackend(fs.getFileStatus(checkpointDir).getPath().toUri())
        );
        env.getCheckpointConfig().enableExternalizedCheckpoints(
            CheckpointConfig.ExternalizedCheckpointCleanup.DELETE_ON_CANCELLATION
        );

        FlinkKafkaConsumer<WAFEvent> kafkaConsumer = new FlinkKafkaConsumer<>(
            properties.getProperty("topic"), new WAFEventDeserializationSchema(), kafkaProperties
        );
        kafkaConsumer.setStartFromEarliest();

        WatermarkStrategy<WAFEvent> strategy = WatermarkStrategy
            .<WAFEvent>forBoundedOutOfOrderness(Duration.ofSeconds(20))
            .withTimestampAssigner((event, timestamp) -> event.getTimestamp().toEpochSecond());

        DataStream<WAFEvent> kafkaStream = env.addSource(kafkaConsumer)
            .name("WAF event source")
            .assignTimestampsAndWatermarks(strategy);
        DataStream<Event> eventStream = kafkaStream.map(new POJOToAvro());

        StreamingFileSink<Event> sink = StreamingFileSink
            .forBulkFormat(
                fs.getFileStatus(eventsFile).getPath(),
                ParquetAvroWriters.forSpecificRecord(Event.class)
            )
            .withBucketAssigner(new EventTimeBucketAssigner<>("yyyy-MM-dd--HH"))
            .build();
        eventStream.addSink(sink);
        env.execute("Sample");
    }
}
