package scarvill.httpserver.request;

import org.junit.Test;
import scarvill.httpserver.request.HTTPRequest;
import scarvill.httpserver.request.Request;

import static org.junit.Assert.assertEquals;
import static scarvill.httpserver.request.Method.GET;
import static scarvill.httpserver.request.Method.NULL_METHOD;

public class HTTPRequestTest {
    @Test
    public void testParsesRawHTTPRequest() {
        Request request = new HTTPRequest("GET /uri HTTP/1.1").parse();

        assertEquals(GET, request.getMethod());
    }

    @Test
    public void testParsesInvalidRawHTTPRequestMethod() {
        Request request = new HTTPRequest("FOO /uri HTTP/1.1").parse();

        assertEquals(NULL_METHOD, request.getMethod());
    }

    @Test
    public void testParsesRawHTTPRequestURI() {
        Request request = new HTTPRequest("GET /uri HTTP/1.1").parse();

        assertEquals("/uri", request.getURI());
    }

    @Test
    public void testParsesRawHTTPRequestWithQueryStringParameters() {
        Request request = new HTTPRequest("GET /uri?foo=bar&bar=baz HTTP/1.1").parse();

        assertEquals("/uri", request.getURI());
        assertEquals("bar", request.getParameters().get("foo"));
        assertEquals("baz", request.getParameters().get("bar"));
    }

    @Test
    public void testParsesRawHTTPRequestWithNoBody() {
        Request request = new HTTPRequest("GET /uri HTTP/1.1").parse();

        assertEquals("", request.getBody());
    }

    @Test
    public void testParsesRawHTTPRequestWithBody() {
        Request request = new HTTPRequest("GET /uri HTTP/1.1\r\n\r\nbody").parse();

        assertEquals("body", request.getBody());
    }
}