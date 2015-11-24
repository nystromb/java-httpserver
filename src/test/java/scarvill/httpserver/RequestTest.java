package scarvill.httpserver;

import org.junit.Test;
import scarvill.httpserver.constants.Method;

import static org.junit.Assert.*;

public class RequestTest {
    @Test
    public void testHasMethod() {
        Request request = new Request.Builder().setMethod(Method.GET).build();

        assertEquals(Method.GET, request.getMethod());
    }

    @Test
    public void testHasURI() {
        Request request = new Request.Builder().setURI("/foo/bar").build();

        assertEquals("/foo/bar", request.getURI());
    }

    @Test
    public void testHasBody() throws Exception {
        Request request = new Request.Builder().setBody("body").build();

        assertEquals("body", request.getBody());
    }
}