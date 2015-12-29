package scarvill.httpserver.routing;

import org.junit.Test;
import scarvill.httpserver.request.Request;
import scarvill.httpserver.request.RequestBuilder;
import scarvill.httpserver.resource.Resource;
import scarvill.httpserver.resource.StringResource;
import scarvill.httpserver.response.Response;
import scarvill.httpserver.response.Status;

import java.util.function.Function;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;

public class ModifyRouteResourceTest {
    @Test
    public void testChangesAssociatedResourceToMatchRequestBody() throws Exception {
        Resource resource = new StringResource("initial data");

        new ModifyRouteResource(resource).apply(
            new RequestBuilder()
                .setBody("new data".getBytes())
                .build());

        assertArrayEquals("new data".getBytes(), resource.getData());
    }

    @Test
    public void testReturnsStatusOKResponseByDefault() throws Exception {
        Response response = new ModifyRouteResource(new StringResource("")).apply(
            new RequestBuilder().build());

        assertEquals(Status.OK, response.getStatus());
    }

    @Test
    public void testReturnsResponseWithSpecifiedStatusIfGiven() {
        Function<Request, Response> modifyRouteResource =
            new ModifyRouteResource(new StringResource("initial data"), Status.NO_CONTENT);

        Response response = modifyRouteResource.apply(new RequestBuilder().build());

        assertEquals(Status.NO_CONTENT, response.getStatus());
    }
}