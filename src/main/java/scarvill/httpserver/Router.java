package scarvill.httpserver;

import scarvill.httpserver.constants.Status;
import scarvill.httpserver.handlers.MethodNotAllowedHandler;
import scarvill.httpserver.handlers.NotFoundHandler;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class Router {
    private List<Route> configuredRoutes = new ArrayList<>();

    public void addRoute(String uri, String[] methodPermissions) {
        configuredRoutes.add(new Route(uri, methodPermissions));
    }

    public Response routeRequest(Request request) {
        for (Route route : configuredRoutes) {
            if (route.path.equals(request.getURI())) {
                if (Arrays.asList(route.permissions).contains(request.getMethod())) {
                    return new Response(Status.OK);
                } else {
                    return methodNotAllowed(request);
                }
            }
        }
        return routeNotFound(request);
    }

    private Response routeNotFound(Request request) {
        return new NotFoundHandler().apply(request);
    }

    private Response methodNotAllowed(Request request) {
        return new MethodNotAllowedHandler().apply(request);
    }

    private class Route {
        public String path;
        public String[] permissions;

        public Route(String path, String[] permissions) {
            this.path = path;
            this.permissions = permissions;
        }
    }
}
