package scarvill.httpserver;

public class RequestUtility {
    public static String rawRequest(String method, String uri) {
        return method + " " + uri + " HTTP/1.1\r\n";
    }
}
