package scarvill.httpserver.handlers;

import org.junit.Test;
import scarvill.httpserver.Request;
import scarvill.httpserver.RequestBuilder;
import scarvill.httpserver.constants.Method;
import scarvill.httpserver.constants.Status;

import static org.junit.Assert.*;

public class MethodNotAllowedHandlerTest {
    @Test
    public void testReturnsResponseWith404NotFoundStatus() throws Exception {
        MethodNotAllowedHandler handler = new MethodNotAllowedHandler();
        Request anyRequest = new Request(RequestBuilder.build(Method.GET,"/"));

        assertEquals(Status.METHOD_NOT_ALLOWED, handler.apply(anyRequest).getStatusLine());
    }
}