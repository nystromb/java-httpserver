package scarvill.httpserver;

import org.junit.Test;

import static org.junit.Assert.*;

public class RequestTest {
    @Test
    public void testParsesAction() {
        String rawRequest = RequestBuilder.build(Method.GET, "/");
        Request request = new Request(rawRequest);

        assertEquals(Method.GET, request.getMethod());
    }

    @Test
    public void testParsesRoute() {
        String rawRequest = RequestBuilder.build(Method.GET, "/foo/bar");
        Request request = new Request(rawRequest);

        assertEquals("/foo/bar", request.getURI());
    }
}