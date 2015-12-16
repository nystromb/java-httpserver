package scarvill.httpserver.routing;

import scarvill.httpserver.request.Method;
import scarvill.httpserver.request.Request;
import scarvill.httpserver.response.Response;

import java.util.function.Function;

public interface Router {
    void addRoute(String uri, Method method, Function<Request, Response> strategy);
    Response routeRequest(Request request);
}
