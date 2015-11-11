package scarvill.httpserver;

import java.util.ArrayList;
import java.util.List;

public class Router {
    private List<String> configuredRoutes = new ArrayList<>();

    public Router(String[] routes) {
        for (String route : routes ) {
            addRoute(route);
        }
    }

    public void addRoute(String route) {
        configuredRoutes.add(route);
    }

    public Response routeRequest(Request request) {
        if (configuredRoutes.contains(request.getURI())) {
            return new Response(Status.OK);
        } else {
            return new Response(Status.NOT_FOUND);
        }
    }
}
