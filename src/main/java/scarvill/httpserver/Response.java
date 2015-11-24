package scarvill.httpserver;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class Response {
    private String statusLine;
    private List<String> headers = new ArrayList<>();
    private String body = "";

    public Response(String status) {
        statusLine = status;
    }

    public Response(String status, String[] headers) {
        statusLine = status;
        Collections.addAll(this.headers, headers);
    }

    public Response(String status, String[] headers, String body) {
        statusLine = status;
        Collections.addAll(this.headers, headers);
        this.body = body;
    }

    public String getStatusLine() {
        return statusLine;
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
}
