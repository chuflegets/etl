/**
 * Autogenerated by Avro
 *
 * DO NOT EDIT DIRECTLY
 */
package records.avro;
@org.apache.avro.specific.AvroGenerated
public enum RequestStatus implements org.apache.avro.generic.GenericEnumSymbol<RequestStatus> {
  passed, blocked, alerted  ;
  public static final org.apache.avro.Schema SCHEMA$ = new org.apache.avro.Schema.Parser().parse("{\"type\":\"enum\",\"name\":\"RequestStatus\",\"namespace\":\"records.avro\",\"symbols\":[\"passed\",\"blocked\",\"alerted\"]}");
  public static org.apache.avro.Schema getClassSchema() { return SCHEMA$; }
  public org.apache.avro.Schema getSchema() { return SCHEMA$; }
}
