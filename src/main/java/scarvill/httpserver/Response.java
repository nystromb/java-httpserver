package scarvill.httpserver;

import java.util.ArrayList;
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
        for (String header : headers) {
            this.headers.add(header);
        }
        addDefaultHeaders();
    }

    public String getStatusLine() {
        return statusLine;
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
