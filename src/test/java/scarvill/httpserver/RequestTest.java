package scarvill.httpserver;

import org.junit.Test;
import scarvill.httpserver.constants.Method;

import static org.junit.Assert.*;

public class RequestTest {
    @Test
    public void testParsesAction() {
        String rawRequest = RequestUtility.rawRequest(Method.GET, "/");
        Request request = new Request(rawRequest);

        assertEquals(Method.GET, request.getMethod());
    }

    @Test
    public void testParsesRoute() {
        String rawRequest = RequestUtility.rawRequest(Method.GET, "/foo/bar");
        Request request = new Request(rawRequest);

        assertEquals("/foo/bar", request.getURI());
    }
}