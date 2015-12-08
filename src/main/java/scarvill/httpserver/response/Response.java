package scarvill.httpserver.response;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class Response {
    private final Status status;
    private List<String> headers = new ArrayList<>();
    private byte[] body;

    public Response(Status status, String[] headers, byte[] body) {
        this.status = status;
        Collections.addAll(this.headers, headers);
        this.body = body;
    }

    public Status getStatus() {
        return status;
    }

    public List<String> getHeaders() {
        return headers;
    }

    public byte[] getBody() { return body; }

}
