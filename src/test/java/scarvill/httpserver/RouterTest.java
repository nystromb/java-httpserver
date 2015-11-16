package scarvill.httpserver;

import org.junit.Test;
import scarvill.httpserver.constants.Method;
import scarvill.httpserver.constants.Status;

import static org.junit.Assert.*;

public class RouterTest {

    @Test
    public void testReturnsResponseWithStatusOKForConfiguredRoute() throws Exception {
        Request request = new Request(RequestBuilder.build(Method.GET, "/"));
        Router router = new Router();
        router.addRoute("/", new String[]{Method.GET});
        Response response = router.routeRequest(request);

        assertEquals(Status.OK, response.getStatusLine());
    }

    @Test
    public void testReturnsResponseWithStatusNotFoundForUnconfiguredRoute() throws Exception {
        Request request = new Request(RequestBuilder.build(Method.GET, "/not/configured"));
        Router router = new Router();
        Response response = router.routeRequest(request);

        assertEquals(Status.NOT_FOUND, response.getStatusLine());
    }

    @Test
    public void testReturnsMethodNotAllowedWhenPermissionDoesNotExist() throws Exception {
        Request request = new Request(RequestBuilder.build(Method.POST, "/"));
        Router router = new Router();
        String[] methodPermissions = {Method.GET, Method.HEAD, Method.OPTIONS};
        router.addRoute("/", methodPermissions);
        Response response = router.routeRequest(request);

        assertEquals(Status.METHOD_NOT_ALLOWED, response.getStatusLine());
    }
}