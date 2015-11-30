package scarvill.httpserver;

import scarvill.httpserver.constants.Status;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class Response {
    private final Status status;
    private List<String> headers = new ArrayList<>();
    private String body;

    public Response(Status status, String[] headers, String body) {
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

    public String getBody() { return body; }

}
