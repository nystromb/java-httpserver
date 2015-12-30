package scarvill.httpserver.request;

import org.junit.Test;

import static org.hamcrest.Matchers.equalTo;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThat;

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
        Request request = new RequestBuilder()
            .setParameter("foo", "bar")
            .setParameter("bar", "baz")
            .build();

        assertEquals("bar", request.getParameters().get("foo"));
        assertEquals("baz", request.getParameters().get("bar"));
    }

    @Test
    public void testHasHeaders() {
        Request request = new RequestBuilder()
            .setHeader("Header:", "a header")
            .setHeader("Other:", "another header")
            .build();

        assertEquals("a header", request.getHeaders().get("Header:"));
        assertEquals("another header", request.getHeaders().get("Other:"));
    }

    @Test
    public void testHasBody() throws Exception {
        byte[] expectedBody = "body".getBytes();
        Request request = new RequestBuilder().setBody(expectedBody).build();

        assertThat(expectedBody, equalTo(request.getBody()));
    }
}