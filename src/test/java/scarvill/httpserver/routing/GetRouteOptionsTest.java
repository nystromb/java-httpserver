package scarvill.httpserver.routing;

import org.junit.Test;
import scarvill.httpserver.request.Method;
import scarvill.httpserver.request.Request;
import scarvill.httpserver.request.RequestBuilder;
import scarvill.httpserver.response.Response;
import scarvill.httpserver.response.Status;

import java.util.Arrays;
import java.util.Collection;
import java.util.function.Function;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

public class GetRouteOptionsTest {

    @Test
    public void testReturnsOptionsResponse() {
        Request request = new RequestBuilder().setMethod(Method.OPTIONS).build();
        Collection<Method> allowedMethods = Arrays.asList(Method.GET, Method.HEAD, Method.OPTIONS);
        Function<Request, Response> optionsStrategy = new GetRouteOptions(allowedMethods);

        Response response = optionsStrategy.apply(request);

        assertEquals(Status.OK, response.getStatus());
        assertTrue(response.getHeaders().get("Allow").contains(Method.GET.toString()));
        assertTrue(response.getHeaders().get("Allow").contains(Method.HEAD.toString()));
        assertTrue(response.getHeaders().get("Allow").contains(Method.OPTIONS.toString()));
    }
}