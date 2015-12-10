package scarvill.httpserver.response;

import org.junit.Test;

import static org.junit.Assert.*;

public class ResponseTest {
    @Test
    public void testHasStatus() {
        Response response = new ResponseBuilder().setStatus(Status.OK).build();

        assertEquals(Status.OK, response.getStatus());
    }

    @Test
    public void testHasHeaders() {
        String[] headers = new String[]{"Foo: a header", "Bar: another header"};
        Response response = new ResponseBuilder()
            .setHeader("Foo", "a header")
            .setHeader("Bar", "another header")
            .build();

        assertEquals("a header", response.getHeaders().get("Foo"));
        assertEquals("another header", response.getHeaders().get("Bar"));
    }

    @Test
    public void testHasBody() {
        Response response = new ResponseBuilder().setBody("body".getBytes()).build();

        assertArrayEquals("body".getBytes(), response.getBody());
    }
}