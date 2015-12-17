package scarvill.httpserver.response;

import java.util.HashMap;

public class ResponseBuilder {
    private Status status;
    private HashMap<String, String> headers = new HashMap<>();
    private byte[] body = new byte[]{};

    public ResponseBuilder setStatus(Status status) {
        this.status = status;
        return this;
    }

    public ResponseBuilder setHeader(String keyword, String content) {
        this.headers.put(keyword, content);
        return this;
    }

    public ResponseBuilder setBody(byte[] body) {
        this.body = body;
        return this;
    }

    public Response build() {
        return new Response(status, headers, body);
    }
}
