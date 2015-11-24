package scarvill.httpserver.handlers;

import org.junit.Test;
import scarvill.httpserver.Request;
import scarvill.httpserver.Response;
import scarvill.httpserver.constants.Method;
import scarvill.httpserver.constants.Status;
import scarvill.httpserver.constants.StatusTwo;
import scarvill.httpserver.mocks.MockHandler;

import java.util.HashMap;
import java.util.function.Function;

import static org.junit.Assert.*;

public class RouteHandlerTest {

    @Test
    public void testReturns405NotAllowedIfRequestCannotDelegatedToAnotherHandler() throws Exception {
        Request request = new Request.Builder().setMethod(Method.GET).build();
        HashMap<Method, Function<Request, Response>> methodHandlers = new HashMap<>();
        Function<Request, Response> routeHandler = new RouteHandler(methodHandlers);

        Response response = routeHandler.apply(request);

        assertEquals(StatusTwo.METHOD_NOT_ALLOWED, response.getStatus());
    }

    @Test
    public void testDelegatesToAppropriateMethodHandler() throws Exception {
        Request request = new Request.Builder().setMethod(Method.GET).build();
        HashMap<Method, Function<Request, Response>> methodHandlers = new HashMap<>();
        StatusTwo expectedResponseStatus = StatusTwo.OK;
        methodHandlers.put(Method.GET, new MockHandler(expectedResponseStatus));
        Function<Request, Response> routeHandler = new RouteHandler(methodHandlers);

        Response response = routeHandler.apply(request);

        assertEquals(expectedResponseStatus, response.getStatus());
    }
}