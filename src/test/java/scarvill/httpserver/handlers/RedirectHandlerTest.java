package scarvill.httpserver.handlers;

import org.junit.Test;
import scarvill.httpserver.Request;
import scarvill.httpserver.RequestBuilder;
import scarvill.httpserver.Response;
import scarvill.httpserver.constants.Status;

import java.util.function.Function;

import static org.junit.Assert.*;

public class RedirectHandlerTest {
    @Test
    public void testReturnsResponseWith302Found() throws Exception {
        Request request = new Request(RequestBuilder.build("METHOD", "some/route"));
        String redirectLocation = "http://www.domain.example";
        Function<Request, Response> handler = new RedirectHandler(redirectLocation);
        Response response = handler.apply(request);

        assertEquals(Status.FOUND, response.getStatusLine());
    }

    @Test
    public void testReturnsResponseWithLocationHeader() throws Exception {
        Request request = new Request(RequestBuilder.build("METHOD", "some/route"));
        String redirectLocation = "http://www.domain.example";
        Function<Request, Response> handler = new RedirectHandler(redirectLocation);
        Response response = handler.apply(request);

        assertTrue(response.getHeaders().contains("Location: http://www.domain.example\r\n"));
    }
}