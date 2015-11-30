package scarvill.httpserver.request;

import java.util.HashMap;

public class Request {
    private final Method method;
    private final String body;
    private final String uri;
    private HashMap<String,String> parameters;

    public Request(Method method, String uri, HashMap<String, String> parameters, String body) {
        this.method = method;
        this.uri = uri;
        this.parameters = parameters;
        this.body = body;
    }

    public String getURI() {
        return uri;
    }

    public String getBody() {
        return body;
    }

    public Method getMethod() {
        return method;
    }

    public HashMap<String, String> getParameters() {
        return parameters;
    }

}

