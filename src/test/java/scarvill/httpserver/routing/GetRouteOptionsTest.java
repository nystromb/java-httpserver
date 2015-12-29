package scarvill.httpserver.routing;

import org.junit.Test;
import scarvill.httpserver.request.Method;
import scarvill.httpserver.request.RequestBuilder;
import scarvill.httpserver.response.Response;
import scarvill.httpserver.response.Status;

import java.util.Arrays;
import java.util.Collection;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static scarvill.httpserver.request.Method.*;

public class GetRouteOptionsTest {

    @Test
    public void testReturnsOptionsResponse() {
        Collection<Method> allowedMethods = Arrays.asList(GET, HEAD, OPTIONS);

        Response response = new GetRouteOptions(allowedMethods).apply(
            new RequestBuilder().setMethod(OPTIONS).build());

        assertEquals(Status.OK, response.getStatus());
        assertTrue(response.getHeaders().get("Allow").contains(GET.toString()));
        assertTrue(response.getHeaders().get("Allow").contains(HEAD.toString()));
        assertTrue(response.getHeaders().get("Allow").contains(OPTIONS.toString()));
    }

    @Test
    public void testOptionsIsAlwaysAllowed() {
        Collection<Method> allowedMethods = Arrays.asList();

        Response response = new GetRouteOptions(allowedMethods).apply(
            new RequestBuilder().setMethod(OPTIONS).build());

        assertEquals(Status.OK, response.getStatus());
        assertTrue(response.getHeaders().get("Allow").contains(OPTIONS.toString()));
    }
}