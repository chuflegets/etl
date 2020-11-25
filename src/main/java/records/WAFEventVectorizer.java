package records;

import org.apache.flink.orc.vector.Vectorizer;
import org.apache.hadoop.hive.ql.exec.vector.BytesColumnVector;
import org.apache.hadoop.hive.ql.exec.vector.TimestampColumnVector;
import org.apache.hadoop.hive.ql.exec.vector.VectorizedRowBatch;

import java.io.IOException;
import java.io.Serializable;
import java.nio.charset.StandardCharsets;
import java.sql.Timestamp;

public class WAFEventVectorizer extends Vectorizer<WAFEvent> implements Serializable {
    public WAFEventVectorizer(String schema) {
        super(schema);
    }
    @Override
    public void vectorize(WAFEvent wafEvent, VectorizedRowBatch vectorizedRowBatch) throws IOException {
        TimestampColumnVector timestampColVector = (TimestampColumnVector) vectorizedRowBatch.cols[0];
        BytesColumnVector ipColVector = (BytesColumnVector) vectorizedRowBatch.cols[1];
        BytesColumnVector attackColVector = (BytesColumnVector) vectorizedRowBatch.cols[1];
        BytesColumnVector geolocationColVector = (BytesColumnVector) vectorizedRowBatch.cols[1];
        BytesColumnVector methodColVector = (BytesColumnVector) vectorizedRowBatch.cols[1];
        BytesColumnVector contentTypeColVector = (BytesColumnVector) vectorizedRowBatch.cols[1];
        BytesColumnVector contentLengthColVector = (BytesColumnVector) vectorizedRowBatch.cols[1];
        BytesColumnVector languageColVector = (BytesColumnVector) vectorizedRowBatch.cols[1];
        BytesColumnVector refererColVector = (BytesColumnVector) vectorizedRowBatch.cols[1];
        BytesColumnVector userAgentColVector = (BytesColumnVector) vectorizedRowBatch.cols[1];
        BytesColumnVector responseCodeColVector = (BytesColumnVector) vectorizedRowBatch.cols[1];
        BytesColumnVector uriColVector = (BytesColumnVector) vectorizedRowBatch.cols[1];
        BytesColumnVector usernameColVector = (BytesColumnVector) vectorizedRowBatch.cols[1];
        BytesColumnVector requestStatusColVector = (BytesColumnVector) vectorizedRowBatch.cols[1];

        int row = vectorizedRowBatch.size++;

        timestampColVector.set(row, new Timestamp(wafEvent.getTimestamp().toEpochSecond()));
        ipColVector.setVal(row, wafEvent.getSourceIP().toString().getBytes(StandardCharsets.UTF_8));
    }
}
