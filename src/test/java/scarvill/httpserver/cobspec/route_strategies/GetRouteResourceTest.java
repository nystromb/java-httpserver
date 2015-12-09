package scarvill.httpserver.cobspec.route_strategies;

import org.junit.Test;
import scarvill.httpserver.request.Request;
import scarvill.httpserver.request.RequestBuilder;
import scarvill.httpserver.response.Response;
import scarvill.httpserver.response.Status;
import scarvill.httpserver.routes.Resource;
import scarvill.httpserver.routes.StringResource;

import java.util.function.Function;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

public class GetRouteResourceTest {
    @Test
    public void testReturnsStatusOKForNonPartialContentRequest() {
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
    public void testReturnsResponseWithContentLengthHeader() throws Exception {
        Resource resource = new StringResource("data");
        Function<Request, Response> routeStrategy = new GetRouteResource(resource);
        Request request = new RequestBuilder().build();
        Response response = routeStrategy.apply(request);
        String expectedHeader = "Content-Length: " + resource.getData().length + "\r\n";

        assertTrue(response.getHeaders().contains(expectedHeader));
    }

    @Test
    public void testReturnsPartialContent() {
        Resource resource = new StringResource("data");
        Function<Request, Response> routeStrategy = new GetRouteResource(resource);
        Request request = new RequestBuilder().setHeader("Range", "bytes=0-1").build();
        Response response = routeStrategy.apply(request);

        assertEquals(Status.PARTIAL_CONTENT, response.getStatus());
        assertArrayEquals("da".getBytes(), response.getBody());
    }
}