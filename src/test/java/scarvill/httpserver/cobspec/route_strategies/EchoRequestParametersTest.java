package scarvill.httpserver.cobspec.route_strategies;

import org.junit.Test;
import scarvill.httpserver.request.Request;
import scarvill.httpserver.request.RequestBuilder;
import scarvill.httpserver.response.Response;

import java.util.HashMap;
import java.util.function.Function;

import static org.junit.Assert.assertTrue;

public class EchoRequestParametersTest {
    @Test
    public void testReturnsRequestParametersInResponseBody() {
        Function<Request, Response> handler = new EchoRequestParameters();
        Request request = new RequestBuilder()
            .setParameter("foo", "bar")
            .setParameter("bar", "baz")
            .build();

        Response response = handler.apply(request);

        assertTrue(new String(response.getBody()).contains("foo = bar"));
        assertTrue(new String(response.getBody()).contains("bar = baz"));
    }
}