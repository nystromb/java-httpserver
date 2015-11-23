package scarvill.httpserver;

import scarvill.httpserver.constants.MethodTwo;

import static scarvill.httpserver.constants.MethodTwo.*;

public class HTTPRequest {
    private String rawRequest;

    public HTTPRequest(String rawRequest) {
        this.rawRequest = rawRequest;
    }

    public RequestTwo parse() {
        return new RequestTwo.Builder()
            .setMethod(parseMethod())
            .setURI(parseURI())
            .setBody(parseBody())
            .build();
    }

    private MethodTwo parseMethod() {
        String method = rawRequest.split(" ")[0];
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
        return rawRequest.split(" ")[1];
    }

    private String parseBody() {
        String bodyDelimiter = "\r\n\r\n";
        int bodyStartIndex = rawRequest.indexOf(bodyDelimiter);

        if (bodyStartIndex == -1) {
            return "";
        } else {
            return rawRequest.substring(bodyStartIndex + bodyDelimiter.length());
        }
    }
}
