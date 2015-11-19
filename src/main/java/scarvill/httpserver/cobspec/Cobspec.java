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

import java.util.HashMap;
import java.util.Set;
import java.util.function.Function;

public class Cobspec {
    private static final Function<Request, Response> REDIRECT_HANDLER =
        new IndifferentHandler(
            new Response(Status.FOUND, new String[]{"Location: http://localhost:5000/\r\n"}));

    public static Router configuredRouter() {
        Router router = new Router();
        router.addRoute("/", unmodifiableResourceRouteHandler(new Resource("")));
        router.addRoute("/form", resourcefulRouteHandler(new Resource("")));
        router.addRoute("/redirect", REDIRECT_HANDLER);
        router.addRoute("/method_options", optionsHandler(Method.allMethods()));
        return router;
    }

    private static Function<Request, Response> unmodifiableResourceRouteHandler(Resource resource) {
        HashMap<String, Function<Request, Response>> methodHandlers = new HashMap<>();
        methodHandlers.put(Method.GET, new GetResourceHandler(resource));
        addOptionsHandler(methodHandlers);

        return new RouteHandler(methodHandlers);
    }

    private static Function<Request, Response> resourcefulRouteHandler(Resource resource) {
        HashMap<String, Function<Request, Response>> methodHandlers = new HashMap<>();
        methodHandlers.put(Method.GET, new GetResourceHandler(resource));
        for (String method : new String[]{Method.POST, Method.PUT, Method.DELETE}) {
            methodHandlers.put(method, new ChangeResourceHandler(resource));
        }
        addOptionsHandler(methodHandlers);

        return new RouteHandler(methodHandlers);
    }

    private static Function<Request, Response> optionsHandler(String[] allowedMethods) {
        String[] headers = new String[] {"Allow: " + String.join(",", allowedMethods) + "\r\n"};
        return new IndifferentHandler(new Response(Status.OK, headers));
    }

    private static void addOptionsHandler(HashMap<String, Function<Request, Response>> methodHandlers) {
        Set<String> methods = methodHandlers.keySet();
        methodHandlers.put(Method.OPTIONS, optionsHandler(methods.toArray(new String[methods.size()])));
    }
}