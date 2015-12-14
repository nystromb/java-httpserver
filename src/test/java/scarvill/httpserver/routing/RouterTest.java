package scarvill.httpserver.routing;

import org.junit.Test;
import scarvill.httpserver.mocks.MockHandler;
import scarvill.httpserver.request.Method;
import scarvill.httpserver.request.Request;
import scarvill.httpserver.request.RequestBuilder;
import scarvill.httpserver.response.Response;
import scarvill.httpserver.response.Status;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

public class RouterTest {

    @Test
    public void testReturnsResponseWithStatusNotFoundForUnconfiguredRoute() {
        Request request = new RequestBuilder().setURI("/unconfigured").build();
        Router router = new Router();

        Response response = router.routeRequest(request);

        assertEquals(Status.NOT_FOUND, response.getStatus());
    }

    @Test
    public void testReturnsMethodNotAllowedWhenNoMethodHandler() {
        Request request = new RequestBuilder().setMethod(Method.GET).setURI("/").build();
        Router router = new Router();
        router.addRoute("/", Method.POST, new MockHandler(Status.OK));

        Response response = router.routeRequest(request);

        assertEquals(Status.METHOD_NOT_ALLOWED, response.getStatus());
    }

    @Test
    public void testReturnsResultOfApplyingCorrespondingMethodHandler() {
        Request request = new RequestBuilder().setMethod(Method.GET).setURI("/").build();
        Status expectedResponseStatus = Status.OK;
        Router router = new Router();
        router.addRoute("/", Method.GET, new MockHandler(expectedResponseStatus));

        Response response = router.routeRequest(request);

        assertEquals(expectedResponseStatus, response.getStatus());
    }

    @Test
    public void testDynamicallyHandlesOptionsRequests() {
        Request request = new RequestBuilder().setMethod(Method.OPTIONS).setURI("/").build();
        Router router = new Router();
        router.addRoute("/", Method.GET, new MockHandler(Status.OK));

        Response response = router.routeRequest(request);

        assertEquals(Status.OK, response.getStatus());
        assertTrue(response.getHeaders().get("Allow").contains("GET"));
        assertTrue(response.getHeaders().get("Allow").contains("OPTIONS"));

        router.addRoute("/", Method.POST, new MockHandler(Status.OK));
        response = router.routeRequest(request);

        assertTrue(response.getHeaders().get("Allow").contains("GET"));
        assertTrue(response.getHeaders().get("Allow").contains("POST"));
        assertTrue(response.getHeaders().get("Allow").contains("OPTIONS"));
    }
}