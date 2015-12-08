package scarvill.httpserver.request;

import java.util.HashMap;

public class RequestBuilder {
    private Method method;
    private String uri;
    private HashMap<String, String> parameters;
    private byte[] body;

    public Request build() {
        return new Request(method, uri, parameters, body);
    }

    public RequestBuilder setMethod(Method method) {
        this.method = method;
        return this;
    }

    public RequestBuilder setURI(String uri) {
        this.uri = uri;
        return this;
    }

    public RequestBuilder setParameters(HashMap<String, String> parameters) {
        this.parameters = parameters;
        return this;
    }

    public RequestBuilder setBody(byte[] body) {
        this.body = body;
        return this;
    }
}
