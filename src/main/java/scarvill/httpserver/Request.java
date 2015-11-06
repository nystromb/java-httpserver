package scarvill.httpserver;

public class Request {
    private String rawRequest;

    public Request(String rawRequest) {
        this.rawRequest = rawRequest;
    }

    public String getAction() {
        return rawRequest.split(" ")[0];
    }

    public String getURI() {
        return rawRequest.split(" ")[1];
    }
}

