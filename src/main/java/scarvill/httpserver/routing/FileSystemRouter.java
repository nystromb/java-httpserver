package scarvill.httpserver.routing;

import scarvill.httpserver.request.Request;
import scarvill.httpserver.response.Response;
import scarvill.httpserver.response.ResponseBuilder;
import scarvill.httpserver.response.Status;
import scarvill.httpserver.routing.resource.FileResource;
import scarvill.httpserver.routing.resource.Resource;
import scarvill.httpserver.routing.route_strategies.GetRouteOptions;
import scarvill.httpserver.routing.route_strategies.GetRouteResource;
import scarvill.httpserver.routing.route_strategies.GiveStaticResponse;

import java.io.File;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.function.Function;

import static scarvill.httpserver.request.Method.*;

public class FileSystemRouter implements Router {
    private Path rootDirectory;

    public final Function<Request, Response> NOT_FOUND_STRATEGY =
        new GiveStaticResponse(
            new ResponseBuilder()
                .setStatus(Status.NOT_FOUND)
                .build());

    public final Function<Request, Response> METHOD_NOT_ALLOWED_STRATEGY =
        new GiveStaticResponse(
            new ResponseBuilder()
                .setStatus(Status.METHOD_NOT_ALLOWED)
                .build());

    public FileSystemRouter() {}

    public FileSystemRouter(Path rootDirectory) {
        setRootDirectory(rootDirectory);
    }

    public void setRootDirectory(Path directory) {
        rootDirectory = directory;
    }

    @Override
    public Response routeRequest(Request request) {
        if (servedDirectoryNotSet() || Files.notExists(filePath(request.getURI()))) {
            return NOT_FOUND_STRATEGY.apply(request);
        } else {
            Resource resource = new FileResource(filePath(request.getURI()));
            return applyDefaultFileRoutingStrategy(request, resource);
        }
    }

    private boolean servedDirectoryNotSet() {
        return rootDirectory == null;
    }

    private Path filePath(String uri) {
        return Paths.get(rootDirectory + uri);
    }

    private Response applyDefaultFileRoutingStrategy(Request request, Resource resource) {
        switch (request.getMethod()) {
            case GET:
                return new GetRouteResource(resource).apply(request);
            case OPTIONS:
                return new GetRouteOptions(Arrays.asList(GET, OPTIONS)).apply(request);
            default:
                return METHOD_NOT_ALLOWED_STRATEGY.apply(request);
        }
    }
}
