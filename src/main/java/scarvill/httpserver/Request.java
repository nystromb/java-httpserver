package scarvill.httpserver;

import scarvill.httpserver.constants.Method;

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

    public static class Builder {
        private Method method;
        private String uri;
        private HashMap<String, String> parameters;
        private String body;

        public Request build() {
            return new Request(method, uri, parameters, body);
        }

        public Builder setMethod(Method method) {
            this.method = method;
            return this;
        }

        public Builder setURI(String uri) {
            this.uri = uri;
            return this;
        }

        public Builder setParameters(HashMap<String, String> parameters) {
            this.parameters = parameters;
            return this;
        }

        public Builder setBody(String body) {
            this.body = body;
            return this;
        }
    }
}

