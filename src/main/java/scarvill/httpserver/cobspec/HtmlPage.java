package scarvill.httpserver.cobspec;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

public class HtmlPage {

    public String indexPage(HashMap<String, String> directoryFileNamesAndPaths) {
        return "<!DOCTYPE html>\n" +
            "<html lang=\"en\">\n" +
            "<head>\n" +
            "<meta charset=\"utf-8\">\n" +
            "<title>Index</title>\n" +
            "</head>\n" +
            "<body>\n" +
            "<ul>\n" +
            formatIndexLinks(directoryFileNamesAndPaths) +
            "</ul>\n" +
            "</body>\n" +
            "</html>\n";
    }

    private String formatIndexLinks(HashMap<String, String> fileNamesAndPaths) {
        String listElements = "";

        for (Map.Entry<String, String> entry : fileNamesAndPaths.entrySet()) {
            listElements += "<li><a href=" + entry.getValue() + ">" + entry.getKey() + "</a></li>\n";
        }

        return listElements;
    }
}
