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
import scarvill.httpserver.routing.route_strategies.ModifyRouteResource;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.Collection;
import java.util.function.Function;

public class FileSystemRouter implements Router {
    private Path publicDirectory;
    private VirtualRouter virtualRouter;

    public FileSystemRouter(Path publicDirectory) {
        this.publicDirectory = publicDirectory;
        this.virtualRouter = new VirtualRouter();
    }

    @Override
    public void addRoute(String uri, Method method, Function<Request, Response> strategy) {
        virtualRouter.addRoute(uri, method, strategy);
    }

    @Override
    public Response routeRequest(Request request) {
        Path filePath = Paths.get(publicDirectory + request.getURI());

        if (Files.notExists(filePath)) {
            return virtualRouter.routeRequest(request);
        } else {
            Resource resource = new FileResource(filePath);
            switch (request.getMethod()) {
                case NULL_METHOD:
                    return new ResponseBuilder().setStatus(Status.METHOD_NOT_ALLOWED).build();
                case OPTIONS:
                    Collection<Method> allowedMethods =
                        Arrays.asList(Method.GET, Method.PUT, Method.POST, Method.DELETE);
                    return new GetRouteOptions(allowedMethods).apply(request);
                case GET:
                    return new GetRouteResource(resource).apply(request);
                case PATCH:
                    return new ModifyRouteResource(resource, Status.NO_CONTENT).apply(request);
                default:
                    return new ModifyRouteResource(resource).apply(request);
            }
        }
    }
}
