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
    public void testReturnsFilesInRouterDirectory() throws IOException {
        String fileContents = "file contents";
        Path directory = Files.createTempDirectory("dir");
        Path file = createTempFileWithContent(directory, fileContents.getBytes());

        FileSystemRouter router = new FileSystemRouter(directory);
        Request request = new RequestBuilder()
            .setMethod(Method.GET)
            .setURI("/" + file.getFileName())
            .build();

        Response response = router.routeRequest(request);

        assertEquals(fileContents, new String(response.getBody()));

        deleteFiles(Arrays.asList(file, directory));
    }

    @Test
    public void testRoutesOptionsRequestsForExtantResources() throws IOException {
        Path directory = Files.createTempDirectory("dir");
        Path file = createTempFileWithContent(directory, "".getBytes());

        FileSystemRouter router = new FileSystemRouter(directory);
        Request request = new RequestBuilder()
            .setMethod(Method.OPTIONS)
            .setURI("/" + file.getFileName())
            .build();

        Response response = router.routeRequest(request);

        assertEquals(Status.OK, response.getStatus());
        assertTrue(response.getHeaders().get("Allow").contains(Method.GET.toString()));
        assertTrue(response.getHeaders().get("Allow").contains(Method.OPTIONS.toString()));

        deleteFiles(Arrays.asList(file, directory));
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