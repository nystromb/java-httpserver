package scarvill.httpserver.routing;

import junit.framework.Assert;
import org.junit.Test;
import scarvill.httpserver.request.Method;
import scarvill.httpserver.request.Request;
import scarvill.httpserver.request.RequestBuilder;
import scarvill.httpserver.response.Response;
import scarvill.httpserver.response.ResponseBuilder;
import scarvill.httpserver.response.Status;
import scarvill.httpserver.routing.route_strategies.GiveStaticResponse;

import static junit.framework.TestCase.assertFalse;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

public class VirtualResourceRouterTest {

    @Test
    public void testReturnsResponseWithStatusNotFoundForUnconfiguredRoute() {
        Request request = new RequestBuilder().setURI("/unconfigured").build();
        Router router = new VirtualResourceRouter();

        Response response = router.routeRequest(request);

        assertEquals(Status.NOT_FOUND, response.getStatus());
    }

    @Test
    public void testReturnsMethodNotAllowedWhenNoStrategyExistsForRequestMethod() {
        Request request = new RequestBuilder().setMethod(Method.GET).setURI("/").build();
        Router router = new VirtualResourceRouter();
        Response response = new ResponseBuilder().setStatus(Status.OK).build();
        router.addRoute("/", Method.POST, new GiveStaticResponse(response));

        Response routerResponse = router.routeRequest(request);

        assertEquals(Status.METHOD_NOT_ALLOWED, routerResponse.getStatus());
    }

    @Test
    public void testReturnsResultOfApplyingConfiguredRouteStrategy() {
        Request request = new RequestBuilder().setMethod(Method.GET).setURI("/").build();
        Status expectedResponseStatus = Status.OK;
        Router router = new VirtualResourceRouter();
        Response response = new ResponseBuilder().setStatus(Status.OK).build();
        router.addRoute("/", Method.GET, new GiveStaticResponse(response));

        Response routerResponse = router.routeRequest(request);

        assertEquals(expectedResponseStatus, routerResponse.getStatus());
    }

    @Test
    public void testDynamicallyHandlesOptionsRequests() {
        Request request = new RequestBuilder().setMethod(Method.OPTIONS).setURI("/").build();
        Router router = new VirtualResourceRouter();
        Response response = new ResponseBuilder().setStatus(Status.OK).build();
        router.addRoute("/", Method.GET, new GiveStaticResponse(response));

        Response routerResponse = router.routeRequest(request);

        assertEquals(Status.OK, response.getStatus());
        assertTrue(routerResponse.getHeaders().get("Allow").contains("GET"));
        assertTrue(routerResponse.getHeaders().get("Allow").contains("OPTIONS"));

        router.addRoute("/", Method.POST, new GiveStaticResponse(response));
        Response newRouterResponse = router.routeRequest(request);

        assertTrue(newRouterResponse.getHeaders().get("Allow").contains("GET"));
        assertTrue(newRouterResponse.getHeaders().get("Allow").contains("POST"));
        assertTrue(newRouterResponse.getHeaders().get("Allow").contains("OPTIONS"));
    }

    @Test
    public void testTracksIfHasConfiguredRouteForGivenURI() {
        VirtualResourceRouter router = new VirtualResourceRouter();

        assertFalse(router.hasRoute("/"));

        router.addRoute("/", Method.GET, new GiveStaticResponse(new ResponseBuilder().build()));

        assertTrue(router.hasRoute("/"));
    }
}