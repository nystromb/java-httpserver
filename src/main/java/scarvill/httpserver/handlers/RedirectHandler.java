package scarvill.httpserver.handlers;

import scarvill.httpserver.Request;
import scarvill.httpserver.Response;
import scarvill.httpserver.constants.Status;

import java.util.function.Function;

public class RedirectHandler implements Function<Request, Response> {
    private String redirectLocation;

    public RedirectHandler(String redirectLocation) {
        this.redirectLocation = redirectLocation;
    }

    @Override
    public Response apply(Request request) {
        return new Response(Status.FOUND, new String[]{"Location: " + redirectLocation + "\r\n"});
    }
}
