package scarvill.httpserver;

import scarvill.httpserver.handlers.RouteNotFoundHandler;
import scarvill.httpserver.handlers.RouteHandler;

import java.util.ArrayList;
import java.util.List;

public class Router {
    private List<Route> configuredRoutes = new ArrayList<>();

    public void addRoute(String uri, RouteHandler handler) {
        configuredRoutes.add(new Route(uri, handler));
    }

    public Response routeRequest(Request request) {
        for (Route route : configuredRoutes) {
            if (route.uri.equals(request.getURI())) {
                return route.handler.apply(request);
            }
        }
        return new RouteNotFoundHandler().apply(request);
    }

    private class Route {
        public String uri;
        public RouteHandler handler;

        public Route(String uri, RouteHandler handler) {
            this.uri = uri;
            this.handler = handler;
        }
    }
}
