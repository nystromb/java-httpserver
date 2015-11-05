package scarvill.httpserver;

import org.junit.Test;

import static org.junit.Assert.*;

public class HTTPRequestTest {
    @Test
    public void testParsesAction() {
        HTTPRequest request = new HTTPRequest("GET / HTTP/1.1\r\n");

        assertEquals("GET", request.getAction());
    }

    @Test
    public void testParsesRoute() {
        HTTPRequest request = new HTTPRequest("GET /foo/bar HTTP/1.1\r\n");

        assertEquals("/foo/bar", request.getURI());
    }
}