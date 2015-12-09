package scarvill.httpserver.request;

import org.junit.Test;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;
import static scarvill.httpserver.request.Method.GET;
import static scarvill.httpserver.request.Method.NULL_METHOD;

public class HTTPRequestTest {
    @Test
    public void testParsesRawHTTPRequest() {
        Request request = new HTTPRequest("GET /uri HTTP/1.1\r\n\r\n").parse();

        assertEquals(GET, request.getMethod());
    }

    @Test
    public void testParsesInvalidRawHTTPRequestMethod() {
        Request request = new HTTPRequest("FOO /uri HTTP/1.1\r\n\r\n").parse();

        assertEquals(NULL_METHOD, request.getMethod());
    }

    @Test
    public void testParsesRawHTTPRequestURI() {
        Request request = new HTTPRequest("GET /uri HTTP/1.1\r\n\r\n").parse();

        assertEquals("/uri", request.getURI());
    }

    @Test
    public void testParsesRawHTTPRequestWithQueryStringParameters() {
        Request request = new HTTPRequest("GET /uri?foo=bar&bar=baz HTTP/1.1\r\n\r\n").parse();

        assertEquals("/uri", request.getURI());
        assertEquals("bar", request.getParameters().get("foo"));
        assertEquals("baz", request.getParameters().get("bar"));
    }

    @Test
    public void testParsesRawHTTPRequestHeaders() {
        String rawRequest = "GET /uri HTTP/1.1\r\n" +
            "Header: a header\r\n" +
            "Other: other header\r\n" +
            "\r\n";
        Request request = new HTTPRequest(rawRequest).parse();

        assertEquals("a header", request.getHeaders().get("Header"));
        assertEquals("other header", request.getHeaders().get("Other"));
    }

    @Test
    public void testParsesRawHTTPRequestWithNoBody() {
        Request request = new HTTPRequest("GET /uri HTTP/1.1\r\n\r\n").parse();

        assertArrayEquals(new byte[]{}, request.getBody());
    }

    @Test
    public void testParsesRawHTTPRequestWithBody() {
        Request request = new HTTPRequest("GET /uri HTTP/1.1\r\n\r\n", "body".getBytes()).parse();

        assertArrayEquals("body".getBytes(), request.getBody());
    }
}