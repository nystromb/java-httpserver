package scarvill.httpserver.routing;

import scarvill.httpserver.request.Request;
import scarvill.httpserver.resource.FileResource;
import scarvill.httpserver.resource.Resource;
import scarvill.httpserver.response.Response;
import scarvill.httpserver.response.ResponseBuilder;
import scarvill.httpserver.response.Status;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.function.Function;

import static scarvill.httpserver.request.Method.GET;
import static scarvill.httpserver.request.Method.OPTIONS;

public class RouteToDirectoryResources implements Function<Request, Response> {
    private Path rootDirectory;

    public RouteToDirectoryResources() {
    }

    public RouteToDirectoryResources(Path rootDirectory) {
        setRootDirectory(rootDirectory);
    }

    public void setRootDirectory(Path directory) {
        rootDirectory = directory;
    }

    @Override
    public Response apply(Request request) {
        if (servedDirectoryNotSet() || Files.notExists(filePath(request.getURI()))) {
            return new ResponseBuilder()
                .setStatus(Status.NOT_FOUND)
                .setBody(Status.NOT_FOUND.toString().getBytes())
                .build();
        } else {
            return applyFileRoutingStrategy(request);
        }
    }

    private Response applyFileRoutingStrategy(Request request) {
        switch (request.getMethod()) {
            case GET:
                return respondWithFileResourceOrDirectoryIndex(request);
            case OPTIONS:
                return new GetRouteOptions(Arrays.asList(GET, OPTIONS)).apply(request);
            default:
                return new ResponseBuilder()
                    .setStatus(Status.METHOD_NOT_ALLOWED)
                    .setBody(Status.METHOD_NOT_ALLOWED.toString().getBytes())
                    .build();
        }
    }

    private Response respondWithFileResourceOrDirectoryIndex(Request request) {
        if (routePointsToADirectory(request.getURI())) {
            return new GetDirectoryIndex(rootDirectory).apply(request);
        } else {
            Resource resource = new FileResource(filePath(request.getURI()));
            return new GetRouteResource(resource).apply(request);
        }
    }

    private Path filePath(String uri) {
        return Paths.get(rootDirectory + uri);
    }

    private boolean servedDirectoryNotSet() {
        return rootDirectory == null;
    }

    private boolean routePointsToADirectory(String uri) {
        return Files.isDirectory(filePath(uri));
    }
}
