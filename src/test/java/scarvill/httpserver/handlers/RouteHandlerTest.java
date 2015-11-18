package scarvill.httpserver.handlers;

import org.junit.Test;
import scarvill.httpserver.Request;
import scarvill.httpserver.RequestUtility;
import scarvill.httpserver.Response;
import scarvill.httpserver.constants.Status;
import scarvill.httpserver.mocks.MockHandler;

import java.util.HashMap;
import java.util.function.Function;

import static org.junit.Assert.*;

public class RouteHandlerTest {

    @Test
    public void testReturns405NotAllowedIfRequestCannotDelegatedToAnotherHandler() throws Exception {
        Request request = new Request(RequestUtility.rawRequest("METHOD", "/a/route"));
        HashMap<String, Function<Request, Response>> methodHandlers = new HashMap<>();
        Function<Request, Response> routeHandler = new RouteHandler(methodHandlers);
        Response response = routeHandler.apply(request);

        assertEquals(Status.METHOD_NOT_ALLOWED, response.getStatusLine());
    }

    @Test
    public void testDelegatesToAppropriateMethodHandler() throws Exception {
        Request request = new Request(RequestUtility.rawRequest("METHOD", "/a/route"));
        HashMap<String, Function<Request, Response>> methodHandlers = new HashMap<>();
        String expectedResponseStatus = "a response status\r\n";
        methodHandlers.put("METHOD", new MockHandler(expectedResponseStatus));
        Function<Request, Response> routeHandler = new RouteHandler(methodHandlers);
        Response response = routeHandler.apply(request);

        assertEquals(expectedResponseStatus, response.getStatusLine());
    }
}