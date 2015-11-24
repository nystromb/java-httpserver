package scarvill.httpserver;

import scarvill.httpserver.constants.Status;
import scarvill.httpserver.constants.StatusTwo;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import static scarvill.httpserver.constants.StatusTwo.*;

public class Response {
    private final StatusTwo status;
    private String statusLine;
    private List<String> headers = new ArrayList<>();
    private String body = "";

    public Response(StatusTwo status) {
        this.status = status;
        statusLine = enumToString(status);
    }

    public Response(StatusTwo status, String[] headers) {
        this.status = status;
        statusLine = enumToString(status);
        Collections.addAll(this.headers, headers);
    }

    public Response(StatusTwo status, String[] headers, String body) {
        this.status = status;
        statusLine = enumToString(status);
        Collections.addAll(this.headers, headers);
        this.body = body;
    }

    public String getStatusLine() {
        return statusLine;
    }

    public StatusTwo getStatus() {
        return status;
    }

    public List<String> getHeaders() {
        return headers;
    }

    public String getBody() { return body;
    }

    public String generate() {
        String response = statusLine;
        for (String header : headers) {
            response = response.concat(header);
        }
        response = response.concat("\r\n");
        response = response.concat(body);
        return response;
    }

    private String enumToString (StatusTwo status) {
        switch (status) {
            case OK: return Status.OK;
            case NOT_FOUND: return Status.NOT_FOUND;
            case METHOD_NOT_ALLOWED: return Status.METHOD_NOT_ALLOWED;
            case FOUND: return Status.FOUND;
            default: return null;
        }
    }
}
