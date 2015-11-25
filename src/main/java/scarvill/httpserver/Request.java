package scarvill.httpserver;

import scarvill.httpserver.constants.Method;

public class Request {
    private final Method method;
    private final String body;
    private final String uri;

    public Request(Method method, String uri, String body) {
        this.method = method;
        this.uri = uri;
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

    public static class Builder {
        private Method method;
        private String uri;
        private String body;

        public Request build() {
            return new Request(method, uri, body);
        }

        public Builder setMethod(Method method) {
            this.method = method;
            return this;
        }

        public Builder setURI(String uri) {
            this.uri = uri;
            return this;
        }

        public Builder setBody(String body) {
            this.body = body;
            return this;
        }
    }
}

