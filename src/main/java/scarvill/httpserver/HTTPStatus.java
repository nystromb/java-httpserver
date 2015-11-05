package scarvill.httpserver;

public class HTTPStatus {
    public static final String OK              = "HTTP/1.1 200 OK\r\n";
    public static final String NO_CONTENT      = "HTTP/1.1 204 No Content\r\n";
    public static final String PARTIAL_CONTENT = "HTTP/1.1 206 Partial Content\r\n";
    public static final String FOUND           = "HTTP/1.1 302 Found\r\n";
    public static final String UNAUTHORIZED    = "HTTP/1.1 401 Unauthorized\r\n";
    public static final String NOT_FOUND       = "HTTP/1.1 404 NOT FOUND\r\n";
    public static final String NOT_ALLOWED     = "HTTP/1.1 405 Method Not Allowed\r\n";
}

