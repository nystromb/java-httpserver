package scarvill.httpserver;

import scarvill.httpserver.constants.StatusTwo;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

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

    public static class Builder {
        private StatusTwo status;
        private String[] headers = new String[]{};
        private String body;

        public Builder setStatus(StatusTwo status) {
            this.status = status;
            return this;
        }

        public Builder setHeaders(String[] headers) {
            this.headers = headers;
            return this;
        }

        public Builder setBody(String body) {
            this.body = body;
            return this;
        }

        public Response build() {
            return new Response(status, headers, body);
        }
    }
}
