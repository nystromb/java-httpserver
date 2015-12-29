package scarvill.httpserver.routing;

import org.junit.Test;
import scarvill.httpserver.request.RequestBuilder;
import scarvill.httpserver.resource.Resource;
import scarvill.httpserver.resource.StringResource;
import scarvill.httpserver.response.Response;
import scarvill.httpserver.response.Status;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;

public class GetRouteResourceTest {
    @Test
    public void testReturnsStatusOK() {
        Resource resource = new StringResource("data");

        Response response = new GetRouteResource(resource).apply(new RequestBuilder().build());

        assertEquals(Status.OK, response.getStatus());
    }

    @Test
    public void testReturnsResponseWithResourceData() throws Exception {
        Resource resource = new StringResource("data");

        Response response = new GetRouteResource(resource).apply(new RequestBuilder().build());

        assertArrayEquals("data".getBytes(), response.getBody());
    }

    @Test
    public void testReturnsResponseWithCorrectContentLengthHeader() throws Exception {
        Resource resource = new StringResource("data");

        Response response = new GetRouteResource(resource).apply(new RequestBuilder().build());

        assertEquals(String.valueOf(resource.getData().length),
            response.getHeaders().get("Content-Length"));
    }

    @Test
    public void testReturnsPartialContentWhenRequestHasRangeHeader() {
        Resource resource = new StringResource("0123456789");

        Response response = new GetRouteResource(resource).apply(
            new RequestBuilder()
                .setHeader("Range", "bytes=0-2")
                .build());

        assertEquals(Status.PARTIAL_CONTENT, response.getStatus());
        assertArrayEquals("012".getBytes(), response.getBody());
    }
}