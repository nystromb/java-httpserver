package scarvill.httpserver;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.function.Function;

public class Router {
    private List<Route> configuredRoutes = new ArrayList<>();

    public void addRoute(String route, String[] methodPermissions) {
        configuredRoutes.add(new Route(route, methodPermissions));
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
