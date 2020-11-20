package waf;

import org.apache.flink.shaded.jackson2.com.fasterxml.jackson.databind.node.ObjectNode;
import org.apache.flink.streaming.api.datastream.DataStream;
import org.apache.flink.streaming.api.environment.StreamExecutionEnvironment;
import org.apache.flink.streaming.connectors.kafka.FlinkKafkaConsumer;
import records.WAFEventDeserializationSchema;
import records.WAFEvent;

import java.util.Properties;

public class FieldExtractor {
    public static void main(String[] args) throws Exception {
        StreamExecutionEnvironment env = StreamExecutionEnvironment.getExecutionEnvironment();

        Properties properties = new Properties();
        properties.setProperty("bootstrap.servers", "localhost:9092");
        properties.setProperty("group.id", "movistar-plus");
        FlinkKafkaConsumer<WAFEvent> kafkaConsumer = new FlinkKafkaConsumer<>(
            "movistar-plus", new WAFEventDeserializationSchema(), properties
        );
        kafkaConsumer.setStartFromEarliest();
        DataStream<WAFEvent> stream = env.addSource(kafkaConsumer);
        stream.print();
        env.execute("Sample");
    }
}
