package scarvill.httpserver.request;

import java.util.HashMap;

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

    public HashMap<String, String> getParameters() {
        return parameters;
    }

    public HashMap<String, String> getHeaders() {
        return headers;
    }

    public byte[] getBody() {
        return body;
    }
}

