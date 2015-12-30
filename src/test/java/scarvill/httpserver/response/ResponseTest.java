package scarvill.httpserver.response;

import org.junit.Test;

import static org.hamcrest.Matchers.containsInAnyOrder;
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

        assertThat(response.getHeaderNames(), containsInAnyOrder("Foo", "Bar"));
        assertEquals("a header", response.getHeaderContent("Foo"));
        assertEquals("another header", response.getHeaderContent("Bar"));
    }

    @Test
    public void testHasBody() {
        byte[] expectedBody = "body".getBytes();
        Response response = new ResponseBuilder().setBody(expectedBody).build();

        assertThat(expectedBody, equalTo(response.getBody()));
    }
}