package scarvill.httpserver;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class Response {
    private String statusLine;
    private List<String> headers = new ArrayList<>();

    public Response(String status) {
        statusLine = status;
        addDefaultHeaders();
    }

    public Response(String status, String[] headers) {
        statusLine = status;
        addDefaultHeaders();
        Collections.addAll(this.headers, headers);
    }

    public String getStatusLine() {
        return statusLine;
    }

    public List<String> getHeaders() {
        return headers;
    }

    public String generate() {
        String response = statusLine;

        for (String header : headers) {
            response = response.concat(header);
        }
        return response;
    }

    private void addDefaultHeaders() {
        headers.add("Connection: close\r\n");
    }
}
