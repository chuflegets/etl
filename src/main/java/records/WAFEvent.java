package records;

import records.avro.Event;
import records.avro.RequestMethod;
import records.avro.RequestStatus;

import java.net.InetAddress;
import java.net.UnknownHostException;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class WAFEvent {
    private static final ZoneId timezone = ZoneId.of("Europe/Madrid");
    private static final DateTimeFormatter dtFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd H:m:s");
    private static final Pattern wafFieldPattern = Pattern.compile("(?<key>\\w+)=\\\\\\x22(?<value>.*?)\\\\\\x22");

    private ZonedDateTime timestamp;
    private InetAddress sourceIP;
    private String geolocation;
    private Short responseCode;
    private String attackType;
    private String username;
    private String sessionId;
    private HttpRequest request;

    public WAFEvent() {
    }

    public WAFEvent(final String message) throws UnknownHostException {
        Map<String, String> requestParams = new HashMap<>();
        Matcher matcher = wafFieldPattern.matcher(message);
        while (matcher.find()) {
            String key = matcher.group("key");
            String value = matcher.group("value");
            switch (key) {
                case "response_code":
                    this.responseCode = Short.valueOf(value);
                    break;
                case "ip_client":
                    this.sourceIP = InetAddress.getByName(value);
                    break;
                case "date_time":
                    this.timestamp = LocalDateTime.parse(value, dtFormatter).atZone(timezone);
                    break;
                case "geo_location":
                    this.geolocation = value;
                    break;
                case "attack_type":
                    this.attackType = value;
                    break;
                case "username":
                    this.username = value;
                    break;
                case "session_id":
                    this.sessionId = value;
                    break;
                case "request":
                    requestParams.put("request", value);
                    break;
                case "method":
                    requestParams.put("method", value);
                    break;
                case "uri":
                    requestParams.put("uri", value);
                    break;
                case "request_status":
                    requestParams.put("status", value);
                    break;
                case "query_string":
                    requestParams.put("query", value);
                    break;
                default:
                    break;
            }
        }
        this.request = new HttpRequest(requestParams);
    }

    public ZonedDateTime getTimestamp() {
        return timestamp;
    }

    public InetAddress getSourceIP() {
        return sourceIP;
    }

    public String getGeolocation() {
        return geolocation;
    }

    public Short getResponseCode() {
        return responseCode;
    }

    public String getAttackType() {
        return attackType;
    }

    public String getUsername() {
        return username;
    }

    public String getSessionId() {
        return sessionId;
    }

    public String getRequestMethod() {
        return request.getMethod();
    }

    public String getUri() {
        return request.getUri();
    }

    private String getHeaderIfAbsent(String header) {
        header = (header != null) ? header : "";
        return header;
    }

    public String getContentType() {
        return getHeaderIfAbsent(request.header("Content-Type"));
    }

    public Short getContentLength() {
        String header = getHeaderIfAbsent(request.header("Content-Length"));
        Short contentLength = (header.trim().isEmpty()) ? 0 : Short.parseShort(header);
        return contentLength;
    }

    public String getLanguage() {
        return getHeaderIfAbsent(request.header("Language"));
    }

    public String getReferer() {
        return getHeaderIfAbsent(request.header("Referer"));
    }

    public String getUserAgent() {
        return getHeaderIfAbsent(request.header("User-Agent"));
    }

    public String getRequestStatus() {
        return request.getStatus();
    }

    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder("WAFEvent<");
        sb.append("timestamp=").append(timestamp).append(", ");
        sb.append("src_ip=").append(sourceIP).append(", ");
        sb.append("geolocation=").append(geolocation).append(", ");
        sb.append("response_code=").append(responseCode).append(", ");
        sb.append("attack_type=").append(attackType).append(", ");
        sb.append("username=").append(username).append(", ");
        sb.append("session_id=").append(sessionId).append(", ");
        sb.append("request=").append(request);
        sb.append(">");
        return sb.toString();
    }

    public Event toAvro() {
        Event event = Event.newBuilder()
            .setTimestamp(timestamp.toEpochSecond())
            .setSrcIp(sourceIP.toString())
            .setGeolocation(geolocation)
            .setRequestMethod(RequestMethod.valueOf(getRequestMethod()))
            .setRequestStatus(RequestStatus.valueOf(getRequestStatus()))
            .setResponseCode(responseCode)
            .setAttackType(attackType)
            .setContentLength(getContentLength())
            .setContentType(getContentType())
            .setLanguage(getLanguage())
            .setReferer(getReferer())
            .setUserAgent(getUserAgent())
            .setUri(getUri())
            .setUsername(getUsername())
            .build();
        return event;
    }
}
