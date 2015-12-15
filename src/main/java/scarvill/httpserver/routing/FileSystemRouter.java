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

public class FileSystemRouter implements Router {
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

        if (Files.notExists(filePath)) {
            return virtualRouter.routeRequest(request);
        } else {
            HashMap<Method, Function<Request, Response>> resourceStrategies =
                generateResourceStrategies(new FileResource(filePath));
            Function<Request, Response> strategy =
                resourceStrategies.getOrDefault(request.getMethod(), NOT_FOUND_STRATEGY);
            return strategy.apply(request);
        }
    }

    private HashMap<Method, Function<Request, Response>> generateResourceStrategies(Resource resource) {
        HashMap<Method, Function<Request, Response>> resourceStrategies = new HashMap<>();
        resourceStrategies.put(Method.GET, new GetRouteResource(resource));
        resourceStrategies.put(Method.PUT, new ModifyRouteResource(resource));
        resourceStrategies.put(Method.POST, new ModifyRouteResource(resource));
        resourceStrategies.put(Method.PATCH, new ModifyRouteResource(resource, Status.NO_CONTENT));
        resourceStrategies.put(Method.DELETE, new ModifyRouteResource(resource));
        Collection<Method> allowedMethods = Arrays.asList(
            Method.GET, Method.PUT, Method.POST, Method.PATCH, Method.DELETE, Method.OPTIONS);
        resourceStrategies.put(Method.OPTIONS, new GetRouteOptions(allowedMethods));

        return resourceStrategies;
    }
}
