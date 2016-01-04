package scarvill.httpserver.routing;

import scarvill.httpserver.request.Method;
import scarvill.httpserver.request.Request;
import scarvill.httpserver.response.Response;
import scarvill.httpserver.response.ResponseBuilder;
import scarvill.httpserver.response.Status;

import java.nio.file.Path;
import java.util.HashMap;
import java.util.function.Function;

public class RouteRequest implements Function<Request, Response> {
    private HashMap<String, HashMap<Method, Function<Request, Response>>> routes = new HashMap<>();
    private RouteToDirectoryResources directoryRouter = new RouteToDirectoryResources();

    public void addRoute(String uri, Method method, Function<Request, Response> strategy) {
        HashMap<Method, Function<Request, Response>> routeStrategies =
            routes.getOrDefault(uri, new HashMap<>());
        routeStrategies.put(method, strategy);
        routeStrategies.put(Method.OPTIONS, new GetRouteOptions(routeStrategies.keySet()));
        routes.put(uri, routeStrategies);
    }

    @Override
    public Response apply(Request request) {
        HashMap<Method, Function<Request, Response>> routeStrategies = routes.get(request.getURI());

        if (routeStrategies == null) {
            return directoryRouter.apply(request);
        } else {
            return routeRequestByMethod(request, routeStrategies);
        }
    }

    public void routeToResourcesInDirectory(Path rootDirectory) {
        this.directoryRouter.setRootDirectory(rootDirectory);
    }

    private Response routeRequestByMethod(Request request,
                                          HashMap<Method, Function<Request, Response>> routeStrategies) {
        Function<Request, Response> strategy =
            routeStrategies.getOrDefault(request.getMethod(), methodNotAllowedStrategy());
        return strategy.apply(request);
    }

    private Function<Request, Response> methodNotAllowedStrategy() {
        return new GiveStaticResponse(
            new ResponseBuilder()
                .setStatus(Status.METHOD_NOT_ALLOWED)
                .setBody(Status.METHOD_NOT_ALLOWED.toString().getBytes())
                .build());
    }
}


// I think the nested hashmap makes things a little confusing in order to understand the request chain path

// There are multiple places (in RouteToDirectoryResources) where you check to see if the Method is not allowed.
// Maybe slip some kind of handler in the request chain to handle this and remove the redundancy?