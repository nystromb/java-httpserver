package scarvill.httpserver.request;

import java.util.HashMap;

import static scarvill.httpserver.request.Method.*;

public class HTTPRequest {
    private String requestLineAndHeaders;
    private byte[] body = new byte[]{};

    public HTTPRequest(String requestLineAndHeaders, byte[] body) {
        this.requestLineAndHeaders = requestLineAndHeaders;
        this.body = body;
    }

    public HTTPRequest(String requestLineAndHeaders) {
        this.requestLineAndHeaders = requestLineAndHeaders;
    }

    public Request parse() {
        return new RequestBuilder()
            .setMethod(parseMethod())
            .setURI(parseURI())
            .setParameters(parseParameters())
            .setBody(body)
            .build();
    }

    private Method parseMethod() {
        String method = requestLineAndHeaders.split(" ")[0];
        switch (method) {
            case "GET":     return GET;
            case "HEAD":    return HEAD;
            case "OPTIONS": return OPTIONS;
            case "PUT":     return PUT;
            case "POST":    return POST;
            case "DELETE":  return DELETE;
            default:        return NULL_METHOD;
        }
    }

    private String parseURI() {
        return requestLineAndHeaders.split(" ")[1].split("\\?")[0];
    }

    private HashMap<String, String> parseParameters() {
        if (hasQueryStringParameters()) {
            return new QueryString().parse(requestLineAndHeaders.split(" ")[1].split("\\?")[1]);
        } else {
            return new HashMap<>();
        }
    }

    private boolean hasQueryStringParameters() {
        return requestLineAndHeaders.split(" ")[1].split("\\?").length > 1;
    }
}
