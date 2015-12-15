package scarvill.httpserver.routing;

import org.junit.Test;
import scarvill.httpserver.request.Method;
import scarvill.httpserver.request.Request;
import scarvill.httpserver.request.RequestBuilder;
import scarvill.httpserver.response.Response;
import scarvill.httpserver.response.ResponseBuilder;
import scarvill.httpserver.response.Status;
import scarvill.httpserver.routing.route_strategies.GiveStaticResponse;
import scarvill.httpserver.server.Logger;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.junit.Assert.*;

public class FileSystemRouterTest {
    @Test
    public void testDelegatesManuallyConfiguredRoutesToVirtualRouter() {
        Request request = new RequestBuilder().setMethod(Method.GET).setURI("/").build();
        Status expectedResponseStatus = Status.OK;
        Router router = new FileSystemRouter(Paths.get("/foo"));
        Response response = new ResponseBuilder().setStatus(Status.OK).build();
        router.addRoute("/", Method.GET, new GiveStaticResponse(response));

        Response routerResponse = router.routeRequest(request);

        assertEquals(expectedResponseStatus, routerResponse.getStatus());
    }

    @Test
    public void testReturnsNotFoundResponseWhenRequestRouteIsNotInRouterDirectory() throws IOException {
        Path publicDirectory = Files.createTempDirectory("public");
        Router router = new FileSystemRouter(publicDirectory);
        Request request = new RequestBuilder().setURI("/unconfigured").build();

        Response response = router.routeRequest(request);

        assertEquals(Status.NOT_FOUND, response.getStatus());

        Files.delete(publicDirectory);
    }

    @Test
    public void testReturnsFileWhenGetRequestRouteIsInRouterDirectory() throws IOException {
        Path publicDirectory = Files.createTempDirectory("public");
        List<String> fileContents = Arrays.asList("Line1", "Line2");
        Path file = createTempFileWithContent(publicDirectory, "file", "tmp", fileContents);

        Router router = new FileSystemRouter(publicDirectory);
        Request request = new RequestBuilder()
            .setMethod(Method.GET)
            .setURI("/" + file.getFileName())
            .build();

        Response response = router.routeRequest(request);

        assertEquals("Line1\nLine2\n", new String(response.getBody()));

        deleteFiles(Arrays.asList(file, publicDirectory));
    }

    @Test
    public void testModifiesFileWhenGetRequestRouteIsInRouterDirectory() throws IOException {
        Path publicDirectory = Files.createTempDirectory("public");
        List<String> initialFileContents = Arrays.asList("Line1", "Line2");
        Path file = createTempFileWithContent(publicDirectory, "file", "tmp", initialFileContents);

        Router router = new FileSystemRouter(publicDirectory);
        String newFileContents = "new file contents\n";
        Request request = new RequestBuilder()
            .setMethod(Method.PUT)
            .setURI("/" + file.getFileName())
            .setBody(newFileContents.getBytes())
            .build();

        router.routeRequest(request);

        assertEquals(newFileContents, new String(Files.readAllBytes(file)));

        deleteFiles(Arrays.asList(file, publicDirectory));
    }

    private Path createTempFileWithContent(
        Path dir, String prefix, String suffix, Iterable<String> content) throws IOException {
        Path file = Files.createTempFile(dir, prefix, suffix);
        Files.write(file, content);

        return file;
    }

    private void deleteFiles(List<Path> paths) throws IOException {
        for (Path path : paths) {
            Files.delete(path);
        }
    }
}