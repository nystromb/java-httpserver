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
    public void testReturnsResponseWithStatusNotFoundForUnconfiguredRoute() {
        Request request = new Request.Builder().setURI("/unconfigured").build();
        Router router = new Router();

        Response response = router.routeRequest(request);

        assertEquals(Status.NOT_FOUND, response.getStatus());
    }

    @Test
    public void testReturnsMethodNotAllowedWhenNoMethodHandler() {
        Request request = new Request.Builder().setMethod(Method.GET).setURI("/").build();
        Router router = new Router();
        router.addRoute("/", Method.POST, new MockHandler(Status.OK));

        Response response = router.routeRequest(request);

        assertEquals(Status.METHOD_NOT_ALLOWED, response.getStatus());
    }

    @Test
    public void testReturnsResultOfApplyingCorrespondingMethodHandler() {
        Request request = new Request.Builder().setMethod(Method.GET).setURI("/").build();
        Status expectedResponseStatus = Status.OK;
        Router router = new Router();
        router.addRoute("/", Method.GET, new MockHandler(expectedResponseStatus));

        Response response = router.routeRequest(request);

        assertEquals(expectedResponseStatus, response.getStatus());
    }
}