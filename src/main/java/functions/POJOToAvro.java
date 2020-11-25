package functions;

import org.apache.flink.api.common.functions.MapFunction;
import records.WAFEvent;
import records.avro.Event;

public class POJOToAvro implements MapFunction<WAFEvent, Event> {
    @Override
    public Event map(WAFEvent wafEvent) throws Exception {
        return wafEvent.toAvro();
    }
}
