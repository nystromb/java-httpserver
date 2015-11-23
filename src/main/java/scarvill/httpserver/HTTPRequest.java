package scarvill.httpserver;

import scarvill.httpserver.constants.Method;

import static scarvill.httpserver.constants.Method.*;

public class HTTPRequest {
    private String rawRequest;

    public HTTPRequest(String rawRequest) {
        this.rawRequest = rawRequest;
    }

    private Method parseMethod() {
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

    public Request parseAsRequest() {
        return new Request.Builder()
            .setMethod(parseMethod())
            .setURI(parseURI())
            .setBody(parseBody())
            .build();
    }
}
