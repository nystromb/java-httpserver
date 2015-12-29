package scarvill.httpserver.routing;

import org.junit.Test;
import scarvill.httpserver.request.RequestBuilder;
import scarvill.httpserver.resource.Resource;
import scarvill.httpserver.resource.StringResource;
import scarvill.httpserver.response.Response;
import scarvill.httpserver.response.Status;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;

public class GetPartialContentTest {

    @Test
    public void testReturnsPartialContent() {
        Resource resource = new StringResource("0123456789");

        Response response = new GetPartialContent(resource).apply(
            new RequestBuilder()
                .setHeader("Range", "bytes=0-2")
                .build());

        assertEquals(Status.PARTIAL_CONTENT, response.getStatus());
        assertArrayEquals("012".getBytes(), response.getBody());
    }

    @Test
    public void testReturnsPartialContentResponseWithCorrectContentLengthHeader() throws Exception {
        Resource resource = new StringResource("0123456789");

        Response response = new GetPartialContent(resource).apply(
            new RequestBuilder()
                .setHeader("Range", "bytes=0-2")
                .build());

        assertEquals(String.valueOf(3), response.getHeaders().get("Content-Length"));
    }

    @Test
    public void testReturnsPartialContentWithNoGivenStartIndex() {
        Resource resource = new StringResource("0123456789");

        Response response = new GetPartialContent(resource).apply(
            new RequestBuilder()
                .setHeader("Range", "bytes=-3")
                .build());

        assertEquals(Status.PARTIAL_CONTENT, response.getStatus());
        assertArrayEquals("789".getBytes(), response.getBody());
    }

    @Test
    public void testReturnsPartialContentWithNoGivenEndIndex() {
        Resource resource = new StringResource("0123456789");

        Response response = new GetPartialContent(resource).apply(
            new RequestBuilder()
                .setHeader("Range", "bytes=7-")
                .build());

        assertEquals(Status.PARTIAL_CONTENT, response.getStatus());
        assertArrayEquals("789".getBytes(), response.getBody());
    }
}