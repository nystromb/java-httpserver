package scarvill.httpserver.cobspec;

import scarvill.httpserver.Request;
import scarvill.httpserver.Response;
import scarvill.httpserver.Router;
import scarvill.httpserver.constants.Method;
import scarvill.httpserver.constants.Status;
import scarvill.httpserver.handlers.IndifferentHandler;
import scarvill.httpserver.handlers.RouteHandler;

import java.util.HashMap;
import java.util.function.Function;

public class Cobspec {
    private static final Function<Request, Response> STATUS_OK_HANDLER =
        new IndifferentHandler(new Response(Status.OK));
    private static final Function<Request, Response> REDIRECT_HANDLER =
        new IndifferentHandler(
            new Response(Status.FOUND, new String[]{"Location: http://localhost:5000\r\n"}));

    public static Router configuredRouter() {
        Router router = new Router();
        router.addRoute("/", assignHandlerToMethods(STATUS_OK_HANDLER,
            new String[]{Method.GET}));
        router.addRoute("/form", assignHandlerToMethods(STATUS_OK_HANDLER,
            new String[]{Method.GET, Method.POST, Method.PUT}));
        router.addRoute("/redirect", assignHandlerToMethods(REDIRECT_HANDLER,
            Method.all_methods()));
        return router;
    }

    private static RouteHandler assignHandlerToMethods(Function<Request, Response> handler, String[] methods) {
        HashMap<String, Function<Request, Response>> methodHandlers = new HashMap<>();
        for (String method : methods) {
            methodHandlers.put(method, handler);
        }
        return new RouteHandler(methodHandlers);
    }
}