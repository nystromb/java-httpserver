package scarvill.httpserver.handlers;

import scarvill.httpserver.Request;
import scarvill.httpserver.Response;
import scarvill.httpserver.constants.Status;

import java.util.function.Function;

public class MethodNotAllowedHandler implements Function<Request, Response> {
    public Response apply(Request anyRequest) {
        return new Response(Status.METHOD_NOT_ALLOWED);
    }
}
