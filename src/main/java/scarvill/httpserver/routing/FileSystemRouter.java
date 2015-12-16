package scarvill.httpserver.routing;

import scarvill.httpserver.request.Method;
import scarvill.httpserver.request.Request;
import scarvill.httpserver.response.Response;
import scarvill.httpserver.routing.resource.FileResource;
import scarvill.httpserver.routing.resource.Resource;
import scarvill.httpserver.routing.route_strategies.GetRouteOptions;
import scarvill.httpserver.routing.route_strategies.GetRouteResource;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.function.Function;

import static scarvill.httpserver.request.Method.*;

public class FileSystemRouter implements Router {
    private final Path publicDirectory;
    private VirtualResourceRouter configurableRouter;

    public FileSystemRouter(Path publicDirectory) {
        this.publicDirectory = publicDirectory;
        this.configurableRouter = new VirtualResourceRouter();
    }

    @Override
    public void addRoute(String uri, Method method, Function<Request, Response> strategy) {
        configurableRouter.addRoute(uri, method, strategy);
    }

    @Override
    public Response routeRequest(Request request) {
        Path filePath = Paths.get(publicDirectory + request.getURI());

        if (configurableRouter.hasRoute(request.getURI())) {
            return configurableRouter.routeRequest(request);
        } else if (Files.notExists(filePath)) {
            return configurableRouter.NOT_FOUND_STRATEGY.apply(request);
        } else {
            return applyDefaultFileRoutingStrategy(request, new FileResource(filePath));
        }
    }

    private Response applyDefaultFileRoutingStrategy(Request request, Resource resource) {
        switch (request.getMethod()) {
            case GET:
                return new GetRouteResource(resource).apply(request);
            case OPTIONS:
                return new GetRouteOptions(Arrays.asList(GET, OPTIONS)).apply(request);
            default:
                return configurableRouter.METHOD_NOT_ALLOWED_STRATEGY.apply(request);
        }
    }
}
