package scarvill.httpserver;

import scarvill.httpserver.constants.Method;

import static scarvill.httpserver.constants.Method.*;

public class Request {
    private final Method method;
    private final String body;
    private final String uri;

    private String rawRequest;

    public Request(String rawRequest) {
        this.rawRequest = rawRequest;
        method = null;
        uri = null;
        body = null;
    }

    public Request(Method method, String uri, String body) {
        this.method = method;
        this.uri = uri;
        this.body = body;
    }

    public String getURI() {
        if (rawRequest == null) {
            return uri;
        } else {
            return rawRequest.split(" ")[1];
        }
    }

    public String getBody() {
        if (rawRequest == null) {
            return body;
        } else {
            String bodyDelimiter = "\r\n\r\n";
            int bodyStartIndex = rawRequest.indexOf(bodyDelimiter) + bodyDelimiter.length();

            return rawRequest.substring(bodyStartIndex);
        }
    }

    public Method getMethod() {
        if (rawRequest == null) {
            return method;
        } else {
            String method = rawRequest.split(" ")[0];
            switch (method) {
                case "GET":
                    return GET;
                case "HEAD":
                    return HEAD;
                case "OPTIONS":
                    return OPTIONS;
                case "PUT":
                    return PUT;
                case "POST":
                    return POST;
                case "DELETE":
                    return DELETE;
                default:
                    return NULL_METHOD;
            }
        }
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

