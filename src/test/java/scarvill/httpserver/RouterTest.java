package scarvill.httpserver;

import org.junit.Test;
import scarvill.httpserver.constants.Method;
import scarvill.httpserver.constants.Status;
import scarvill.httpserver.handlers.RouteHandler;
import scarvill.httpserver.mocks.MockHandler;

import java.util.HashMap;
import java.util.List;
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

    @Test
    public void testDynamicallyHandlesOptionsRequests() {
        Request request = new Request.Builder().setMethod(Method.OPTIONS).setURI("/").build();
        Router router = new Router();
        router.addRoute("/", Method.GET, new MockHandler(Status.OK));

        Response response = router.routeRequest(request);

        assertEquals(Status.OK, response.getStatus());
        String allowHeader = getHeaderContaining("Allow: ", response.getHeaders());

        assertTrue(allowHeader.contains("GET"));
        assertTrue(allowHeader.contains("OPTIONS"));

        router.addRoute("/", Method.POST, new MockHandler(Status.OK));

        response = router.routeRequest(request);
        allowHeader = getHeaderContaining("Allow: ", response.getHeaders());

        assertTrue(allowHeader.contains("GET"));
        assertTrue(allowHeader.contains("POST"));
        assertTrue(allowHeader.contains("OPTIONS"));
    }

    private String getHeaderContaining(String substring, List<String> headers) {
        for (String header : headers) {
            if (header.contains(substring)) {
                return header;
            }
        }
        return "";
    }
}