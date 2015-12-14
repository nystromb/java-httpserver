package scarvill.httpserver.request;

import org.junit.Test;

import java.util.HashMap;

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
    public void testTranslatesSpecialCharacterCodesInQueryString() {
        String query = "message=%3C%2C%20%3E%2C%20%3D%2C%20!%3D%3B%20%2B%2C%20-%2C%20*%2C%20%26" +
            "%2C%20%40%2C%20%23%2C%20%24%2C%20%5B%2C%20%5D%3A%20%22is%20that%20all%22%3F";
        String translatedMessage =
            "<, >, =, !=; +, -, *, &, @, #, $, [, ]: \"is that all\"?";

        Request request = new HTTPRequest("GET /uri?" + query + " HTTP/1.1\r\n\r\n").parse();

        assertEquals(translatedMessage, request.getParameters().get("message"));
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