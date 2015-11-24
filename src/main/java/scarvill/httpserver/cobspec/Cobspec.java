package scarvill.httpserver.cobspec;

import scarvill.httpserver.Request;
import scarvill.httpserver.Resource;
import scarvill.httpserver.Response;
import scarvill.httpserver.Router;
import scarvill.httpserver.constants.Method;
import scarvill.httpserver.constants.Status;
import scarvill.httpserver.handlers.ChangeResourceHandler;
import scarvill.httpserver.handlers.GetResourceHandler;
import scarvill.httpserver.handlers.IndifferentHandler;
import scarvill.httpserver.handlers.RouteHandler;

import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

public class Cobspec {
    private static final Function<Request, Response> REDIRECT_HANDLER =
        new IndifferentHandler(
            new Response.Builder()
                .setStatus(Status.FOUND)
                .setHeaders(new String[]{"Location: http://localhost:5000/\r\n"})
                .build());

    public static Router configuredRouter() {
        Router router = new Router();
        router.addRoute("/", unmodifiableResourceRouteHandler(new Resource("")));
        router.addRoute("/form", resourcefulRouteHandler(new Resource("")));
        router.addRoute("/redirect", REDIRECT_HANDLER);
        return router;
    }

    private static Function<Request, Response> unmodifiableResourceRouteHandler(Resource resource) {
        HashMap<Method, Function<Request, Response>> methodHandlers = new HashMap<>();
        methodHandlers.put(Method.GET, new GetResourceHandler(resource));
        addOptionsHandler(methodHandlers);

        return new RouteHandler(methodHandlers);
    }

    private static Function<Request, Response> resourcefulRouteHandler(Resource resource) {
        HashMap<Method, Function<Request, Response>> methodHandlers = new HashMap<>();
        methodHandlers.put(Method.GET, new GetResourceHandler(resource));
        for (Method method : new Method[]{Method.POST, Method.PUT, Method.DELETE}) {
            methodHandlers.put(method, new ChangeResourceHandler(resource));
        }
        addOptionsHandler(methodHandlers);

        return new RouteHandler(methodHandlers);
    }

    private static Function<Request, Response> optionsHandler(List<Method> allowedMethods) {
        List<String> methodNames = allowedMethods.stream()
            .map(Method::toString)
            .collect(Collectors.toList());
        String[] headers = new String[] {"Allow: " + String.join(",", methodNames) + "\r\n"};
        return new IndifferentHandler(
            new Response.Builder().setStatus(Status.OK).setHeaders(headers).build());
    }

    private static void addOptionsHandler(HashMap<Method, Function<Request, Response>> methodHandlers) {
        List<Method> methods = new ArrayList<>();
        methods.addAll(methodHandlers.keySet());

        methodHandlers.put(Method.OPTIONS, optionsHandler(methods));
    }
}