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
import java.util.Arrays;
import java.util.List;

import static org.junit.Assert.*;

public class FileSystemRouterTest {

    @Test
    public void testDelegatesToConfigurableRouterIfRouteHasBeenManuallyConfigured() throws IOException {
        Path publicDirectory = Files.createTempDirectory("public");
        Path file = createTempFileWithContent(publicDirectory, "should NOT get this".getBytes());
        String fileRoute = "/" + file.getFileName();

        String expectedContent = "should get this";
        Response response = new ResponseBuilder()
            .setStatus(Status.OK)
            .setBody(expectedContent.getBytes())
            .build();
        Router router = new FileSystemRouter(publicDirectory);
        router.addRoute(fileRoute, Method.GET, new GiveStaticResponse(response));
        Request request = new RequestBuilder()
            .setMethod(Method.GET)
            .setURI(fileRoute)
            .build();

        Response routerResponse = router.routeRequest(request);

        assertEquals(expectedContent, new String(routerResponse.getBody()));
    }

    @Test
    public void testReturnsFilesInRouterDirectory() throws IOException {
        String fileContents = "file contents";
        Path publicDirectory = Files.createTempDirectory("public");
        Path file = createTempFileWithContent(publicDirectory, fileContents.getBytes());

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
    public void testRoutesOptionsRequestsForExtantResources() throws IOException {
        Path publicDirectory = Files.createTempDirectory("public");
        Path file = createTempFileWithContent(publicDirectory, "".getBytes());

        Router router = new FileSystemRouter(publicDirectory);
        Request request = new RequestBuilder()
            .setMethod(Method.OPTIONS)
            .setURI("/" + file.getFileName())
            .build();

        Response response = router.routeRequest(request);

        assertEquals(Status.OK, response.getStatus());
        assertTrue(response.getHeaders().get("Allow").contains(Method.GET.toString()));
        assertTrue(response.getHeaders().get("Allow").contains(Method.OPTIONS.toString()));

        deleteFiles(Arrays.asList(file, publicDirectory));
    }

    private Path createTempFileWithContent(Path dir, byte[] content) throws IOException {
        Path file = Files.createTempFile(dir, "temp", "");
        Files.write(file, content);

        return file;
    }

    private void deleteFiles(List<Path> paths) throws IOException {
        for (Path path : paths) {
            Files.delete(path);
        }
    }
}