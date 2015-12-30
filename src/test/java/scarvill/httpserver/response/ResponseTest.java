package scarvill.httpserver.response;

import org.junit.Test;

import static org.hamcrest.Matchers.equalTo;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThat;

public class ResponseTest {
    @Test
    public void testHasStatus() {
        Response response = new ResponseBuilder().setStatus(Status.OK).build();

        assertEquals(Status.OK, response.getStatus());
    }

    @Test
    public void testHasHeaders() {
        Response response = new ResponseBuilder()
            .setHeader("Foo", "a header")
            .setHeader("Bar", "another header")
            .build();

        assertEquals("a header", response.getHeaders().get("Foo"));
        assertEquals("another header", response.getHeaders().get("Bar"));
    }

    @Test
    public void testHasBody() {
        byte[] expectedBody = "body".getBytes();
        Response response = new ResponseBuilder().setBody(expectedBody).build();

        assertThat(expectedBody, equalTo(response.getBody()));
    }
}