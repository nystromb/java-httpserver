package scarvill.httpserver;

public class Request {
    private String rawRequest;

    public Request(String rawRequest) {
        this.rawRequest = rawRequest;
    }

    public String getMethod() {
        return rawRequest.split(" ")[0];
    }

    public String getURI() {
        return rawRequest.split(" ")[1];
    }

    public String getBody() {
        String bodyDelimiter = "\r\n\r\n";
        int bodyStartIndex = rawRequest.indexOf(bodyDelimiter) + bodyDelimiter.length();

        return rawRequest.substring(bodyStartIndex);
    }
}

