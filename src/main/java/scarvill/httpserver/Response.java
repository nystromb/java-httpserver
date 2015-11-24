package scarvill.httpserver;

import scarvill.httpserver.constants.Status;
import scarvill.httpserver.constants.StatusTwo;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import static scarvill.httpserver.constants.StatusTwo.*;

public class Response {
    private final StatusTwo status;
    private List<String> headers = new ArrayList<>();
    private String body = "";

    public Response(StatusTwo status) {
        this.status = status;
    }

    public Response(StatusTwo status, String[] headers) {
        this.status = status;
        Collections.addAll(this.headers, headers);
    }

    public Response(StatusTwo status, String[] headers, String body) {
        this.status = status;
        Collections.addAll(this.headers, headers);
        this.body = body;
    }

    public StatusTwo getStatus() {
        return status;
    }

    public List<String> getHeaders() {
        return headers;
    }

    public String getBody() { return body;
    }
}
