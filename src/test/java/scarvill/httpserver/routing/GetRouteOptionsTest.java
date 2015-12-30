package scarvill.httpserver.routing;

import org.junit.Test;
import scarvill.httpserver.request.Method;
import scarvill.httpserver.request.RequestBuilder;
import scarvill.httpserver.response.Response;
import scarvill.httpserver.response.Status;

import java.util.Arrays;
import java.util.Collection;

import static org.hamcrest.Matchers.containsString;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThat;
import static scarvill.httpserver.request.Method.*;

public class GetRouteOptionsTest {

    @Test
    public void testReturnsOptionsResponse() {
        Collection<Method> allowedMethods = Arrays.asList(GET, HEAD, OPTIONS);

        Response response = new GetRouteOptions(allowedMethods).apply(
            new RequestBuilder().setMethod(OPTIONS).build());

        assertEquals(Status.OK, response.getStatus());
        assertThat(response.getHeaders().get("Allow"), containsString(GET.toString()));
        assertThat(response.getHeaders().get("Allow"), containsString(HEAD.toString()));
        assertThat(response.getHeaders().get("Allow"), containsString(OPTIONS.toString()));
    }

    @Test
    public void testOptionsIsAlwaysAllowed() {
        Collection<Method> allowedMethods = Arrays.asList();

        Response response = new GetRouteOptions(allowedMethods).apply(
            new RequestBuilder().setMethod(OPTIONS).build());

        assertEquals(Status.OK, response.getStatus());
        assertThat(response.getHeaders().get("Allow"), containsString(OPTIONS.toString()));
    }
}