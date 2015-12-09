package scarvill.httpserver.request;

import java.util.HashMap;

public class RequestBuilder {
    private Method method;
    private String uri;
    private HashMap<String, String> parameters = new HashMap<>();
    private HashMap<String, String> headers = new HashMap<>();
    private byte[] body;

    public Request build() {
        return new Request(method, uri, parameters, headers, body);
    }

    public RequestBuilder setMethod(Method method) {
        this.method = method;
        return this;
    }

    public RequestBuilder setURI(String uri) {
        this.uri = uri;
        return this;
    }

    public RequestBuilder setParameter(String name, String value) {
        this.parameters.put(name, value);
        return this;
    }

    public RequestBuilder setParameters(HashMap<String, String> parameters) {
        this.parameters.putAll(parameters);
        return this;
    }

    public RequestBuilder setHeader(String keyword, String content) {
        this.headers.put(keyword, content);
        return this;
    }

    public RequestBuilder setHeaders(HashMap<String, String> headers) {
        this.headers.putAll(headers);
        return this;
    }

    public RequestBuilder setBody(byte[] body) {
        this.body = body;
        return this;
    }
}
