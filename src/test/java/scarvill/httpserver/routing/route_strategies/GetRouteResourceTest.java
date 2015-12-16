package scarvill.httpserver.routing.route_strategies;

import org.junit.Test;
import scarvill.httpserver.request.Request;
import scarvill.httpserver.request.RequestBuilder;
import scarvill.httpserver.response.Response;
import scarvill.httpserver.response.Status;
import scarvill.httpserver.routing.GetRouteResource;
import scarvill.httpserver.routing.resource.Resource;
import scarvill.httpserver.routing.resource.StringResource;

import java.util.function.Function;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;

public class GetRouteResourceTest {
    @Test
    public void testReturnsStatusOK() {
        Resource resource = new StringResource("data");
        Function<Request, Response> routeStrategy = new GetRouteResource(resource);
        Request request = new RequestBuilder().build();
        Response response = routeStrategy.apply(request);

        assertEquals(Status.OK, response.getStatus());
    }

    @Test
    public void testReturnsResponseWithResourceData() throws Exception {
        Resource resource = new StringResource("data");
        Function<Request, Response> routeStrategy = new GetRouteResource(resource);
        Request request = new RequestBuilder().build();
        Response response = routeStrategy.apply(request);

        assertArrayEquals("data".getBytes(), response.getBody());
    }

    @Test
    public void testReturnsResponseWithCorrectContentLengthHeader() throws Exception {
        Resource resource = new StringResource("data");
        Function<Request, Response> routeStrategy = new GetRouteResource(resource);
        Request request = new RequestBuilder().build();
        Response response = routeStrategy.apply(request);

        assertEquals(String.valueOf(resource.getData().length),
                     response.getHeaders().get("Content-Length"));
    }

    @Test
    public void testReturnsPartialContentWhenRequestHasRangeHeader() {
        Resource resource = new StringResource("0123456789");
        Function<Request, Response> routeStrategy = new GetRouteResource(resource);
        Request request = new RequestBuilder().setHeader("Range", "bytes=0-2").build();
        Response response = routeStrategy.apply(request);

        assertEquals(Status.PARTIAL_CONTENT, response.getStatus());
        assertArrayEquals("012".getBytes(), response.getBody());
    }
}