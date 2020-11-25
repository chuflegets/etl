package records;

import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class HttpRequest {
    private static final Pattern urlParamPattern = Pattern.compile(
        "(?<key>\\w+)=(?<value>.*)"
    );
    private static final Pattern requestHeaderPattern = Pattern.compile(
        "(?<key>[A-Z][a-z]*(?:\\x2d[A-Z][a-z]*)*)\\x3a\\x20(?<value>.*)"
    );

    private String method;
    private String uri;
    private String status;
    private Map<String, String> urlParams;
    private Map<String, String> headers;

    public HttpRequest() {
    }

    public HttpRequest(String method, String uri, String status, String query, String request) {
        this.method = method;
        this.uri = uri;
        this.status = status;
        this.urlParams = parse(query, "&", urlParamPattern);
        this.headers = parse(request, "\\\\\\\\r\\\\\\\\n", requestHeaderPattern);
    }

    public HttpRequest(Map<String, String> requestParams) {
        this(
            requestParams.get("method"),
            requestParams.get("uri"),
            requestParams.get("status"),
            requestParams.get("query"),
            requestParams.get("request")
        );
    }

    private Map<String, String> parse(String text, String separator, Pattern keyValuePattern) {
        Map<String, String> fields = new HashMap<>();
        for (String field : text.split(separator)) {
            Matcher matcher = keyValuePattern.matcher(field);
            while (matcher.find()) {
                String key = matcher.group("key");
                String value = matcher.group("value");
                fields.put(key, value);
            }
        }
        return fields;
    }

    public String getMethod() {
        return method;
    }

    public String getUri() {
        return uri;
    }

    public String getStatus() {
        return status;
    }

    public String header(String name) {
        return headers.get(name);
    }

    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder("HttpRequest<");
        sb.append("method=").append(method);
        sb.append(", ").append("uri=").append(uri);
        sb.append(", ").append("status=").append(status);
        if (!urlParams.isEmpty()) {
            sb.append(", ").append("url_params=").append(urlParams);
        }
        if (!headers.isEmpty()) {
            sb.append(", ").append("headers=").append(headers);
        }
        sb.append(">");
        return sb.toString();
    }
}
