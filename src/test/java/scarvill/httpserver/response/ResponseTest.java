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
        Response response = new ResponseBuilder().setHeaders(headers).build();

        for (String header : headers) {
            assertTrue(response.getHeaders().contains(header));
        }
    }

    @Test
    public void testHasBody() {
        Response response = new ResponseBuilder().setBody("body".getBytes()).build();

        assertArrayEquals("body".getBytes(), response.getBody());
    }
}