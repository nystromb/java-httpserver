package scarvill.httpserver.response;

public enum Status {
    OK("200 OK"),
    NOT_FOUND("404 Not Found"),
    METHOD_NOT_ALLOWED("405 Method Not Allowed"),
    FOUND("302 Found"),
    PARTIAL_CONTENT("206 Partial Content"),
    UNAUTHORIZED("401 Unauthorized"),
    NO_CONTENT("204 No Content");

    private final String statusString;

    Status(String statusString) {
        this.statusString = statusString;
    }

    public String toString() {
        return this.statusString;
    }
}
