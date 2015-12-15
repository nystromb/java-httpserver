package scarvill.httpserver.cobspec.route_strategies;

import org.junit.Test;
import scarvill.httpserver.request.Request;
import scarvill.httpserver.request.RequestBuilder;
import scarvill.httpserver.response.Response;
import scarvill.httpserver.response.Status;
import scarvill.httpserver.routing.Resource;
import scarvill.httpserver.routing.StringResource;
import scarvill.httpserver.routing.route_strategies.GetPartialContent;

import java.util.function.Function;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;

public class GetPartialContentTest {

    @Test
    public void testReturnsPartialContent() {
        Resource resource = new StringResource("0123456789");
        Function<Request, Response> routeStrategy = new GetPartialContent(resource);
        Request request = new RequestBuilder().setHeader("Range", "bytes=0-2").build();
        Response response = routeStrategy.apply(request);

        assertEquals(Status.PARTIAL_CONTENT, response.getStatus());
        assertArrayEquals("012".getBytes(), response.getBody());
    }

    @Test
    public void testReturnsPartialContentResponseWithCorrectContentLengthHeader() throws Exception {
        Resource resource = new StringResource("0123456789");
        Function<Request, Response> routeStrategy = new GetPartialContent(resource);
        Request request = new RequestBuilder().setHeader("Range", "bytes=0-2").build();
        Response response = routeStrategy.apply(request);

        assertEquals(String.valueOf(3), response.getHeaders().get("Content-Length"));
    }

    @Test
    public void testReturnsPartialContentWithNoGivenStartIndex() {
        Resource resource = new StringResource("0123456789");
        Function<Request, Response> routeStrategy = new GetPartialContent(resource);
        Request request = new RequestBuilder().setHeader("Range", "bytes=-3").build();
        Response response = routeStrategy.apply(request);

        assertEquals(Status.PARTIAL_CONTENT, response.getStatus());
        assertArrayEquals("789".getBytes(), response.getBody());
    }

    @Test
    public void testReturnsPartialContentWithNoGivenEndIndex() {
        Resource resource = new StringResource("0123456789");
        Function<Request, Response> routeStrategy = new GetPartialContent(resource);
        Request request = new RequestBuilder().setHeader("Range", "bytes=7-").build();
        Response response = routeStrategy.apply(request);

        assertEquals(Status.PARTIAL_CONTENT, response.getStatus());
        assertArrayEquals("789".getBytes(), response.getBody());
    }
}