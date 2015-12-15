package scarvill.httpserver.routing;

import org.junit.Test;
import scarvill.httpserver.request.Method;
import scarvill.httpserver.request.Request;
import scarvill.httpserver.request.RequestBuilder;
import scarvill.httpserver.response.Response;
import scarvill.httpserver.response.ResponseBuilder;
import scarvill.httpserver.response.Status;
import scarvill.httpserver.routing.route_strategies.GiveStaticResponse;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.List;

import static org.junit.Assert.*;

public class FileSystemRouterTest {
    @Test
    public void testDelegatesRoutingToVirtualRouterWhenRequestRouteIsNotInRouterDirectory() {
        Router router = new FileSystemRouter(Paths.get("/noFooBarFileHere"));
        Request request = new RequestBuilder().setMethod(Method.GET).setURI("/foo.bar").build();
        Response response = new ResponseBuilder().setStatus(Status.OK).build();
        router.addRoute("/foo.bar", Method.GET, new GiveStaticResponse(response));

        Response routerResponse = router.routeRequest(request);

        assertEquals(Status.OK, routerResponse.getStatus());
    }

    @Test
    public void testReturnsFilesInRouterDirectory() throws IOException {
        Path publicDirectory = Files.createTempDirectory("public");
        String fileContents = "file contents";
        Path file = createTempFileWithContent(publicDirectory, "file", "tmp", fileContents.getBytes());

        Router router = new FileSystemRouter(publicDirectory);
        Request request = new RequestBuilder()
            .setMethod(Method.GET)
            .setURI("/" + file.getFileName())
            .build();

        Response response = router.routeRequest(request);

        assertEquals(fileContents, new String(response.getBody()));

        deleteFiles(Arrays.asList(file, publicDirectory));
    }

    @Test
    public void testModifiesFilesInRouterDirectory() throws IOException {
        Path publicDirectory = Files.createTempDirectory("public");
        byte[] initialFileContents = "foo".getBytes();
        Path file = createTempFileWithContent(publicDirectory, "file", "tmp", initialFileContents);

        Router router = new FileSystemRouter(publicDirectory);
        String newFileContents = "new file contents";
        Request request = new RequestBuilder()
            .setMethod(Method.PUT)
            .setURI("/" + file.getFileName())
            .setBody(newFileContents.getBytes())
            .build();

        router.routeRequest(request);

        assertEquals(newFileContents, new String(Files.readAllBytes(file)));

        deleteFiles(Arrays.asList(file, publicDirectory));
    }

    @Test
    public void testPatchesFilesInRouterDirectory() throws IOException {
        Path publicDirectory = Files.createTempDirectory("public");
        byte[] initialFileContents = "foo".getBytes();
        Path file = createTempFileWithContent(publicDirectory, "file", "tmp", initialFileContents);

        Router router = new FileSystemRouter(publicDirectory);
        String newFileContents = "new file contents";
        Request request = new RequestBuilder()
            .setMethod(Method.PATCH)
            .setURI("/" + file.getFileName())
            .setBody(newFileContents.getBytes())
            .build();

        Response response = router.routeRequest(request);

        assertEquals(Status.NO_CONTENT, response.getStatus());
        assertEquals(newFileContents, new String(Files.readAllBytes(file)));

        deleteFiles(Arrays.asList(file, publicDirectory));
    }

    @Test
    public void testRoutesOptionsRequestsForExtantResources() throws IOException {
        Path publicDirectory = Files.createTempDirectory("public");
        Path file = createTempFileWithContent(publicDirectory, "file", "tmp", "".getBytes());

        Router router = new FileSystemRouter(publicDirectory);
        Request request = new RequestBuilder()
            .setMethod(Method.OPTIONS)
            .setURI("/" + file.getFileName())
            .build();

        Response response = router.routeRequest(request);

        assertEquals(Status.OK, response.getStatus());
        assertTrue(response.getHeaders().get("Allow").contains(Method.GET.toString()));
        assertTrue(response.getHeaders().get("Allow").contains(Method.PUT.toString()));
        assertTrue(response.getHeaders().get("Allow").contains(Method.POST.toString()));
        assertTrue(response.getHeaders().get("Allow").contains(Method.DELETE.toString()));
        assertTrue(response.getHeaders().get("Allow").contains(Method.OPTIONS.toString()));

        deleteFiles(Arrays.asList(file, publicDirectory));
    }

    private Path createTempFileWithContent(
        Path dir, String prefix, String suffix, byte[] content) throws IOException {
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