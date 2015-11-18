package scarvill.httpserver;

import org.junit.Test;
import scarvill.httpserver.constants.Status;

import static org.junit.Assert.*;

public class ResponseTest {
    @Test
    public void testSetStatusLine() throws Exception {
        Response response = new Response(Status.OK);

        assertEquals("HTTP/1.1 200 OK\r\n", response.getStatusLine());
    }

    @Test
    public void testGeneratesAWellFormedHTTPResponse() throws Exception {
        String[] headers = {"Foo: a random header\r\n", "Bar: another header\r\n"};
        Response response = new Response(Status.OK, headers, "this is the response body");
        String expectedRawResponse = "HTTP/1.1 200 OK\r\n" +
            "Connection: close\r\n" +
            "Foo: a random header\r\n" +
            "Bar: another header\r\n" +
            "\r\n" +
            "this is the response body";

        assertEquals(expectedRawResponse, response.generate());
    }

    @Test
    public void testGeneratedResponseAlwaysIncludesConnectionCloseHeader() throws Exception {
        Response response = new Response(Status.OK);

        assertTrue(response.generate().contains("Connection: close\r\n"));
    }
}