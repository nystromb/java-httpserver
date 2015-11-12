package scarvill.httpserver;

import org.junit.Test;

import static org.junit.Assert.*;

public class RouterTest {
    @Test
    public void testReturnsResponseWithStatusOKForConfiguredRoute() throws Exception {
        Request request = new Request("GET / HTTP/1.1\r\n");
        Router router = new Router();
        router.addRoute("/", new String[]{Method.GET});
        Response response = router.routeRequest(request);

        assertEquals(Status.OK, response.getStatusLine());
    }

    @Test
    public void testReturnsResponseWithStatusNotFoundForUnconfiguredRoute() throws Exception {
        Request request = new Request("GET /not/configured HTTP/1.1\r\n");
        Router router = new Router();
        Response response = router.routeRequest(request);

        assertEquals(Status.NOT_FOUND, response.getStatusLine());
    }

    @Test
    public void testReturnsMethodNotAllowedWhenPermissionDoesNotExist() throws Exception {
        Request request = new Request("POST / HTTP/1.1\r\n");
        Router router = new Router();
        String[] methodPermissions = {Method.GET, Method.HEAD, Method.OPTIONS};
        router.addRoute("/", methodPermissions);
        Response response = router.routeRequest(request);

        assertEquals(Status.METHOD_NOT_ALLOWED, response.getStatusLine());
    }
}