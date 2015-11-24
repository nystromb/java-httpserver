package scarvill.httpserver;

import scarvill.httpserver.constants.StatusTwo;
import scarvill.httpserver.handlers.IndifferentHandler;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Function;

public class Router {
    private final Function<Request, Response> NOT_FOUND_HANDLER =
        new IndifferentHandler(new Response.Builder().setStatus(StatusTwo.NOT_FOUND).build());

    private List<Route> configuredRoutes = new ArrayList<>();

    public void addRoute(String uri, Function<Request, Response> handler) {
        configuredRoutes.add(new Route(uri, handler));
    }

    public Response routeRequest(Request request) {
        for (Route route : configuredRoutes) {
            if (route.uri.equals(request.getURI())) {
                return route.handler.apply(request);
            }
        }
        return NOT_FOUND_HANDLER.apply(request);
    }

    private class Route {
        public String uri;
        public Function<Request, Response> handler;

        public Route(String uri, Function<Request, Response> handler) {
            this.uri = uri;
            this.handler = handler;
        }
    }
}
