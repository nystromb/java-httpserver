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

        assertThat("data".getBytes(), equalTo(response.getBody()));
    }

    @Test
    public void testReturnsResponseWithCorrectContentLengthHeader() throws Exception {
        Resource resource = new StringResource("data");

        Response response = new GetRouteResource(resource).apply(new RequestBuilder().build());

        assertEquals(String.valueOf(resource.getData().length),
            response.getHeaderContent("Content-Length"));
    }

    @Test
    public void testReturnsPartialContentWhenRequestHasRangeHeader() {
        Resource resource = new StringResource("0123456789");

        Response response = new GetRouteResource(resource).apply(
            new RequestBuilder()
                .setHeader("Range", "bytes=0-2")
                .build());

        assertEquals(Status.PARTIAL_CONTENT, response.getStatus());
        assertThat("012".getBytes(), equalTo(response.getBody()));
    }
}