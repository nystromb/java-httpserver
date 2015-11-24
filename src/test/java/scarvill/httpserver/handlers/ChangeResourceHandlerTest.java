package scarvill.httpserver.handlers;

import org.junit.Test;
import scarvill.httpserver.Request;
import scarvill.httpserver.Resource;
import scarvill.httpserver.Response;
import scarvill.httpserver.constants.Status;

import java.util.function.Function;

import static org.junit.Assert.*;

public class ChangeResourceHandlerTest {
    @Test
    public void testChangesAssociatedResourceToMatchRequestBody() throws Exception {
        Resource resource = new Resource("initial data");
        Function<Request, Response> handler = new ChangeResourceHandler(resource);
        Request request = new Request.Builder().setBody("new data").build();

        handler.apply(request);

        assertEquals("new data", resource.getData());
    }

    @Test
    public void testReturnsStatusOKResponse() throws Exception {
        Resource resource = new Resource("initial data");
        Function<Request, Response> handler = new ChangeResourceHandler(resource);
        Request request = new Request.Builder().setBody("new data").build();

        Response response = handler.apply(request);

        assertEquals(Status.OK, response.getStatusLine());
    }
}