package scarvill.httpserver.html;

import org.junit.Test;
import scarvill.httpserver.html.HtmlPage;

import java.util.HashMap;

import static org.junit.Assert.assertEquals;

public class HtmlPageTest {

    @Test
    public void testGeneratesIndexPage() {
        HashMap<String, String> directoryFileNamesAndPaths = new HashMap<>();
        directoryFileNamesAndPaths.put("one", "/foo/one");
        directoryFileNamesAndPaths.put("two", "/foo/two");
        directoryFileNamesAndPaths.put("three", "/foo/three");

        String expectedOutput = "<!DOCTYPE html>\n" +
            "<html lang=\"en\">\n" +
            "<head>\n" +
            "<meta charset=\"utf-8\">\n" +
            "<title>Index</title>\n" +
            "</head>\n" +
            "<body>\n" +
            "<ul>\n" +
            "<li><a href=/foo/one>one</a></li>\n" +
            "<li><a href=/foo/two>two</a></li>\n" +
            "<li><a href=/foo/three>three</a></li>\n" +
            "</ul>\n" +
            "</body>\n" +
            "</html>\n";

        assertEquals(expectedOutput, new HtmlPage().indexPage(directoryFileNamesAndPaths));
    }
}