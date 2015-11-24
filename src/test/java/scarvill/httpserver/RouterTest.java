package scarvill.httpserver;

import org.junit.Test;
import scarvill.httpserver.constants.Method;
import scarvill.httpserver.constants.Status;
import scarvill.httpserver.handlers.RouteHandler;
import scarvill.httpserver.mocks.MockHandler;

import java.util.HashMap;
import java.util.function.Function;

import static org.junit.Assert.*;

public class RouterTest {

    @Test
    public void testReturnsResponseWithStatusNotFoundForUnconfiguredRoute() throws Exception {
        Request request = new Request.Builder().setURI("/unconfigured").build();
        Router router = new Router();

        Response response = router.routeRequest(request);

        assertEquals(Status.NOT_FOUND, response.getStatusLine());
    }

    @Test
    public void testReturnsMethodNotAllowedWhenNoMethodHandler() throws Exception {
        Request request = new Request.Builder().setMethod(Method.GET).setURI("/").build();
        Router router = new Router();
        HashMap<Method, Function<Request, Response>> methodHandlers = new HashMap<>();
        router.addRoute("/", new RouteHandler(methodHandlers));

        Response response = router.routeRequest(request);

        assertEquals(Status.METHOD_NOT_ALLOWED, response.getStatusLine());
    }

    @Test
    public void testReturnsResultOfApplyingCorrespondingMethodHandler() throws Exception {
        Request request = new Request.Builder().setMethod(Method.GET).setURI("/").build();
        Router router = new Router();
        HashMap<Method, Function<Request, Response>> methodHandlers = new HashMap<>();
        String expectedResponseStatus = "a response status\r\n";
        methodHandlers.put(Method.GET, new MockHandler(expectedResponseStatus));
        router.addRoute("/", new RouteHandler(methodHandlers));

        Response response = router.routeRequest(request);

        assertEquals(expectedResponseStatus, response.getStatusLine());
    }
}