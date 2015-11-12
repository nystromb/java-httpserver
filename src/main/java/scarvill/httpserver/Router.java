package scarvill.httpserver;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class Router {
    private class Route {
        public String path;
        public String[] permissions;

        public Route(String path, String[] permissions) {
            this.path = path;
            this.permissions = permissions;
        }
    }

    private List<Route> configuredRoutes = new ArrayList<>();
    
    public void addRoute(String route, String[] methodPermissions) {
        configuredRoutes.add(new Route(route, methodPermissions));
    }

    public Response routeRequest(Request request) {
        for (Route route : configuredRoutes) {
            if (route.path.equals(request.getURI())) {
                if (Arrays.asList(route.permissions).contains(request.getAction())) {
                    return new Response(Status.OK);
                } else {
                    return new Response(Status.METHOD_NOT_ALLOWED);
                }
            }
        }
        return new Response(Status.NOT_FOUND);
    }
}
