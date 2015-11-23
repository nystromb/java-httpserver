package scarvill.httpserver;

import scarvill.httpserver.constants.MethodTwo;

public class RequestTwo {
    private MethodTwo method;
    private String uri;
    private String body;

    public RequestTwo(MethodTwo method, String uri, String body) {
        this.method = method;
        this.uri = uri;
        this.body = body;
    }

    public MethodTwo getMethod() {
        return method;
    }

    public String getURI() {
        return uri;
    }

    public String getBody() {
        return body;
    }

    public static class Builder {
        private MethodTwo method;
        private String uri;
        private String body;

        public Builder setMethod(MethodTwo method) {
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

        public RequestTwo build() {
            return new RequestTwo(this.method, this.uri, this.body);
        }
    }
}
