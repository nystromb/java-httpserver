package scarvill.httpserver;

import org.junit.Test;
import scarvill.httpserver.constants.Method;

import static org.junit.Assert.*;

public class RequestTest {
    @Test
    public void testParsesGetMethod() {
        String rawRequest = RequestUtility.rawRequest("GET", "/foo/bar");
        Request request = new Request(rawRequest);

        assertEquals(Method.GET, request.getMethod());
    }

    @Test
    public void testParsesPostMethod() {
        String rawRequest = RequestUtility.rawRequest("POST", "/foo/bar");
        Request request = new Request(rawRequest);

        assertEquals(Method.POST, request.getMethod());
    }

    @Test
    public void testParsesRoute() {
        String rawRequest = RequestUtility.rawRequest("METHOD", "/foo/bar");
        Request request = new Request(rawRequest);

        assertEquals("/foo/bar", request.getURI());
    }

    @Test
    public void testParsesBody() throws Exception {
        String rawRequest = RequestUtility.rawRequest("METHOD", "/foo/bar", "body");
        Request request = new Request(rawRequest);

        assertEquals("body", request.getBody());
    }
}