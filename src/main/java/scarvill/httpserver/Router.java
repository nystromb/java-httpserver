package scarvill.httpserver;

import scarvill.httpserver.constants.Method;
import scarvill.httpserver.constants.Status;
import scarvill.httpserver.handlers.IndifferentHandler;

import java.beans.MethodDescriptor;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Set;
import java.util.function.Function;
import java.util.stream.Collectors;

public class Router {
    private final Function<Request, Response> NOT_FOUND_HANDLER =
        new IndifferentHandler(new Response.Builder().setStatus(Status.NOT_FOUND).build());
    private final Function<Request, Response> METHOD_NOT_ALLOWED_HANDLER =
        new IndifferentHandler(new Response.Builder().setStatus(Status.METHOD_NOT_ALLOWED).build());

    private HashMap<String, HashMap<Method, Function<Request, Response>>> routes = new HashMap<>();

    public void addRoute(String uri, Method method, Function<Request, Response> handler) {
        HashMap<Method, Function<Request, Response>> methodHandlers =
            routes.getOrDefault(uri, new HashMap<>());
        methodHandlers.put(method, handler);
        methodHandlers.put(Method.OPTIONS, optionsHandler(methodHandlers.keySet()));
        routes.put(uri, methodHandlers);
    }

    public Response routeRequest(Request request) {
        HashMap<Method, Function<Request, Response>> methodHandlers = routes.get(request.getURI());
        if (methodHandlers == null) {
            return NOT_FOUND_HANDLER.apply(request);
        } else {
            Function<Request, Response> handler =
                methodHandlers.getOrDefault(request.getMethod(), METHOD_NOT_ALLOWED_HANDLER);
            return handler.apply(request);
        }
    }

    private Function<Request, Response> optionsHandler(Set<Method> allowedMethods) {
        return new IndifferentHandler(
            new Response.Builder()
                .setStatus(Status.OK)
                .setHeaders(new String[]{"Allow: " + optionsMethodString(allowedMethods) + "\r\n"})
                .build());
    }

    private String optionsMethodString(Set<Method> allowedMethods) {
        String methodString =
            allowedMethods.stream().map(Method::toString).collect(Collectors.joining(","));

        if (!methodString.contains(Method.OPTIONS.toString())) {
            methodString += "," + Method.OPTIONS.toString();
        }

        return methodString;
    }
}
