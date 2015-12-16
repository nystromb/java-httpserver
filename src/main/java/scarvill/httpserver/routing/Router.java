package scarvill.httpserver.routing;

import scarvill.httpserver.request.Request;
import scarvill.httpserver.response.Response;

public interface Router {
    Response routeRequest(Request request);
}
