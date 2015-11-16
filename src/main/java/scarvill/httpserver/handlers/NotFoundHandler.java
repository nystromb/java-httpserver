package scarvill.httpserver.handlers;

import scarvill.httpserver.Request;
import scarvill.httpserver.Response;
import scarvill.httpserver.constants.Status;

import java.util.function.Function;

public class NotFoundHandler implements Function<Request, Response> {

    public Response apply(Request request) {
        return new Response(Status.NOT_FOUND);
    }
}
