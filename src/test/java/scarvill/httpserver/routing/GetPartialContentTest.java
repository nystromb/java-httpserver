package scarvill.httpserver.routing;

import org.junit.Test;
import scarvill.httpserver.request.RequestBuilder;
import scarvill.httpserver.resource.Resource;
import scarvill.httpserver.resource.StringResource;
import scarvill.httpserver.response.Response;
import scarvill.httpserver.response.Status;

import static org.hamcrest.Matchers.equalTo;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThat;

public class GetPartialContentTest {

    @Test
    public void testReturnsPartialContent() {
        Resource resource = new StringResource("0123456789");

        Response response = new GetPartialContent(resource).apply(
            new RequestBuilder()
                .setHeader("Range", "bytes=0-2")
                .build());

        assertEquals(Status.PARTIAL_CONTENT, response.getStatus());
        assertThat("012".getBytes(), equalTo(response.getBody()));
    }

    @Test
    public void testReturnsPartialContentResponseWithCorrectContentLengthHeader() throws Exception {
        Resource resource = new StringResource("0123456789");

        Response response = new GetPartialContent(resource).apply(
            new RequestBuilder()
                .setHeader("Range", "bytes=0-2")
                .build());

        assertEquals("3", response.getHeaders().get("Content-Length"));
    }

    @Test
    public void testReturnsPartialContentWithNoGivenStartIndex() {
        Resource resource = new StringResource("0123456789");

        Response response = new GetPartialContent(resource).apply(
            new RequestBuilder()
                .setHeader("Range", "bytes=-3")
                .build());

        assertEquals(Status.PARTIAL_CONTENT, response.getStatus());
        assertThat("789".getBytes(), equalTo(response.getBody()));

    }

    @Test
    public void testReturnsPartialContentWithNoGivenEndIndex() {
        Resource resource = new StringResource("0123456789");

        Response response = new GetPartialContent(resource).apply(
            new RequestBuilder()
                .setHeader("Range", "bytes=7-")
                .build());

        assertEquals(Status.PARTIAL_CONTENT, response.getStatus());
        assertThat("789".getBytes(), equalTo(response.getBody()));
    }
}