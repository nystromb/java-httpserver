package scarvill.httpserver;

public class RequestBuilder {
    public static String build(String method, String uri) {
        return method + " " + uri + " HTTP/1.1\r\n";
    }
}
