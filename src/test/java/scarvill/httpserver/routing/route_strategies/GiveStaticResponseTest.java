package scarvill.httpserver.routing.route_strategies;

import org.junit.Test;
import scarvill.httpserver.request.Request;
import scarvill.httpserver.request.RequestBuilder;
import scarvill.httpserver.response.Response;
import scarvill.httpserver.response.ResponseBuilder;
import scarvill.httpserver.response.Status;
import scarvill.httpserver.routing.GiveStaticResponse;

import java.util.function.Function;

import static org.junit.Assert.assertEquals;

public class GiveStaticResponseTest {
    @Test
    public void testReturnsResponseDefinedAtInitialization() throws Exception {
        Function<Request, Response> statusOKHandler =
            new GiveStaticResponse(new ResponseBuilder().setStatus(Status.OK).build());
        Function<Request, Response> statusNotFoundHandler =
            new GiveStaticResponse(new ResponseBuilder().setStatus(Status.NOT_FOUND).build());
        Request anyRequest = new RequestBuilder().build();

        assertEquals(Status.OK, statusOKHandler.apply(anyRequest).getStatus());
        assertEquals(Status.NOT_FOUND, statusNotFoundHandler.apply(anyRequest).getStatus());
    }
}