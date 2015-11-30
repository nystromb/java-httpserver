package scarvill.httpserver.handlers;

import org.junit.Test;
import scarvill.httpserver.Request;
import scarvill.httpserver.RequestBuilder;
import scarvill.httpserver.Response;
import scarvill.httpserver.ResponseBuilder;
import scarvill.httpserver.constants.Status;

import java.util.function.Function;

import static org.junit.Assert.assertEquals;

public class IndifferentHandlerTest {
    @Test
    public void testReturnsResponseDefinedAtInitialization() throws Exception {
        Function<Request, Response> statusOKHandler =
            new IndifferentHandler(new ResponseBuilder().setStatus(Status.OK).build());
        Function<Request, Response> statusNotFoundHandler =
            new IndifferentHandler(new ResponseBuilder().setStatus(Status.NOT_FOUND).build());
        Request anyRequest = new RequestBuilder().build();

        assertEquals(Status.OK, statusOKHandler.apply(anyRequest).getStatus());
        assertEquals(Status.NOT_FOUND, statusNotFoundHandler.apply(anyRequest).getStatus());
    }
}