package scarvill.httpserver.routing.route_strategies;

import scarvill.httpserver.request.Request;
import scarvill.httpserver.response.Response;

import java.util.function.Function;

public class GiveStaticResponse implements Function<Request, Response> {
    private Response response;

    public GiveStaticResponse(Response responseToGive) {
        response = responseToGive;
    }

    @Override
    public Response apply(Request request) {
        return response;
    }
}
