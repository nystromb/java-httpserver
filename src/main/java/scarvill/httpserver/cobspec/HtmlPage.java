package scarvill.httpserver.cobspec;

import java.util.Collection;
import java.util.List;

public class HtmlPage {

    public String indexPage(List<String> listItems) {
        return "<!DOCTYPE html>\n" +
            "<html lang=\"en\">\n" +
            "<head>\n" +
            "<meta charset=\"utf-8\">\n" +
            "<title>Index</title>\n" +
            "</head>\n" +
            "<body>\n" +
            "<ul>\n" +
            formatIndexLinks(listItems) +
            "</ul>\n" +
            "</body>\n" +
            "</html>\n";
    }

    private String formatIndexLinks(Collection<String> entries) {
        String listElements = "";

        for (String entry : entries) {
            listElements += "<li><a href=/" + entry + ">" + entry + "</a></li>\n";
        }

        return listElements;
    }
}
