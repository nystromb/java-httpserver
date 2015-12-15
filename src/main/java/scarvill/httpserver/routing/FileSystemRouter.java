package scarvill.httpserver.routing;

import scarvill.httpserver.request.Method;
import scarvill.httpserver.request.Request;
import scarvill.httpserver.response.Response;
import scarvill.httpserver.response.ResponseBuilder;
import scarvill.httpserver.response.Status;
import scarvill.httpserver.routing.resource.FileResource;
import scarvill.httpserver.routing.resource.Resource;
import scarvill.httpserver.routing.route_strategies.GetRouteResource;
import scarvill.httpserver.routing.route_strategies.GiveStaticResponse;
import scarvill.httpserver.routing.route_strategies.ModifyRouteResource;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
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
                case GET:
                    return new GetRouteResource(resource).apply(request);
                default:
                    return new ModifyRouteResource(resource).apply(request);
            }
        }
    }
}
