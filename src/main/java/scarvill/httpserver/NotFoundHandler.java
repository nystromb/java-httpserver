package scarvill.httpserver;

import java.util.function.Function;

public class NotFoundHandler implements Function<Request, Response> {

    public Response apply(Request request) {
        return new Response(Status.NOT_FOUND);
    }
}
