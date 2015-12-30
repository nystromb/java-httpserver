package scarvill.httpserver.response;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

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

    public List<String> getHeaderNames() {
        return new ArrayList<>(headers.keySet());
    }

    public String getHeaderContent(String headerName) {
        return headers.get(headerName);
    }

    public byte[] getBody() {
        return body;
    }
}
