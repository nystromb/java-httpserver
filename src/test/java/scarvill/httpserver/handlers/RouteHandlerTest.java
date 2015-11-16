package scarvill.httpserver.handlers;

import org.junit.Test;
import scarvill.httpserver.Request;
import scarvill.httpserver.RequestBuilder;
import scarvill.httpserver.Response;
import scarvill.httpserver.constants.Status;

import java.util.HashMap;
import java.util.function.Function;

import static org.junit.Assert.*;

public class RouteHandlerTest {
    @Test
    public void testDelegatesToAppropriateMethodHandlerGivenAtInit() throws Exception {
        Request request = new Request(RequestBuilder.build("METHOD", "/a/route"));
        HashMap<String, Function<Request, Response>> methodHandlers = new HashMap<>();
        methodHandlers.put("METHOD", new MockHandler());
        Function<Request, Response> routeHandler = new RouteHandler(methodHandlers);
        Response response = routeHandler.apply(request);

        assertEquals("a response status\r\n", response.getStatusLine());
    }

    @Test
    public void testReturns405NotAllowedIfRequestCannotDelegatedToAnotherHandler() throws Exception {
        Request request = new Request(RequestBuilder.build("METHOD", "/a/route"));
        HashMap<String, Function<Request, Response>> methodHandlers = new HashMap<>();
        Function<Request, Response> routeHandler = new RouteHandler(methodHandlers);
        Response response = routeHandler.apply(request);

        assertEquals(Status.METHOD_NOT_ALLOWED, response.getStatusLine());
    }

    private class MockHandler implements Function<Request, Response> {
        @Override
        public Response apply(Request request) {
            return new Response("a response status\r\n");
        }
    }
}