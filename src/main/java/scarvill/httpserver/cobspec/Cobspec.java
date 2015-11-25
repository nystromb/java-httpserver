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

import java.util.function.Function;

public class Cobspec {
    private static final Function<Request, Response> REDIRECT_HANDLER =
        new IndifferentHandler(
            new Response.Builder()
                .setStatus(Status.FOUND)
                .setHeaders(new String[]{"Location: http://localhost:5000/\r\n"})
                .build());
    private static final Function<Request, Response> STATUS_OK_HANDLER =
        new IndifferentHandler(
            new Response.Builder()
                .setStatus(Status.OK)
                .build());

    public static Router configuredRouter() {
        Router router = new Router();

        router.addRoute("/", Method.GET, new GetResourceHandler(new Resource("")));

        Resource formResource = new Resource("");
        router.addRoute("/form", Method.GET, new GetResourceHandler(formResource));
        router.addRoute("/form", Method.POST, new ChangeResourceHandler(formResource));
        router.addRoute("/form", Method.PUT, new ChangeResourceHandler(formResource));
        router.addRoute("/form", Method.DELETE, new ChangeResourceHandler(formResource));

        router.addRoute("/redirect", Method.GET, REDIRECT_HANDLER);

        router.addRoute("/method_options", Method.GET, STATUS_OK_HANDLER);
        router.addRoute("/method_options", Method.PUT, STATUS_OK_HANDLER);
        router.addRoute("/method_options", Method.POST, STATUS_OK_HANDLER);
        router.addRoute("/method_options", Method.HEAD, STATUS_OK_HANDLER);

        return router;
    }
}