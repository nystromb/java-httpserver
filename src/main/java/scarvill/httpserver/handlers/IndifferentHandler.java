package scarvill.httpserver.handlers;

import scarvill.httpserver.Request;
import scarvill.httpserver.Response;

import java.util.function.Function;

public class IndifferentHandler implements Function<Request, Response> {
    private Response response;

    public IndifferentHandler(Response responseToGive) {
        response = responseToGive;
    }

    @Override
    public Response apply(Request request) {
        return response;
    }
}
