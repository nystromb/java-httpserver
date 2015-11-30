package scarvill.httpserver.cobspec.route_behavior;

import org.junit.Test;
import scarvill.httpserver.request.Request;
import scarvill.httpserver.request.RequestBuilder;
import scarvill.httpserver.routes.Resource;
import scarvill.httpserver.response.Response;
import scarvill.httpserver.response.Status;

import java.util.function.Function;

import static org.junit.Assert.assertEquals;

public class ModifyRouteResourceTest {
    @Test
    public void testChangesAssociatedResourceToMatchRequestBody() throws Exception {
        Resource resource = new Resource("initial data");
        Function<Request, Response> handler = new ModifyRouteResource(resource);
        Request request = new RequestBuilder().setBody("new data").build();

        handler.apply(request);

        assertEquals("new data", resource.getData());
    }

    @Test
    public void testReturnsStatusOKResponse() throws Exception {
        Resource resource = new Resource("initial data");
        Function<Request, Response> handler = new ModifyRouteResource(resource);
        Request request = new RequestBuilder().setBody("new data").build();

        Response response = handler.apply(request);

        assertEquals(Status.OK, response.getStatus());
    }
}