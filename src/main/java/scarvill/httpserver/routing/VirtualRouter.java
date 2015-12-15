package scarvill.httpserver.routing;

import scarvill.httpserver.routing.route_strategies.GetRouteOptions;
import scarvill.httpserver.routing.route_strategies.GiveStaticResponse;
import scarvill.httpserver.request.Method;
import scarvill.httpserver.request.Request;
import scarvill.httpserver.response.Response;
import scarvill.httpserver.response.ResponseBuilder;
import scarvill.httpserver.response.Status;

import java.util.HashMap;
import java.util.Set;
import java.util.function.Function;
import java.util.stream.Collectors;

public class VirtualRouter implements Router {
    private final Function<Request, Response> NOT_FOUND_STRATEGY =
        new GiveStaticResponse(new ResponseBuilder().setStatus(Status.NOT_FOUND).build());
    private final Function<Request, Response> METHOD_NOT_ALLOWED_STRATEGY =
        new GiveStaticResponse(new ResponseBuilder().setStatus(Status.METHOD_NOT_ALLOWED).build());

    private HashMap<String, HashMap<Method, Function<Request, Response>>> routes = new HashMap<>();

    @Override
    public void addRoute(String uri, Method method, Function<Request, Response> strategy) {
        HashMap<Method, Function<Request, Response>> routeStrategies =
            routes.getOrDefault(uri, new HashMap<>());
        routeStrategies.put(method, strategy);
        routeStrategies.put(Method.OPTIONS, new GetRouteOptions(routeStrategies.keySet()));
        routes.put(uri, routeStrategies);
    }

    @Override
    public Response routeRequest(Request request) {
        HashMap<Method, Function<Request, Response>> routeStrategies = routes.get(request.getURI());
        if (routeStrategies == null) {
            return NOT_FOUND_STRATEGY.apply(request);
        } else {
            return routeRequestByMethod(request, routeStrategies);
        }
    }

    private Response routeRequestByMethod(
        Request request, HashMap<Method, Function<Request, Response>> routeStrategies) {
        Function<Request, Response> strategy =
            routeStrategies.getOrDefault(request.getMethod(), METHOD_NOT_ALLOWED_STRATEGY);
        return strategy.apply(request);
    }
}
