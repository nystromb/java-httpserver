package scarvill.httpserver.routing;

import org.junit.Test;
import scarvill.httpserver.request.RequestBuilder;
import scarvill.httpserver.response.Response;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

import static org.hamcrest.Matchers.containsString;
import static org.junit.Assert.assertThat;
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

        assertThat(new String(response.getBody()), containsString(file1.getFileName().toString()));
        assertThat(new String(response.getBody()), containsString(file2.getFileName().toString()));
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

        assertThat(new String(response.getBody()),
            containsString("/" + subdirectory.getFileName() + "/" + file1.getFileName()));
        assertThat(new String(response.getBody()),
            containsString("/" + subdirectory.getFileName() + "/" + file2.getFileName()));
    }

    @Test(expected = RuntimeException.class)
    public void testThrowsRuntimeExceptionWhenCannotGetDirectoryContents() throws IOException {
        Path directory = createTempDirectory();
        GetDirectoryIndex getDirectoryIndex = new GetDirectoryIndex(directory);
        directory.toFile().delete();

        getDirectoryIndex.apply(
            new RequestBuilder()
            .setMethod(GET)
            .setURI("/" + directory.getFileName())
            .build());
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