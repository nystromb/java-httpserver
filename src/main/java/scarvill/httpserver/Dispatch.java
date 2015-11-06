package scarvill.httpserver;

public class Dispatch {
    public static String handle(Request request) {
        if (request.getAction().equals("GET") && !request.getURI().equals("/foobar")) {
            return Status.OK;
        } else if (request.getAction().equals("OPTIONS")) {
            return Status.OK + "Allow: GET,HEAD,POST,OPTIONS,PUT\r\n";
        } else if (request.getAction().equals("PUT") || request.getAction().equals("POST")) {
            return Status.OK;
        } else {
            return Status.NOT_FOUND;
        }
    }
}