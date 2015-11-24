package scarvill.httpserver;

import scarvill.httpserver.constants.Status;

public class HTTPResponse {

    public String generate(Response response) {
        String generatedResponse = statusToString(response.getStatus());
        for (String header : response.getHeaders()) {
            generatedResponse = generatedResponse.concat(header);
        }
        generatedResponse = generatedResponse.concat("\r\n");
        generatedResponse = generatedResponse.concat(response.getBody());

        return generatedResponse;
    }

    private String statusToString(Status status) {
        String statusLine;

        switch (status) {
            case OK:
                statusLine = "200 OK";
                break;
            case NOT_FOUND:
                statusLine = "404 Not Found";
                break;
            case METHOD_NOT_ALLOWED:
                statusLine = "405 Method Not Allowed";
                break;
            case FOUND:
                statusLine = "302 Found";
                break;
            default:
                statusLine = "";
                break;
        }

        return "HTTP/1.1 " + statusLine + "\r\n";
    }
}
