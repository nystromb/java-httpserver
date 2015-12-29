package scarvill.httpserver.routing;

import org.junit.Test;
import scarvill.httpserver.request.Method;
import scarvill.httpserver.request.RequestBuilder;
import scarvill.httpserver.response.Response;
import scarvill.httpserver.response.Status;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static scarvill.httpserver.request.Method.GET;
import static scarvill.httpserver.request.Method.OPTIONS;

public class RouteToDirectoryResourcesTest {

    @Test
    public void testReturnsFileInRootDirectory() throws IOException {
        String fileContents = "file contents";
        Path directory = createTempDirectory();
        Path file = createTempFileWithContent(directory, fileContents.getBytes());

        Response response = new RouteToDirectoryResources(directory).apply(
            new RequestBuilder()
                .setMethod(GET)
                .setURI("/" + file.getFileName())
                .build());

        assertEquals(fileContents, new String(response.getBody()));
    }

    @Test
    public void testReturnsFileInNestedDirectory() throws IOException {
        String fileContents = "file contents";
        Path directory = createTempDirectory();
        Path subdirectory = createTempSubdirectory(directory);
        Path file = createTempFileWithContent(subdirectory, fileContents.getBytes());

        Response response = new RouteToDirectoryResources(directory).apply(
            new RequestBuilder()
                .setMethod(GET)
                .setURI("/" + subdirectory.getFileName() + "/" + file.getFileName())
                .build());

        assertEquals(fileContents, new String(response.getBody()));
    }

    @Test
    public void testReturnsRootDirectoryContents() throws IOException {
        Path directory = createTempDirectory();
        Path file = createTempFileWithContent(directory, "".getBytes());

        Response response = new RouteToDirectoryResources(directory).apply(
            new RequestBuilder()
                .setMethod(GET)
                .setURI("/")
                .build());

        assertTrue(new String(response.getBody()).contains(file.getFileName().toString()));
    }

    @Test
    public void testReturnsNestedDirectoryContents() throws IOException {
        Path directory = createTempDirectory();
        Path subdirectory = createTempSubdirectory(directory);
        Path file = createTempFileWithContent(subdirectory, "".getBytes());

        Response response = new RouteToDirectoryResources(directory).apply(
            new RequestBuilder()
                .setMethod(GET)
                .setURI("/" + subdirectory.getFileName())
                .build());

        assertTrue(new String(response.getBody()).contains(file.getFileName().toString()));
    }

    @Test
    public void testRoutesOptionsRequestsForExtantResources() throws IOException {
        Path directory = createTempDirectory();
        Path file = createTempFileWithContent(directory, "".getBytes());

        Response response = new RouteToDirectoryResources(directory).apply(
            new RequestBuilder()
                .setMethod(OPTIONS)
                .setURI("/" + file.getFileName())
                .build());

        assertEquals(Status.OK, response.getStatus());
        assertTrue(response.getHeaders().get("Allow").contains(Method.GET.toString()));
        assertTrue(response.getHeaders().get("Allow").contains(Method.OPTIONS.toString()));
    }

    @Test
    public void testReturnsMethodNotAllowedForNonGetOrOptionsRequests() throws IOException {
        Path directory = createTempDirectory();
        Path file = createTempFileWithContent(directory, "".getBytes());

        Response response = new RouteToDirectoryResources(directory).apply(
            new RequestBuilder()
                .setMethod(Method.POST)
                .setURI("/" + file.getFileName())
                .build());

        assertEquals(Status.METHOD_NOT_ALLOWED, response.getStatus());
    }

    @Test
    public void testReturns404NotFoundWhenNoRootDirectoryIsSet() {
        Response response = new RouteToDirectoryResources().apply(new RequestBuilder().build());

        assertEquals(Status.NOT_FOUND, response.getStatus());
    }

    private Path createTempDirectory() throws IOException {
        Path directory = Files.createTempDirectory("");
        directory.toFile().deleteOnExit();

        return directory;
    }

    private Path createTempSubdirectory(Path rootDirectory) throws IOException {
        Path subdirectory = Files.createTempDirectory(rootDirectory, "");
        subdirectory.toFile().deleteOnExit();

        return subdirectory;
    }

    private Path createTempFileWithContent(Path dir, byte[] content) throws IOException {
        File file = File.createTempFile("temp", "", dir.toFile());
        Files.write(file.toPath(), content);
        file.deleteOnExit();

        return file.toPath();
    }
}