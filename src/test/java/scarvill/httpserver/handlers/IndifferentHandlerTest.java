package scarvill.httpserver.handlers;

import org.junit.Test;
import scarvill.httpserver.Request;
import scarvill.httpserver.RequestUtility;
import scarvill.httpserver.Response;
import scarvill.httpserver.constants.Status;

import java.util.function.Function;

import static org.junit.Assert.*;

public class IndifferentHandlerTest {
    @Test
    public void testReturnsResponseDefinedAtInitialization() throws Exception {
        Function<Request, Response> statusOKHandler =
            new IndifferentHandler(new Response(Status.OK));
        Function<Request, Response> statusNotFoundHandler =
            new IndifferentHandler(new Response(Status.NOT_FOUND));
        Request anyRequest = new Request(RequestUtility.rawRequest("METHOD","/"));

        assertEquals(Status.OK, statusOKHandler.apply(anyRequest).getStatusLine());
        assertEquals(Status.NOT_FOUND, statusNotFoundHandler.apply(anyRequest).getStatusLine());
    }
}