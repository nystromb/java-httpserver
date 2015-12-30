package scarvill.httpserver.request;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class Request {
    private final Method method;
    private final String uri;
    private final byte[] body;
    private HashMap<String, String> parameters;
    private HashMap<String, String> headers;

    public Request(Method method,
                   String uri,
                   HashMap<String, String> parameters,
                   HashMap<String, String> headers,
                   byte[] body) {
        this.method = method;
        this.uri = uri;
        this.parameters = parameters;
        this.headers = headers;
        this.body = body;
    }

    public Method getMethod() {
        return method;
    }

    public String getURI() {
        return uri;
    }

    public List<String> getParameterNames() {
        return new ArrayList<>(parameters.keySet());
    }

    public String getParameterValue(String headerName) {
        return parameters.get(headerName);
    }

    public List<String> getHeaderNames() {
        return new ArrayList<>(headers.keySet());
    }

    public String getHeaderContent(String headerName) {
        return headers.get(headerName);
    }

    public byte[] getBody() {
        return body;
    }
}

