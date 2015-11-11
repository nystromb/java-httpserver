package scarvill.httpserver;

public class Response {
    private String statusLine;

    public Response(String status) {
        statusLine = status;
    }

    public String getStatusLine() {
        return statusLine;
    }
}
