package scarvill.httpserver.request;

import java.util.Arrays;
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
            .setHeaders(parseHeaders())
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
        HashMap<String, String> parameters = new HashMap<>();

        if (requestHasQueryStringParameters()) {
            parameters = new QueryString().parse(requestLineAndHeaders.split(" ")[1].split("\\?")[1]);
        }

        return parameters;
    }

    private HashMap<String,String> parseHeaders() {
        HashMap<String, String> headers = new HashMap<>();
        String[] requestLines = requestLineAndHeaders.split("\\r\\n");

        if (includesHeaders(requestLines)) {
            Arrays.stream(requestLines, 1, requestLines.length).forEach(
                (header) -> headers.put(header.split(": ")[0], header.split(": ")[1])
            );
        }

        return headers;
    }

    private boolean requestHasQueryStringParameters() {
        return requestLineAndHeaders.split(" ")[1].split("\\?").length > 1;
    }

    private boolean includesHeaders(String[] requestLines) {
        return requestLines.length > 2;
    }
}
