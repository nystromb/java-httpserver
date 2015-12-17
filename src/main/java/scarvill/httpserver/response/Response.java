package scarvill.httpserver.response;

import java.util.HashMap;

public class Response {
    private final Status status;
    private HashMap<String, String> headers = new HashMap<>();
    private byte[] body;

    public Response(Status status, HashMap<String, String> headers, byte[] body) {
        this.status = status;
        this.headers = headers;
        this.body = body;
    }

    public Status getStatus() {
        return status;
    }

    public HashMap<String, String> getHeaders() {
        return headers;
    }

    public byte[] getBody() {
        return body;
    }

}
