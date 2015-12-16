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