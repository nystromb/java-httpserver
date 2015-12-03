package scarvill.httpserver.cobspec;

import scarvill.httpserver.request.Method;
import scarvill.httpserver.response.Response;
import scarvill.httpserver.response.ResponseBuilder;
import scarvill.httpserver.response.Status;
import scarvill.httpserver.cobspec.route_strategies.ModifyRouteResource;
import scarvill.httpserver.cobspec.route_strategies.EchoRequestParameters;
import scarvill.httpserver.cobspec.route_strategies.GetRouteResource;
import scarvill.httpserver.cobspec.route_strategies.GiveStaticResponse;
import scarvill.httpserver.request.Request;
import scarvill.httpserver.routes.Resource;
import scarvill.httpserver.routes.Router;

import java.util.function.Function;

public class Cobspec {
    private static final Function<Request, Response> REDIRECT_HANDLER =
        new GiveStaticResponse(
            new ResponseBuilder()
                .setStatus(Status.FOUND)
                .setHeaders(new String[]{"Location: http://localhost:5000/\r\n"})
                .build());
    private static final Function<Request, Response> STATUS_OK_HANDLER =
        new GiveStaticResponse(
            new ResponseBuilder()
                .setStatus(Status.OK)
                .build());

    public static Router configuredRouter() {
        Router router = new Router();

        router.addRoute("/", Method.GET, new GetRouteResource(new Resource("")));

        Resource formResource = new Resource("");
        router.addRoute("/form", Method.GET, new GetRouteResource(formResource));
        router.addRoute("/form", Method.POST, new ModifyRouteResource(formResource));
        router.addRoute("/form", Method.PUT, new ModifyRouteResource(formResource));
        router.addRoute("/form", Method.DELETE, new ModifyRouteResource(formResource));

        router.addRoute("/redirect", Method.GET, REDIRECT_HANDLER);

        router.addRoute("/method_options", Method.GET, STATUS_OK_HANDLER);
        router.addRoute("/method_options", Method.PUT, STATUS_OK_HANDLER);
        router.addRoute("/method_options", Method.POST, STATUS_OK_HANDLER);
        router.addRoute("/method_options", Method.HEAD, STATUS_OK_HANDLER);

        router.addRoute("/file1", Method.GET, STATUS_OK_HANDLER);
        router.addRoute("/text-file.txt", Method.GET, STATUS_OK_HANDLER);

        router.addRoute("/parameters", Method.GET, new EchoRequestParameters());

        return router;
    }
}