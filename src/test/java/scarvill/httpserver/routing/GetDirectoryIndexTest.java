package scarvill.httpserver.routing;

import org.junit.Test;
import scarvill.httpserver.request.RequestBuilder;
import scarvill.httpserver.response.Response;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

import static org.junit.Assert.assertTrue;
import static scarvill.httpserver.request.Method.GET;

public class GetDirectoryIndexTest {
    @Test
    public void testResponseContainsDirectoryContents() throws IOException {
        Path directory = Files.createTempDirectory("directory");
        Path file1 = createTempFile(directory);
        Path file2 = createTempFile(directory);

        Response response = new GetDirectoryIndex(directory).apply(
            new RequestBuilder()
                .setMethod(GET)
                .setURI("/")
                .build());

        assertTrue(new String(response.getBody()).contains(file1.getFileName().toString()));
        assertTrue(new String(response.getBody()).contains(file2.getFileName().toString()));
    }

    @Test
    public void testResponseContainsCorrectPathsToNestedDirectoryContents() throws IOException {
        Path directory = createTempDirectory();
        Path subdirectory = createTempSubdirectory(directory);
        Path file1 = createTempFile(subdirectory);
        Path file2 = createTempFile(subdirectory);

        Response response = new GetDirectoryIndex(directory).apply(
            new RequestBuilder()
                .setMethod(GET)
                .setURI("/" + subdirectory.getFileName())
                .build());

        assertTrue(new String(response.getBody())
            .contains("/" + subdirectory.getFileName() + "/" + file1.getFileName()));
        assertTrue(new String(response.getBody())
            .contains("/" + subdirectory.getFileName() + "/" + file2.getFileName()));
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

    private Path createTempFile(Path dir) throws IOException {
        File file = File.createTempFile("temp", "", dir.toFile());
        file.deleteOnExit();

        return file.toPath();
    }
}