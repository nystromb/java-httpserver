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
    public void testGeneratesAHTTPResponse() throws Exception {
        String[] headers = {"Foo: a random header\r\n", "Bar: another header\r\n"};
        Response response = new Response(Status.OK, headers);

        assertTrue(response.generate().contains(Status.OK));
        assertTrue(response.generate().contains("Foo: a random header\r\n"));
        assertTrue(response.generate().contains("Bar: another header\r\n"));
    }

    @Test
    public void testGeneratedResponseAlwaysIncludesConnectionCloseHeader() throws Exception {
        Response response = new Response(Status.OK);

        assertTrue(response.generate().contains("Connection: close\r\n"));
    }
}