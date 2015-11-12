package scarvill.httpserver;

import org.junit.Test;

import static org.junit.Assert.*;

public class RequestTest {
    @Test
    public void testParsesAction() {
        Request request = new Request("GET / HTTP/1.1\r\n");

        assertEquals("GET", request.getMethod());
    }

    @Test
    public void testParsesRoute() {
        Request request = new Request("GET /foo/bar HTTP/1.1\r\n");

        assertEquals("/foo/bar", request.getURI());
    }
}