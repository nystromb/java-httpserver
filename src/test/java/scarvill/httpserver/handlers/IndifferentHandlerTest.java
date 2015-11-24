package scarvill.httpserver.handlers;

import org.junit.Test;
import scarvill.httpserver.Request;
import scarvill.httpserver.Response;
import scarvill.httpserver.constants.Status;
import scarvill.httpserver.constants.StatusTwo;

import java.util.function.Function;

import static org.junit.Assert.*;

public class IndifferentHandlerTest {
    @Test
    public void testReturnsResponseDefinedAtInitialization() throws Exception {
        Function<Request, Response> statusOKHandler =
            new IndifferentHandler(new Response(StatusTwo.OK));
        Function<Request, Response> statusNotFoundHandler =
            new IndifferentHandler(new Response(StatusTwo.NOT_FOUND));
        Request anyRequest = new Request.Builder().build();

        assertEquals(StatusTwo.OK, statusOKHandler.apply(anyRequest).getStatus());
        assertEquals(StatusTwo.NOT_FOUND, statusNotFoundHandler.apply(anyRequest).getStatus());
    }
}