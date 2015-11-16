package scarvill.httpserver;

public class MethodNotAllowedHandler {
    public Response apply(Request anyRequest) {
        return new Response(Status.METHOD_NOT_ALLOWED);
    }
}
