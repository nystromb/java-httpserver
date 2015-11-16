package scarvill.httpserver.handlers;

import scarvill.httpserver.Request;
import scarvill.httpserver.Response;

import java.util.HashMap;
import java.util.function.Function;

public class RouteHandler implements Function<Request, Response> {
    private HashMap<String, Function<Request, Response>> methodHandlers;

    public RouteHandler(HashMap<String, Function<Request, Response>> methodHandlers) {
        this.methodHandlers = methodHandlers;
    }

    @Override
    public Response apply(Request request) {
        Function<Request, Response> handler =
            methodHandlers.getOrDefault(request.getMethod(), new MethodNotAllowedHandler());

        return handler.apply(request);
    }
}
