package scarvill.httpserver;

import scarvill.httpserver.constants.Status;

public class ResponseBuilder {
    private Status status;
    private String[] headers = new String[]{};
    private String body = "";

    public ResponseBuilder setStatus(Status status) {
        this.status = status;
        return this;
    }

    public ResponseBuilder setHeaders(String[] headers) {
        this.headers = headers;
        return this;
    }

    public ResponseBuilder setBody(String body) {
        this.body = body;
        return this;
    }

    public Response build() {
        return new Response(status, headers, body);
    }
}
