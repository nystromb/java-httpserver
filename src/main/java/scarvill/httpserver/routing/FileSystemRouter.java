package scarvill.httpserver.routing;

import scarvill.httpserver.request.Method;
import scarvill.httpserver.request.Request;
import scarvill.httpserver.response.Response;
import scarvill.httpserver.response.ResponseBuilder;
import scarvill.httpserver.response.Status;
import scarvill.httpserver.routing.resource.FileResource;
import scarvill.httpserver.routing.resource.Resource;
import scarvill.httpserver.routing.route_strategies.GetRouteOptions;
import scarvill.httpserver.routing.route_strategies.GetRouteResource;
import scarvill.httpserver.routing.route_strategies.GiveStaticResponse;
import scarvill.httpserver.routing.route_strategies.ModifyRouteResource;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashMap;
import java.util.function.Function;

import static scarvill.httpserver.request.Method.*;

public class FileSystemRouter implements Router {
    private final Function<Request, Response> METHOD_NOT_ALLOWED_STRATEGY =
        new GiveStaticResponse(new ResponseBuilder().setStatus(Status.METHOD_NOT_ALLOWED).build());
    private final Function<Request, Response> NOT_FOUND_STRATEGY =
        new GiveStaticResponse(new ResponseBuilder().setStatus(Status.NOT_FOUND).build());

    private final Path publicDirectory;
    private VirtualResourceRouter virtualRouter;

    public FileSystemRouter(Path publicDirectory) {
        this.publicDirectory = publicDirectory;
        this.virtualRouter = new VirtualResourceRouter();
    }

    @Override
    public void addRoute(String uri, Method method, Function<Request, Response> strategy) {
        virtualRouter.addRoute(uri, method, strategy);
    }

    @Override
    public Response routeRequest(Request request) {
        Path filePath = Paths.get(publicDirectory + request.getURI());

        if (virtualRouter.hasRoute(request.getURI())) {
            return virtualRouter.routeRequest(request);
        } else if (Files.notExists(filePath)) {
            return NOT_FOUND_STRATEGY.apply(request);
        } else {
            return applyDefaultFileStrategy(request, new FileResource(filePath));
        }
    }

    private Response applyDefaultFileStrategy(Request request, Resource resource) {
        switch (request.getMethod()) {
            case GET: return new GetRouteResource(resource).apply(request);
            case OPTIONS: return new GetRouteOptions(Arrays.asList(GET, OPTIONS)).apply(request);
            default: return METHOD_NOT_ALLOWED_STRATEGY.apply(request);
        }
    }
}
