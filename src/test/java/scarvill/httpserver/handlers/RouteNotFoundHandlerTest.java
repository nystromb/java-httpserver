package scarvill.httpserver.handlers;

import org.junit.Test;
import scarvill.httpserver.Request;
import scarvill.httpserver.RequestBuilder;
import scarvill.httpserver.constants.Method;
import scarvill.httpserver.constants.Status;

import static org.junit.Assert.*;

public class RouteNotFoundHandlerTest {

    @Test
    public void testReturnsResponseWith404NotFoundStatus() throws Exception {
        RouteNotFoundHandler handler = new RouteNotFoundHandler();
        Request anyRequest = new Request(RequestBuilder.build(Method.GET,"/"));

        assertEquals(Status.NOT_FOUND, handler.apply(anyRequest).getStatusLine());
    }
}