package scarvill.httpserver.cobspec;

import scarvill.httpserver.*;
import scarvill.httpserver.constants.Method;
import scarvill.httpserver.constants.Status;
import scarvill.httpserver.handlers.RedirectHandler;
import scarvill.httpserver.handlers.RouteHandler;

import java.io.IOException;
import java.util.HashMap;
import java.util.function.Function;

public class ServerRunner {
    public static void main(String[] args) throws IOException {
        ServerArguments arguments = new ServerArguments(args);
        HTTPService service = new HTTPService(cobspecRouter());
        Server server = new Server(arguments.port, service);

        server.start();
    }

    private static Router cobspecRouter() {
        Router router = new Router();
        router.addRoute("/", assignHandlerToMultipleMethods(statusOKHandler(),
            new String[]{Method.GET}));
        router.addRoute("/form", assignHandlerToMultipleMethods(statusOKHandler(),
            new String[]{Method.GET, Method.POST, Method.PUT}));
        router.addRoute("/redirect", assignHandlerToMultipleMethods(new RedirectHandler("http://localhost:5000/"),
            Method.all_methods()));
        return router;
    }

    private static RouteHandler assignHandlerToMultipleMethods(Function<Request, Response> handler, String[] methods) {
        HashMap<String, Function<Request, Response>> methodHandlers = new HashMap<>();
        for (String method : methods) {
            methodHandlers.put(method, handler);
        }
        return new RouteHandler(methodHandlers);
    }

    private static Function<Request, Response> statusOKHandler() {
        return (request) -> new Response(Status.OK);
    }
}
