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

// when you call build(), you could just pass "this" entire object into the constructor of the Request method
//
// instead of:
//      return new Request(method, uri, parameters, headers, body);
// you would do:
//      return new Request(this);
//
// then call the fields from the this builder to set the appropriate fields in the Request constructor

// this is something I got out of Effective java
// http://www.informit.com/articles/article.aspx?p=1216151&seqNum=2