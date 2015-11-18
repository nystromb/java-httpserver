package scarvill.httpserver;

import org.junit.Test;
import scarvill.httpserver.constants.Method;

import static org.junit.Assert.*;

public class RequestTest {
    @Test
    public void testParsesAction() {
        String rawRequest = RequestUtility.rawRequest("METHOD", "/foo/bar");
        Request request = new Request(rawRequest);

        assertEquals("METHOD", request.getMethod());
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