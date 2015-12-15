package scarvill.httpserver.request;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
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
            case "PATCH":   return PATCH;
            case "DELETE":  return DELETE;
            default:        return NULL_METHOD;
        }
    }

    private String parseURI() {
        return requestLineAndHeaders.split(" ")[1].split("\\?")[0];
    }

    private HashMap<String, String> parseParameters() {
        HashMap<String, String> parameters = new HashMap<>();

        if (requestHasQueryString()) {
            try {
                parameters = parseQueryStringParameters(requestLineAndHeaders.split(" ")[1].split("\\?")[1]);
            } catch (UnsupportedEncodingException ignored) {}
        }

        return parameters;
    }

    private boolean requestHasQueryString() {
        return requestLineAndHeaders.split(" ")[1].split("\\?").length > 1;
    }

    private HashMap<String, String> parseQueryStringParameters(String query) throws UnsupportedEncodingException {
        HashMap<String, String> parameters = new HashMap<>();

        for (String argument : query.split("&")) {
            String parameterName = URLDecoder.decode(argument.split("=")[0], "UTF-8");
            String parameterValue = URLDecoder.decode(argument.split("=")[1], "UTF-8");
            parameters.put(parameterName, parameterValue);
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

    private boolean includesHeaders(String[] requestLines) {
        return requestLines.length > 2;
    }
}
