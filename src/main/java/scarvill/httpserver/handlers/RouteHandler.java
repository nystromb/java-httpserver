package scarvill.httpserver.handlers;

import scarvill.httpserver.Request;
import scarvill.httpserver.Response;
import scarvill.httpserver.constants.Method;
import scarvill.httpserver.constants.Status;
import scarvill.httpserver.constants.StatusTwo;

import java.util.HashMap;
import java.util.function.Function;

public class RouteHandler implements Function<Request, Response> {
    private final Function<Request, Response> METHOD_NOT_ALLOWED_HANDLER =
        new IndifferentHandler(new Response(StatusTwo.METHOD_NOT_ALLOWED));

    private final HashMap<Method, Function<Request, Response>> methodHandlers;

    public RouteHandler(HashMap<Method, Function<Request, Response>> methodHandlers) {
        this.methodHandlers = methodHandlers;
    }

    @Override
    public Response apply(Request request) {
        Function<Request, Response> handler =
            methodHandlers.getOrDefault(request.getMethod(), METHOD_NOT_ALLOWED_HANDLER);

        return handler.apply(request);
    }
}
