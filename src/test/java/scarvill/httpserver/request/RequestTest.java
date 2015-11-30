package scarvill.httpserver.request;

import org.junit.Test;
import scarvill.httpserver.request.Method;
import scarvill.httpserver.request.Request;
import scarvill.httpserver.request.RequestBuilder;

import java.util.HashMap;

import static org.junit.Assert.assertEquals;

public class RequestTest {
    @Test
    public void testHasMethod() {
        Request request = new RequestBuilder().setMethod(Method.GET).build();

        assertEquals(Method.GET, request.getMethod());
    }

    @Test
    public void testHasURI() {
        Request request = new RequestBuilder().setURI("/foo/bar").build();

        assertEquals("/foo/bar", request.getURI());
    }

    @Test
    public void testHasQueryStringParameters() {
        HashMap<String, String> parameters = new HashMap<>();
        parameters.put("foo", "bar");
        parameters.put("bar", "baz");

        Request request = new RequestBuilder().setParameters(parameters).build();

        assertEquals("bar", request.getParameters().get("foo"));
        assertEquals("baz", request.getParameters().get("bar"));
    }

    @Test
    public void testHasBody() throws Exception {
        Request request = new RequestBuilder().setBody("body").build();

        assertEquals("body", request.getBody());
    }
}