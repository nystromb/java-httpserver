package scarvill.httpserver.handlers;

import org.junit.Test;
import scarvill.httpserver.Request;
import scarvill.httpserver.RequestUtility;
import scarvill.httpserver.Resource;
import scarvill.httpserver.Response;

import java.util.function.Function;

import static org.junit.Assert.*;

public class GetResourceHandlerTest {
    @Test
    public void testReturnsResponseWithResourceData() throws Exception {
        Resource resource = new Resource("data");
        Function<Request, Response> handler = new GetResourceHandler(resource);
        Request request = new Request(RequestUtility.rawRequest("METHOD", "/some/route"));
        Response response = handler.apply(request);

        assertEquals(response.getBody(), "data");
    }

    @Test
    public void testReturnsResponseWithContentLengthHeader() throws Exception {
        Resource resource = new Resource("data");
        Function<Request, Response> handler = new GetResourceHandler(resource);
        Request request = new Request(RequestUtility.rawRequest("METHOD", "/some/route"));
        Response response = handler.apply(request);
        String expectedHeader = "Content-length: " + resource.getData().length() + "\r\n";

        assertTrue(response.getHeaders().contains(expectedHeader));
    }
}