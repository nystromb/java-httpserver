package scarvill.httpserver;

public class HTTPRequest {
    private String rawRequest;

    public HTTPRequest(String rawRequest) {
        this.rawRequest = rawRequest;
    }

    public String getAction() {
        return rawRequest.split(" ")[0];
    }

    public String getURI() {
        return rawRequest.split(" ")[1];
    }
}

