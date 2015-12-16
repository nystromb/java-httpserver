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
        Path subdirectory = Files.createTempDirectory(directory, "subdirectory");
        Path file = createTempFileWithContent(subdirectory, "".getBytes());

        GetDirectoryIndex getDirectoryIndex = new GetDirectoryIndex(directory);
        Request request = new RequestBuilder()
            .setMethod(Method.GET)
            .setURI("/" + subdirectory.getFileName())
            .build();

        Response response = getDirectoryIndex.apply(request);

        assertTrue(new String(response.getBody()).contains(file.getFileName().toString()));

        deleteFiles(Arrays.asList(file, subdirectory, directory));
    }

    @Test
    public void testResponseContainsWellFormattedHtml() throws IOException {
        Path directory = Files.createTempDirectory("directory");
        GetDirectoryIndex getDirectoryIndex = new GetDirectoryIndex(directory);
        Request request = new RequestBuilder()
            .setMethod(Method.GET)
            .setURI("/")
            .build();
        String htmlTemplate = "<!DOCTYPE html>\n" +
            "<html lang=\"en\">\n" +
            "<head>\n" +
            "<meta charset=\"utf-8\">\n" +
            "<title>Index</title>\n" +
            "</head>\n" +
            "<body>\n" +
            "<ul>\n" +
            "</ul>\n" +
            "</body>\n" +
            "</html>\n";

        Response response = getDirectoryIndex.apply(request);

        assertEquals(htmlTemplate, new String(response.getBody()));

        deleteFiles(Arrays.asList(directory));
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