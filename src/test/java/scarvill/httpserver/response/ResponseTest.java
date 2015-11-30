package scarvill.httpserver.response;

import org.junit.Test;
import scarvill.httpserver.response.Response;
import scarvill.httpserver.response.ResponseBuilder;
import scarvill.httpserver.response.Status;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

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
        Response response = new ResponseBuilder().setBody("body").build();

        assertEquals("body", response.getBody());
    }
}