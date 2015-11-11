package scarvill.httpserver;

import org.junit.Test;

import static org.junit.Assert.*;

public class RouterTest {
    @Test
    public void testReturnsResponseWithStatusOKForConfiguredRoute() throws Exception {
        Request request = new Request("GET / HTTP/1.1\r\n");
        String[] routes = new String[]{"/"};
        Router router = new Router(routes);
        Response response = router.routeRequest(request);

        assertEquals(Status.OK, response.getStatusLine());
    }

    @Test
    public void testReturnsResponseWithStatusNotFoundForUnconfiguredRoute() throws Exception {
        Request request = new Request("GET /not/configured HTTP/1.1\r\n");
        String[] routes = new String[]{};
        Router router = new Router(routes);
        Response response = router.routeRequest(request);

        assertEquals(Status.NOT_FOUND, response.getStatusLine());
    }
}