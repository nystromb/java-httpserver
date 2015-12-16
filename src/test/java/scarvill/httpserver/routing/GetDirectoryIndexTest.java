package scarvill.httpserver.routing;

import org.junit.Test;
import scarvill.httpserver.request.Method;
import scarvill.httpserver.request.Request;
import scarvill.httpserver.request.RequestBuilder;
import scarvill.httpserver.response.Response;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Arrays;
import java.util.List;

import static org.junit.Assert.*;

public class GetDirectoryIndexTest {
    @Test
    public void testResponseContainsDirectoryContents() throws IOException {
        Path directory = Files.createTempDirectory("directory");
        Path file1 = createTempFileWithContent(directory, "".getBytes());
        Path file2 = createTempFileWithContent(directory, "".getBytes());

        GetDirectoryIndex getDirectoryIndex = new GetDirectoryIndex(directory);
        Request request = new RequestBuilder()
            .setMethod(Method.GET)
            .setURI("/")
            .build();

        Response response = getDirectoryIndex.apply(request);

        assertTrue(new String(response.getBody()).contains(file1.getFileName().toString()));
        assertTrue(new String(response.getBody()).contains(file2.getFileName().toString()));

        deleteFiles(Arrays.asList(file1, file2, directory));
    }

    @Test
    public void testResponseContainsCorrectPathsToNestedDirectoryContents() throws IOException {
        Path directory = Files.createTempDirectory("directory");
        Path subdirectory = Files.createTempDirectory(directory, "subdirectory");
        Path file1 = createTempFileWithContent(subdirectory, "".getBytes());
        Path file2 = createTempFileWithContent(subdirectory, "".getBytes());

        GetDirectoryIndex getDirectoryIndex = new GetDirectoryIndex(directory);
        Request request = new RequestBuilder()
            .setMethod(Method.GET)
            .setURI("/" + subdirectory.getFileName())
            .build();

        Response response = getDirectoryIndex.apply(request);

        assertTrue(new String(response.getBody())
            .contains("/" + subdirectory.getFileName() + "/" + file1.getFileName()));
        assertTrue(new String(response.getBody())
            .contains("/" + subdirectory.getFileName() + "/" + file2.getFileName()));

        deleteFiles(Arrays.asList(file1, file2, subdirectory, directory));
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