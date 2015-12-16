package scarvill.httpserver.routing;

import org.junit.Test;
import scarvill.httpserver.request.Request;
import scarvill.httpserver.request.RequestBuilder;
import scarvill.httpserver.response.Response;
import scarvill.httpserver.response.Status;
import scarvill.httpserver.routing.ModifyRouteResource;
import scarvill.httpserver.routing.resource.Resource;
import scarvill.httpserver.routing.resource.StringResource;

import java.util.function.Function;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;

public class ModifyRouteResourceTest {
    @Test
    public void testChangesAssociatedResourceToMatchRequestBody() throws Exception {
        Resource resource = new StringResource("initial data");
        Function<Request, Response> handler = new ModifyRouteResource(resource);
        Request request = new RequestBuilder().setBody("new data".getBytes()).build();

        handler.apply(request);

        assertArrayEquals("new data".getBytes(), resource.getData());
    }

    @Test
    public void testReturnsStatusOKResponseByDefault() throws Exception {
        Resource resource = new StringResource("initial data");
        Function<Request, Response> handler = new ModifyRouteResource(resource);
        Request request = new RequestBuilder().setBody("new data".getBytes()).build();

        Response response = handler.apply(request);

        assertEquals(Status.OK, response.getStatus());
    }

    @Test
    public void testReturnsResponseWithSpecifiedStatusIfGiven() {
        Resource resource = new StringResource("initial data");
        Function<Request, Response> handler = new ModifyRouteResource(resource, Status.NO_CONTENT);
        Request request = new RequestBuilder().setBody("new data".getBytes()).build();

        Response response = handler.apply(request);

        assertEquals(Status.NO_CONTENT, response.getStatus());
    }
}