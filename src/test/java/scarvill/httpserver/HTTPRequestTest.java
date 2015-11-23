package scarvill.httpserver;

import org.junit.Test;

import static org.junit.Assert.*;
import static scarvill.httpserver.constants.MethodTwo.*;

public class HTTPRequestTest {
    @Test
    public void testParsesRawHTTPRequest() {
        RequestTwo request = new HTTPRequest("GET /uri HTTP/1.1").parse();

        assertEquals(GET, request.getMethod());
    }

    @Test
    public void testParsesInvalidRawHTTPRequestMethod() {
        RequestTwo request = new HTTPRequest("FOO /uri HTTP/1.1").parse();

        assertEquals(NULL_METHOD, request.getMethod());
    }

    @Test
    public void testParsesRawHTTPRequestURI() {
        RequestTwo request = new HTTPRequest("GET /uri HTTP/1.1").parse();

        assertEquals("/uri", request.getURI());
    }

    @Test
    public void testParsesRawHTTPRequestWithNoBody() {
        RequestTwo request = new HTTPRequest("GET /uri HTTP/1.1").parse();

        assertEquals("", request.getBody());
    }

    @Test
    public void testParsesRawHTTPRequestWithBody() {
        RequestTwo request = new HTTPRequest("GET /uri HTTP/1.1\r\n\r\nbody").parse();

        assertEquals("body", request.getBody());
    }
}