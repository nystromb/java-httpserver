package scarvill.httpserver.cobspec.route_behavior;

import scarvill.httpserver.request.Request;
import scarvill.httpserver.response.Response;

import java.util.function.Function;

public class StaticRouteResponse implements Function<Request, Response> {
    private Response response;

    public StaticRouteResponse(Response responseToGive) {
        response = responseToGive;
    }

    @Override
    public Response apply(Request request) {
        return response;
    }
}
