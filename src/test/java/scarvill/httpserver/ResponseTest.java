package scarvill.httpserver;

import org.junit.Test;
import scarvill.httpserver.constants.Status;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

public class ResponseTest {
    @Test
    public void testHasStatus() {
        Response response = new Response.Builder().setStatus(Status.OK).build();

        assertEquals(Status.OK, response.getStatus());
    }

    @Test
    public void testHasHeaders() {
        String[] headers = new String[]{"Foo: a header", "Bar: another header"};
        Response response = new Response.Builder().setHeaders(headers).build();

        for (String header : headers) {
            assertTrue(response.getHeaders().contains(header));
        }
    }

    @Test
    public void testHasBody() {
        Response response = new Response.Builder().setBody("body").build();

        assertEquals("body", response.getBody());
    }
}