package scarvill.httpserver.cobspec;

import org.junit.Test;

import java.util.Arrays;
import java.util.List;

import static org.junit.Assert.*;

public class HtmlPageTest {

    @Test
    public void testGeneratesIndexPage() {
        List<String> listItems = Arrays.asList("one", "two", "three");
        String expectedOutput = "<!DOCTYPE html>\n" +
            "<html lang=\"en\">\n" +
            "<head>\n" +
            "<meta charset=\"utf-8\">\n" +
            "<title>Index</title>\n" +
            "</head>\n" +
            "<body>\n" +
            "<ul>\n" +
            "<li><a href=/one>one</a></li>\n" +
            "<li><a href=/two>two</a></li>\n" +
            "<li><a href=/three>three</a></li>\n" +
            "</ul>\n" +
            "</body>\n" +
            "</html>\n";

        assertEquals(expectedOutput, new HtmlPage().indexPage(listItems));
    }
}