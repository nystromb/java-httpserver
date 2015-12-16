package scarvill.httpserver.routing;

import scarvill.httpserver.request.Request;
import scarvill.httpserver.response.Response;
import scarvill.httpserver.response.ResponseBuilder;
import scarvill.httpserver.response.Status;

import java.io.IOException;
import java.nio.file.DirectoryStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.function.Function;

public class GetDirectoryIndex implements Function<Request, Response> {
    private Path rootDirectory;

    public GetDirectoryIndex(Path rootDirectory) {
        this.rootDirectory = rootDirectory;
    }

    @Override
    public Response apply(Request request) {
        String indexPage = htmlIndexPage(request.getURI());

        return new ResponseBuilder()
            .setStatus(Status.OK)
            .setHeader("Content-Length", String.valueOf(indexPage.length()))
            .setBody(indexPage.getBytes())
            .build();
    }

    private String htmlIndexPage(String uri) {
        return "<!DOCTYPE html>\n" +
            "<html lang=\"en\">\n" +
            "<head>\n" +
            "<meta charset=\"utf-8\">\n" +
            "<title>Index</title>\n" +
            "</head>\n" +
            "<body>\n" +
            "<ul>\n" +
            formatHtmlListEntries(directoryFileNames(uri)) +
            "</ul>\n" +
            "</body>\n" +
            "</html>\n";
    }

    private String formatHtmlListEntries(Collection<String> entries) {
        String listElements = "";

        for (String entry : entries) {
            listElements += "<li><a href=/" + entry + ">" + entry + "</a></li>\n";
        }

        return listElements;
    }

    private List<String> directoryFileNames(String uri) {
        List<String> contents = new ArrayList<>();
        try {
            DirectoryStream<Path> stream = Files.newDirectoryStream(Paths.get(rootDirectory + uri));
            for (Path entry : stream) {
                contents.add(entry.getFileName().toString());
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        return contents;
    }
}
